let PAGE_SHOP = 0;
let currentShopId;
let tempShopId;
const SHOP_IDS = 'SHOP_IDS';
const localStorageManager = new LocalStorageManager();
window.addEventListener('getListShops', (event) => {
    let response = getListShop;
    localStorageManager.setItem(SHOP_IDS, response.items);
    window.dispatchEvent(
        new CustomEvent(
            'getItemsList'
        )
    )
});

window.addEventListener('getItemsList', (event) => {
    let shopIds = localStorageManager.getItem(SHOP_IDS);
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
    return `https://shopee.vn/shop/${shopId}?page=${PAGE_SHOP}&sortBy=pop`
}

async function crawlShopItems(shopId) {
    const url = buildUrlShop(shopId);
    routeToPage(url);
}

chrome.devtools.network.onRequestFinished.addListener(
    function(request) {
        if (request.request.url && request.request.url.includes('get_shop_base')) {
            request.getContent((content, mimeType) => {
                if (tempShopId !== currentShopId) {
                    // console.log('content', content);
                    //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text)
                    const { data } = JSON.parse(content)
                    const out = {
                        username: data.account.username,
                        shopName: data.name,
                        description: data.description
                    }
                    //TODO: push data shop to BE
                }
                tempShopId = currentShopId;
            });
        }
        if (request.request.url && request.request.url.includes('get_shop_tab')) {
            request.getContent((content, mimeType) => {
                if (tempShopId !== currentShopId) {
                    // console.log('content', content);
                    //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text)
                    //TODO: push data shop to BE
                }
                tempShopId = currentShopId;
            });
        }
        if (request.request.url && request.request.url.includes('shop/rcmd_items')) {
            request.getContent((content, mimeType) => {
                const {data} = JSON.parse(content);
                console.log('content', data);
                console.log('total', data.total);
                console.log('page', PAGE_SHOP);
                //TODO: bóc tách dữ liệu lấy SĐT, địa chỉ từ raw content (text)
                //TODO: push data shop to BE
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

