let currentShopIdItemDetail;
let listItemsOfShop = [];
const localStorageManagerItem = new LocalStorageManager();
let urlItemDetail;

window.addEventListener('getListShopCrawled', async (event) => {
    // let response = await getAllShopIdAPI();
    let response = [
        // 727529781,
        // 884804991,
        // 531199972,
        529573640,
        1072321433,
        // 530152797,
        // 967732774,
        // 620506764,
        // 529455673,
        // 716766179,
        // 533323302,
        // 629627436,
        // 728332247,
        // 673416314,
        // 864465563,
        // 842067593,
        // 134741968,
        // 716808781,
        // 537291716,
        // 716709822
    ];
    localStorageManagerItem.setItem(SHOP_CRAWLED_IDS, response);
    window.dispatchEvent(
        new CustomEvent(
            'getItemsListInShop'
        )
    )
});

window.addEventListener('getItemsListInShop', async (event) => {
    let shopIds = localStorageManagerItem.getItem(SHOP_CRAWLED_IDS);
    if (!shopIds || shopIds.length === 0) {
        const step3Done = document.createElement('p');
        step3Done.textContent = 'Step 3 Done';
        document.getElementById('result-crawl').appendChild(step3Done);
        return;
    }
    if (shopIds.length) {
        const [firstShop, ...shops] = shopIds;
        currentShopIdItemDetail = firstShop;
        localStorageManager.removeItem(SHOP_CRAWLED_IDS);
        localStorageManager.setItem(SHOP_CRAWLED_IDS, shops);
        listItemsOfShop = await getProductByShopAPI(currentShopIdItemDetail);
        window.dispatchEvent(
            new CustomEvent(
                'getDetailItem'
            )
        )
    }
});

window.addEventListener('getDetailItem', async (event) => {
    if (listItemsOfShop.length) {
        const [firstItem, ...items] = listItemsOfShop;
        listItemsOfShop = items;
        crawlItem(firstItem);
    }
});

function buildUrlItem(item) {
    urlShop = `https://shopee.vn/product/${item.shopid}/${item.itemid}`;
    return urlShop
}

async function crawlItem(item) {
    const url = buildUrlItem(item);
    routeToPage(url);
}

function getRandomTime() {
    const arrayTime = [3000, 5000, 6000];
    const randomIndex = Math.floor(Math.random() * arrayTime.length);
    return arrayTime[randomIndex];
}

chrome.devtools.network.onRequestFinished.addListener(
    function(request) {
        if (request.request.url && request.request.url.includes('/get_pc')) {
            request.getContent((content, mimeType) => {
                const {data} = JSON.parse(content);
                if (data && data.item && data.item.description) {
                    const phone = getPhone(data.item.description);
                    const address = getAddress(data.item.description);
                    console.log('phone', phone);
                    console.log('address', address);
                    updateShopAPI(
                        {  
                            shopid: currentShopIdItemDetail, 
                            rawInfo: data.item.description, 
                            detailAddress: address, 
                            detailPhone: phone
                        }
                    );
                }
            });
        }
        if (request.request.url && request.request.url.includes('get_ratings')) {
            request.getContent((content, mimeType) => {
                const phone = getPhone(content);
                const address = getAddress(content);
                updateShopAPI(
                    {  
                        shopid: currentShopIdItemDetail, 
                        rawInfo: content, 
                        detailAddress: address, 
                        detailPhone: phone
                    }
                );
                setTimeout(() => {
                    if (listItemsOfShop.length) {
                        window.dispatchEvent(
                            new CustomEvent(
                                'getDetailItem'
                            )
                        )
                    } else {
                        window.dispatchEvent(
                            new CustomEvent(
                                'getItemsListInShop'
                            )
                        )
                    }
                }, getRandomTime())
            });
        }
    }
);

document.getElementById('crawl-detail-item').addEventListener('click', () => {
    window.dispatchEvent(new CustomEvent(
            'getListShopCrawled',
            {
                detail: {
                    status: 'start',
                }
            }
        )
    );
    document.getElementById('crawl-detail-item').disabled = true;
});

