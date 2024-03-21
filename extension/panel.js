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

const CATEGORY_TREE = 'CATEGORY_TREE';
const localStorageManagerPanel = new LocalStorageManager();
const types = {};
const LOCATION = 'Hải Dương';
const HASH_LOCATION = 'H%25E1%25BA%25A3i%2520D%25C6%25B0%25C6%25A1ng';
let PAGE_CATEGORY = 0;
let idInterval;
let tabId;
let currentUrl = '';
let currentCategory;
let tempCategory;
let step;
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

function getRandomTime() {
    const arrayTime = [3000, 5000, 6000];
    const randomIndex = Math.floor(Math.random() * arrayTime.length);
    return arrayTime[randomIndex];
}

function flattenChildren(arr) {
    return arr.reduce((acc, curr) => {
        acc.push(curr);
        if (curr.children && curr.children.length > 0) {
            acc.push(...flattenChildren(curr.children));
            curr.children = null;
        }
        return acc;
    }, []);
}

function getTabId() {
    chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
        if (tabs && tabs[0]) {
            tabId = tabs[0].id;
            currentUrl = tabs[0].url
        }
    });
}

getTabId();

window.addEventListener('getCategories', (event) => {
    const {detail: {categoryTree}} = event;
    const flattenedDataCategoryTree = flattenChildren(categoryTree);
    const slicedArray = flattenedDataCategoryTree.slice(2, 3);
    localStorageManagerPanel.setItem(CATEGORY_TREE, slicedArray);
    window.dispatchEvent(
        new CustomEvent(
            'crawlShopByCategory'
        )
    )
});

window.addEventListener('crawlShopByCategory', async (event) => {
    let categoryTree = localStorageManagerPanel.getItem(CATEGORY_TREE);
    if (!categoryTree || categoryTree.length === 0) {
        await statusShopItemRawDataAPI(true);
        step = null;
        const step1Done = document.createElement('p');
        step1Done.textContent = 'Step 1 Done';
        document.getElementById('result-crawl').appendChild(step1Done);
        return;
    }
    if (categoryTree.length) {
        PAGE_CATEGORY = 0;
        const [firstCategory, ...categories] = categoryTree;
        currentCategory = firstCategory;
        localStorageManagerPanel.removeItem(CATEGORY_TREE);
        localStorageManagerPanel.setItem(CATEGORY_TREE, categories);
        crawlShopByCategory(currentCategory);
    }
});

window.addEventListener('callLoopPageCategory', (event) => {
    if (currentCategory) {
        PAGE_CATEGORY += 1;
        crawlShopByCategory(currentCategory);
    }
});

function download(content, fileName, contentType) {
    var a = document.createElement("a");
    var file = new Blob([content], { type: contentType });
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

function buildURL(category, sortBy = 'pop', location = HASH_LOCATION) {
    let {display_name, catid, parent_catid} = category;
    display_name = display_name.replace(/&/g, '');
    display_name = display_name.replace(/\s+/g, '-');
    let baseUrl;
    if (parent_catid !== 0) {
        baseUrl = `${display_name}-cat.${parent_catid}.${catid}`;
    } else {
        baseUrl = `${display_name}-cat.${catid}`;
    }
    currentUrl = `https://shopee.vn/${baseUrl}?locations=${location}&page=${PAGE_CATEGORY}&sortBy=${sortBy}`
    return currentUrl;
}

function crawlShopByCategory(category) {
    const url = buildURL(category);
    routeToPage(url);
}

chrome.devtools.network.onRequestFinished.addListener(
    function (request) {
        if (step === 1) {
            if (request.request.url && request.request.url.includes('search_items')) {
                request.getContent((content, mimeType) => {
                    console.log(PAGE_CATEGORY);
                    const { items } = JSON.parse(content);
                    if (!items) {
                        window.dispatchEvent(
                            new CustomEvent(
                                'crawlShopByCategory'
                            )
                        )
                        return;
                    }
                    const out = [];
                    for (const data of items) {
                        const item = data.item_basic
                        if (!item) return
                        if (item.shop_location?.toUpperCase() === LOCATION.toUpperCase()) {
                            out.push({
                                shopid: item.shopid,
                                shopLocation: item.shop_location,
                                catid: currentCategory.catid
                            })
                        }
                    }
                    // download(JSON.stringify(out), LOCATION + '_' + (PAGE + 1) + '.txt', 'text/plain');
                    saveShopItemRawDataAPI(JSON.stringify(out), currentUrl);
                    setTimeout(() => {
                        if (items.length < 60) {
                            window.dispatchEvent(
                                new CustomEvent(
                                    'crawlShopByCategory'
                                )
                            )
                        } else {
                            window.dispatchEvent(
                                new CustomEvent(
                                    'callLoopPageCategory'
                                )
                            )
                        }
                    }, getRandomTime());
                });
            }
        }
    }
);


document.getElementById('crawl-shop').addEventListener('click', () => {
    setTimeout(async () => {
        const allCategoriesData = await getAllCategories();
        window.dispatchEvent(new CustomEvent(
                'getCategories',
                {
                    detail: {
                        categoryTree: allCategoriesData.data.category_list
                    }
                }
            )
        );
        document.getElementById('crawl-shop').disabled = true;
        step = 1;
    }, 3000);
});

document.getElementById('download-excel').addEventListener('click', () => {
    downloadExcelAPI().then(res => {
        const url = window.URL.createObjectURL(res);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        // the filename you want
        a.download = 'Hai Duong.xlsx';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
    });
});