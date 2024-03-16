const API_URL = 'http://localhost:8080/api';
const BASE_HEADER = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };

const saveShopItemRawDataAPI = (data, url) => fetch(API_URL + "/shop-product-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())

const saveRawItemAPI = (data, url) => fetch(API_URL + "/product-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())

// call after crawl done all item to filter shopid from item. save if status = true
const statusRawItemAPI = (status) => fetch(API_URL + "/product-raw/status?staus=" + status, {
    method: 'GET'
}).then(response => response.json())

const getAllShopIdAPI = () => fetch(API_URL + "/shop", {
    method: 'GET',
}).then(response => response.json())

const saveRawShopAPI = (data, url) => fetch(API_URL + "/shop-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())

const statusRawShopAPI = (status) => fetch(API_URL + "/shop-raw/status?staus=" + status, {
    method: 'GET',
}).then(response => response.json())

const downloadExcelAPI = () => fetch(API_URL + "/excel/shop", {
    method: 'GET',
}).then(response => response.blob())

const getListShop = {
  items: [98353787, 255447711],
  count: 2
}