let PAGE_SHOP = 0;
let currentShopId;
let tempShopId;
const SHOP_IDS = "SHOP_IDS";
const localStorageManager = new LocalStorageManager();
let urlShop;
let currentAddress;
let currentPhone;

window.addEventListener("getListShops", async (event) => {
  // let response = await getAllShopIdAPI();
  localStorageManager.setItem(SHOP_IDS, [255447711, 134741968]);
  window.dispatchEvent(new CustomEvent("getItemsList"));
});

window.addEventListener("getItemsList", async (event) => {
  let shopIds = localStorageManager.getItem(SHOP_IDS);
  if (!shopIds || shopIds.length === 0) {
    await statusRawShopAPI(true);
    await statusRawItemAPI(true);
  }
  if (shopIds.length) {
    PAGE_SHOP = 0;
    const [firstShop, ...shops] = shopIds;
    currentShopId = firstShop;
    localStorageManager.removeItem(SHOP_IDS);
    localStorageManager.setItem(SHOP_IDS, shops);
    crawlShopItems(currentShopId);
  }
});

window.addEventListener("callLoopPageShop", (event) => {
  if (currentShopId) {
    PAGE_SHOP += 1;
    crawlShopItems(currentShopId);
  }
});

function buildUrlShop(shopId) {
  urlShop = `https://shopee.vn/shop/${shopId}?page=${PAGE_SHOP}&sortBy=pop`;
  return urlShop;
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
  if (request.request.url && request.request.url.includes("get_shop_base")) {
    request.getContent((content, mimeType) => {
      // console.log('content', content);
      // console.log('phone ' + getPhone(content));
      if (tempShopId !== currentShopId) {
        // console.log('get_shop_base', content);
        //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text)
        const { data } = JSON.parse(content);
        currentPhone = getPhone(content);
        currentAddress = getAddress(content);
        saveShop(data, currentPhone, currentAddress);
      }
    });
  }
  if (request.request.url && request.request.url.includes("get_shop_tab")) {
    request.getContent((content, mimeType) => {
      // console.log('content', content);

      if (tempShopId !== currentShopId) {
        const { data } = JSON.parse(content);
        console.log("phone: " + getPhone(content));
        console.log("address: " + getAddress(content));
        const phone = getPhone(content);
        const address = getAddress(content);
        let isUpdate = false;
        if (phone && phone !== currentPhone) {
          currentPhone = phone;
          isUpdate = true;
        }
        if (address && address !== currentAddress) {
          currentAddress = address;
          isUpdate = true;
        }
        if (isUpdate) {
          saveShop(data, currentPhone, currentAddress);
        }
      }
    });
  }
  if (request.request.url && request.request.url.includes("shop/rcmd_items")) {
    request.getContent((content, mimeType) => {
      const { data } = JSON.parse(content);
      console.log("phone: " + getPhone(content));
      console.log("address: " + getAddress(content));
      const phone = getPhone(content);
      const address = getAddress(content);
      let isUpdate = false;
      if (phone && phone !== currentPhone) {
        currentPhone = phone;
        isUpdate = true;
      }
      if (address && address !== currentAddress) {
        currentAddress = address;
        isUpdate = true;
      }
      if (isUpdate) {
        saveShop(data, currentPhone, currentAddress);
      }
      //TODO: push data shop to BE
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
});

document.getElementById("crawl-items-shop").addEventListener("click", () => {
  window.dispatchEvent(
    new CustomEvent("getListShops", {
      detail: {
        status: "start",
      },
    })
  );
  document.getElementById("crawl-items-shop").disabled = true;
});
