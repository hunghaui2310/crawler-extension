const API_URL = 'http://localhost:8080/api';


const saveRawItem = (data, url) => fetch(API_URL + "/shop-raw", {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ url, data })
})
  .then(response => response.json())
