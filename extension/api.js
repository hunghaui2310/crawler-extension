const API_URL = 'http://localhost:8080/api';
const BASE_HEADER = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };

const getAllShopId = () => fetch(API_URL + "/shop", {
    method: 'GET',
}).then(response => response.json())

const saveRawShop = (data, url) => fetch(API_URL + "/shop-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())

const saveRawItem = (data, url) => fetch(API_URL + "/product-raw", {
    method: 'POST',
    headers: BASE_HEADER,
    body: JSON.stringify({ url, data })
}).then(response => response.json())
