let PAGE_SHOP = 0;
let currentShopId;
let tempShopId;
const SHOP_IDS = 'SHOP_IDS';
const SHOP_CRAWLED_IDS = 'SHOP_CRAWLED_IDS';
const localStorageManager = new LocalStorageManager();
let CATES = [];
let urlShop;
let pageRequest = 0;

async function getAllCategoriesStep2() {
  const { data: { category_list } } = await getAllCategories();
  const flattenedDataCategoryTree = flattenChildren(category_list);
  CATES = flattenedDataCategoryTree

  document.getElementById('category-list').innerHTML = flattenedDataCategoryTree.map((category, index) => {
    let categoryName;
    categoryName = category.display_parent ? category.display_parent + '->' + category.display_name : category.display_name;
    if (index === 0) {
      currentCatIdGlobal = category.catid
    }
    return `<option value="${category.catid}">${categoryName}</option>`
  }).join('')
  // currentCatIdGlobal = flattenedDataCategoryTree[0].catid;
}

getAllCategoriesStep2();

document.getElementById('category-list').addEventListener('change', (event) => {
  currentCatIdGlobal = event.target.value;
  // console.log('currentCatIdGlobal', currentCatIdGlobal);
});

window.addEventListener('getListShops', async (event) => {
  let response = await getAllShopIdAPI(false, currentCatIdGlobal);
  localStorageManager.setItem(SHOP_IDS, response);
  window.dispatchEvent(
    new CustomEvent(
      'getItemsList'
    )
  )
});

window.addEventListener('getItemsList', async (event) => {
  let shopIds = localStorageManager.getItem(SHOP_IDS);
  if (!shopIds || shopIds.length === 0) {
    await statusRawShopAPI(true);
    await statusRawItemAPI(true);
    step = 2;
    document.getElementById("crawl-items-shop").disabled = false;
    const step2Done = document.createElement('p');
    const categryFound = CATES.find(item => item.catid == currentCatIdGlobal)
    const catName = categryFound.display_parent ? categryFound.display_parent + '->' + categryFound.display_name : categryFound.display_name
    step2Done.textContent = 'Done ' + catName;
    document.getElementById('result-crawl').appendChild(step2Done);
    const message = {
      catid: currentCatIdGlobal,
      status: 3,
      isActive: true,
      username: currentUsername
    };
    // chrome.browsingData.remove(
    //   {},
    //   {
    //     "cache": true,
    //     "cacheStorage": true,
    //     "cookies": true,
    //   }
    // );
    sendMessage(JSON.stringify(message));
    return;
  }
  if (shopIds.length) {
    PAGE_SHOP = 0;
    const [firstShop, ...shops] = shopIds;
    currentShopId = firstShop;
    let lastPage = await getLastPageOfShop(currentShopId);
    PAGE_SHOP = lastPage
    // let shop_crawled;
    // if (localStorageManager.getItem(SHOP_CRAWLED_IDS)) {
    //     shop_crawled = [currentShopId, ...localStorageManager.getItem(SHOP_CRAWLED_IDS)];
    // } else {
    //     shop_crawled = [currentShopId];
    // }
    // localStorageManager.removeItem(SHOP_CRAWLED_IDS);
    // localStorageManager.setItem(SHOP_CRAWLED_IDS, shop_crawled);
    localStorageManager.removeItem(SHOP_IDS);
    localStorageManager.setItem(SHOP_IDS, shops);
    crawlShopItems(currentShopId);
  }
});

window.addEventListener('callLoopPageShop', (event) => {
  if (currentShopId) {
    PAGE_SHOP += 1;
    crawlShopItems(currentShopId);
  }
});

function buildUrlShop(shopId) {
  urlShop = `https://shopee.vn/shop/${shopId}?page=${PAGE_SHOP}&sortBy=pop`
  return urlShop
}

async function crawlShopItems(shopId) {
  const url = buildUrlShop(shopId);
  routeToPage(url);
}

function saveShop(data, phone, address) {
  const out = {
    shopid: data.shopid,
    username: data.account.username,
    shopName: data.name,
    description: data.description,
    phone: phone,
    address: address,
  };
  console.log(out);
  saveRawShopAPI(JSON.stringify(out), urlShop);
}

function checkAccountVerify() {
  chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
    if (tabs && tabs[0]) {
      if (tabs[0].url.includes('/verify')) {
        const message = {
          catid: currentCatIdGlobal,
          status: 2,
          isActive: false,
          username: currentUsername
        }
        sendMessage(JSON.stringify(message));
      }
    }
  });
}

chrome.devtools.network.onRequestFinished.addListener(function (request) {
  if (step === 2) {
    checkAccountVerify();
    if (request.request.url && request.request.url.includes('captcha/generate')) {
      const message = {
        catid: currentCatIdGlobal,
        status: 2,
        isActive: true,
        username: currentUsername
      }
      // chrome.browsingData.remove(
      //   {},
      //   {
      //     "cache": true,
      //     "cacheStorage": true,
      //     "cookies": true,
      //   }
      // );
      sendMessage(JSON.stringify(message));
    }
    if (request.request.url && request.request.url.includes("get_shop_base")) {
      request.getContent((content, mimeType) => {
        // console.log('content', content);
        // console.log('phone ' + getPhone(content));

        if (tempShopId !== currentShopId) {
          // console.log('get_shop_base', content);
          //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text)
          const { data } = JSON.parse(content);
          if (data.account.status !== 1) {
            setTimeout(() => {
              changeShopCrawlDone(currentShopId, false);
              window.dispatchEvent(
                new CustomEvent(
                  'getItemsList'
                )
              )
            }, getRandomTime());
            return;
          }
          const phone = getPhone(data.description);
          const address = getAddress(data.description);
          const out = {
            shopid: data.shopid,
            username: data.account.username,
            shopName: data.name,
            description: data.description
          };
          console.log(out);
          saveRawShopAPI(JSON.stringify(out), urlShop);
          updateShopAPI(
            {
              shopid: currentShopId,
              rawInfo: content,
              detailAddress: address,
              detailPhone: phone,
              ctime: data.ctime
            }
          )
          if (data.item_count === 0) {
            changeShopCrawlDone(currentShopId, true);
            setTimeout(() => {
              window.dispatchEvent(
                new CustomEvent(
                  'getItemsList'
                )
              )
            }, getRandomTime());
            return;
          }
        }
      });
    }
    if (request.request.url && request.request.url.includes("get_shop_tab")) {
      request.getContent((content, mimeType) => {
        // console.log('content', content);

        if (tempShopId !== currentShopId) {
          const { data } = JSON.parse(content);
          // console.log("phone: " + getPhone(content));
          // console.log("address: " + getAddress(content));
          const phone = getPhone(content);
          const address = getAddress(content);
          if (phone || address) {
            updateShopAPI(
              {
                shopid: currentShopId,
                rawInfo: content,
                detailAddress: address,
                detailPhone: phone,
                ctime: data.ctime
              }
            )
          }
        }
      });
    }
    if (request.request.url && request.request.url.includes("shop/rcmd_items")) {
      request.getContent((content, mimeType) => {
        const { data, error } = JSON.parse(content);
        ++pageRequest;
        if (error) {
          setTimeout(() => {
          changeShopCrawlDone(currentShopId, false);
            window.dispatchEvent(
              new CustomEvent(
                'getItemsList'
              )
            )
          }, getRandomTime());
        }
        if (!data?.items || (data.items && data.items.length === 0)) {
          if (pageRequest >= 1000000) {
            const message = {
              catid: currentCatIdGlobal,
              status: 2,
              isActive: true,
              username: currentUsername
            }
            // chrome.browsingData.remove(
            //   {},
            //   {
            //     "cache": true,
            //     "cacheStorage": true,
            //     "cookies": true,
            //   }
            // );
            sendMessage(JSON.stringify(message));
            return;
          };
          changeShopCrawlDone(currentShopId, true);
          setTimeout(() => {
            window.dispatchEvent(
              new CustomEvent(
                'getItemsList'
              )
            )
          }, getRandomTime());
          return;
        }
        saveRawProductAPI(JSON.stringify(data.items), urlShop);
        setTimeout(() => {
          tempShopId = currentShopId;
          if ((PAGE_SHOP + 1) * 30 > data.total) {
            changeShopCrawlDone(currentShopId, true);
            window.dispatchEvent(new CustomEvent("getItemsList"));
          } else {
            window.dispatchEvent(new CustomEvent("callLoopPageShop"));
          }
        }, getRandomTime());
      });
    }
  }
});

document.getElementById("crawl-items-shop").addEventListener("click", () => {
  window.dispatchEvent(
    new CustomEvent("getListShops", {
      detail: {
        status: "start",
      },
    })
  );
  step = 2;
  document.getElementById("crawl-items-shop").disabled = true;
});