let PAGE_SHOP = 0;
let currentShopId;
let tempShopId;
const SHOP_IDS = 'SHOP_IDS';
const SHOP_CRAWLED_IDS = 'SHOP_CRAWLED_IDS';
const localStorageManager = new LocalStorageManager();
let urlShop;

window.addEventListener('getListShops', async (event) => {
    let response = await getAllShopIdAPI();
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
        step = null;
    }
    if (shopIds.length) {
        PAGE_SHOP = 0;
        const [firstShop, ...shops] = shopIds;
        currentShopId = firstShop;
        let shop_crawled;
        if (!localStorageManager.getItem(SHOP_CRAWLED_IDS)) {
            shop_crawled = [currentShopId, ...localStorageManager.getItem(SHOP_CRAWLED_IDS)];
        } else {
            shop_crawled = [currentShopId];
        }
        localStorageManager.removeItem(SHOP_CRAWLED_IDS);
        localStorageManager.setItem(SHOP_CRAWLED_IDS, shop_crawled);
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

function getRandomTime() {
    const arrayTime = [3000, 5000, 6000];
    const randomIndex = Math.floor(Math.random() * arrayTime.length);
    return arrayTime[randomIndex];
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

chrome.devtools.network.onRequestFinished.addListener(function (request) {
  if(step === 2) {
    if (request.request.url && request.request.url.includes("get_shop_base")) {
        request.getContent((content, mimeType) => {
          // console.log('content', content);
          // console.log('phone ' + getPhone(content));
          if (tempShopId !== currentShopId) {
            // console.log('get_shop_base', content);
            //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text)
            const { data } = JSON.parse(content);
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
                    detailPhone: phone
                }
            )
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
                        detailPhone: phone
                    }
                )
            }
          }
        });
      }
      if (request.request.url && request.request.url.includes("shop/rcmd_items")) {
        request.getContent((content, mimeType) => {
          const { data } = JSON.parse(content);
          if (!data.items || (data.items && data.items.length === 0 )) {
            window.dispatchEvent(
                new CustomEvent(
                    'getItemsList'
                )
            )
            return;
        }
          saveRawProductAPI(JSON.stringify(data.items), urlShop);
          setTimeout(() => {
            tempShopId = currentShopId;
            if ((PAGE_SHOP + 1) * 30 > data.total) {
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
