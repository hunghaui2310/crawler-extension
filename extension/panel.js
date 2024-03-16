// Copyright 2023 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

const types = {};
const LOCATION = 'Hải Dương'
const CATEGORY = 'Thời-Trang-Nam-cat.11035567'
const HASH_LOCATION = 'H%25E1%25BA%25A3i%2520D%25C6%25B0%25C6%25A1ng'
const INTERVAL_ROUTE_PAGE = 25000; // 25s
let PAGE = -1;
const MAX_PAGE = 1;
let idInterval;
let tabId;
const divElement = document.getElementById('cdata');

let currentUrl = '';
// chrome.devtools.inspectedWindow.getResources((resources) => {
//   resources.forEach((resource) => {
//     if (!(resource.type in types)) {
//       types[resource.type] = 0;
//     }
//     types[resource.type] += 1;
//   });
//   let result = `Resources on this page: 
//   ${Object.entries(types)
//     .map((entry) => {
//       const [type, count] = entry;
//       return `${type}: ${count}`;
//     })
//     .join('\n')}`;
//   let div = document.createElement('div');
//   div.innerText = result;
//   document.body.appendChild(div);
// });

function getTabId() {
    chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
        if (tabs && tabs[0]) {
            tabId = tabs[0].id;
            currentUrl = tabs[0].url
        }
    });
}

getTabId();

function download(content, fileName, contentType) {
    var a = document.createElement("a");
    var file = new Blob([content], {type: contentType});
    a.href = URL.createObjectURL(file);
    a.download = fileName;
    a.click();
  }

function routeToPage(link) {
    chrome.tabs.update(
        tabId,
        {
            active: false,
            url: link
        }
    );
}

function buildURL(category, page, sortBy = 'pop', location = HASH_LOCATION) {
    currentUrl = `https://shopee.vn/${category}?locations=${location}&page=${page}&sortBy=${sortBy}`
    return currentUrl;
}

function autoCrawlShop() {
    idInterval = setInterval(() => {
        if (PAGE < MAX_PAGE) {
            PAGE += 1
            const url = buildURL(CATEGORY, PAGE);
            routeToPage(url)
        } else {
            if (idInterval) {
                clearInterval(idInterval)
            }
        }
    }, INTERVAL_ROUTE_PAGE)
}

chrome.devtools.network.onRequestFinished.addListener(
    function(request) {
        if (request.request.url && request.request.url.includes('search_items')) {
            request.getContent((content, mimeType) => {
                const { items } = JSON.parse(content)
                const out = [];
                for (const data of items) {
                    const item = data.item_basic
                    if (!item) return
                    if (item.shop_location?.toUpperCase() === LOCATION.toUpperCase()) {
                        out.push({
                            shopid: item.shopid,
                            shopLocation: item.shop_location,
                        })
                    }
                }
                // download(JSON.stringify(out), LOCATION + '_' + (PAGE + 1) + '.txt', 'text/plain');
                // console.log(PAGE +1);
                const paragraphElement = document.createElement('p');
                paragraphElement.textContent = JSON.stringify(out);
                divElement.appendChild(paragraphElement);
                // saveRawItem(JSON.stringify(out), currentUrl)
            });
        }
    }
  );


  document.getElementById('crawl-shop').addEventListener('click', () => {
    autoCrawlShop();
  });