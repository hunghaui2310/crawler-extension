const API_URL = 'http://localhost:8080/api';
const BASE_HEADER = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };

// shop-product-raw API
const saveShopItemRawDataAPI = (data, url) => fetch(API_URL + "/shop-product-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())

const statusShopItemRawDataAPI = (status) => fetch(API_URL + "/shop-product-raw/status?status=" + status, {
    method: 'GET'
}).then(response => response.json())



// product-raw API
const saveRawProductAPI = (data, url) => fetch(API_URL + "/product-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())

// call after crawl done all item to filter shopid from item. save if status = true
const statusRawItemAPI = (status) => fetch(API_URL + "/product-raw/status?status=" + status, {
    method: 'GET'
}).then(response => response.json())



// shop API
const getAllShopIdAPI = (isCrawled = false, catid) => fetch(API_URL + `/shop?isCrawled=${isCrawled}&catid=${catid}`, {
    method: 'GET',
}).then(response => response.json())

// shop API
const getCategoriesCrawled = () => fetch(API_URL + "/shop-product-raw/getDataCrawled", {
    method: 'GET',
}).then(response => response.json())

/* shopInfo: {  shopid: string, 
                rawInfo: string, 
                detailAddress: string, 
                detailPhone: string } */
const updateShopAPI = (shopInfo) => fetch(API_URL + "/shop/update-info", {
    method: 'PUT',
    headers: BASE_HEADER,
    body: JSON.stringify(shopInfo)
}).then(response => response.json())

const changeShopCrawlDone = (shopId, isCrawlDone) => fetch(API_URL + "/shop/isCrawlDone?shopid=" 
            + shopId + "&isDone=" + isCrawlDone, {
    method: 'GET',
}).then(response => response.json())


// shop-raw API
const saveRawShopAPI = (data, url) => fetch(API_URL + "/shop-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())

const statusRawShopAPI = (status) => fetch(API_URL + "/shop-raw/status?status=" + status, {
    method: 'GET',
}).then(response => response.json())

const downloadExcelAPI = (catid) => fetch(API_URL + "/excel/shop?catid=" + catid, {
    method: 'GET',
}).then(response => response.blob())



// product API
const getProductByShopAPI = (shopId) => fetch(API_URL + "/product/getByShop?shopid=" + shopId, {
    method: 'GET',
}).then(response => response.json())

const getAllCategories = () => fetch('./get_category_tree.json', {
  method: 'GET',
}).then(response => response.json())

// check Server status
// const checkServerAPI = () => fetch(API_URL + "/status", {
//     method: 'GET',
// }).then(response => response.json())

const getListShop = {
  items: [98353787, 255447711],
  count: 2
}