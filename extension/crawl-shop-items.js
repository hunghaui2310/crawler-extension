let PAGE_SHOP = 0;
let currentShopId;
let tempShopId;
const SHOP_IDS = 'SHOP_IDS';
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

chrome.devtools.network.onRequestFinished.addListener(
    function(request) {
        if (request.request.url && request.request.url.includes('get_shop_base')) {
            request.getContent((content, mimeType) => {
                if (tempShopId !== currentShopId) {
                    // console.log('get_shop_base', content);
                    //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text)
                    const { data } = JSON.parse(content)
                    const out = {
                        shopid: data.shopid,
                        username: data.account.username,
                        shopName: data.name,
                        description: data.description
                    }
                    //TODO: push data shop to BE
                    saveRawShopAPI(JSON.stringify(out), urlShop);
                }
            });
        }
        if (request.request.url && request.request.url.includes('get_shop_tab')) {
            request.getContent((content, mimeType) => {
                if (tempShopId !== currentShopId) {
                    // console.log('get_shop_tab', content);
                    //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text) - nếu có thông tin thì mới push lên
                }
            });
        }
        if (request.request.url && request.request.url.includes('shop/rcmd_items')) {
            request.getContent((content, mimeType) => {
                const {data} = JSON.parse(content);
                // console.log('content', data);
                // console.log('total', data.total);
                // console.log('page', PAGE_SHOP);
                //TODO: push data shop to BE
                saveRawProductAPI(JSON.stringify(data.items), urlShop);
                setTimeout(() => {
                    tempShopId = currentShopId;
                    if ((PAGE_SHOP + 1) * 30 > data.total) {
                        window.dispatchEvent(
                            new CustomEvent(
                                'getItemsList'
                            )
                        )
                    } else {
                        window.dispatchEvent(
                            new CustomEvent(
                                'callLoopPageShop'
                            )
                        )
                    }
                }, getRandomTime())
            });
        }
    }
);

document.getElementById('crawl-items-shop').addEventListener('click', () => {
    window.dispatchEvent(new CustomEvent(
            'getListShops',
            {
                detail: {
                    status: 'start',
                }
            }
        )
    );
    document.getElementById('crawl-items-shop').disabled = true;
});

