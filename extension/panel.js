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
const MAX_PAGE = 17;
let idInterval;
let tabId;
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

function autoCrawl() {
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
                            itemid: item.itemid,
                            shopid: item.shopid,
                            name: item.name,
                            currency: item.currency,
                            stock: item.stock,
                            status: item.status,
                            sold: item.sold,
                            historical_sold: item.historical_sold,
                            liked: item.liked,
                            liked_count: item.liked_count,
                            view_count: item.view_count,
                            catid: item.catid,
                            brand: item.brand,
                            cmt_count: item.cmt_count,
                            flag: item.flag,
                            cb_option: item.cb_option,
                            item_status: item.item_status,
                            price: item.price,
                            price_min: item.price_min,
                            price_max: item.price_max,
                            price_min_before_discount: item.price_min_before_discount,
                            price_max_before_discount: item.price_max_before_discount,
                            hidden_price_display: item.hidden_price_display,
                            price_before_discount: item.price_before_discount,
                            has_lowest_price_guarantee: item.has_lowest_price_guarantee,
                            show_discount: item.show_discount,
                            raw_discount: item.raw_discount,
                            discount: item.discount,
                            is_category_failed: item.is_category_failed,
                            item_type: item.item_type,
                            reference_item_id: item.reference_item_id,
                            is_adult: item.is_adult,
                            shopee_verified: item.shopee_verified,
                            is_official_shop: item.is_official_shop,
                            show_official_shop_label: item.show_official_shop_label,
                            show_shopee_verified_label: item.show_shopee_verified_label,
                            show_official_shop_label_in_title: item.show_official_shop_label_in_title,
                            is_cc_installment_payment_eligible: item.is_cc_installment_payment_eligible,
                            is_non_cc_installment_payment_eligible: item.is_non_cc_installment_payment_eligible,
                            show_free_shipping: item.show_free_shipping,
                            preview_info: item.preview_info,
                            exclusive_price_info: item.exclusive_price_info,
                            add_on_deal_info: item.add_on_deal_info,
                            is_preferred_plus_seller: item.is_preferred_plus_seller,
                            shop_location: item.shop_location,
                            voucher_info: item.voucher_info,
                            can_use_cod: item.can_use_cod,
                            is_on_flash_sale: item.is_on_flash_sale,
                            is_live_streaming_price: item.is_live_streaming_price,
                            is_mart: item.is_mart,
                            free_shipping_info: item.free_shipping_info,
                            model_id: item.model_id
                        })
                    }
                }
                // download(JSON.stringify(out), LOCATION + '_' + (PAGE + 1) + '.txt', 'text/plain');
                // saveRawItem(JSON.stringify(out), currentUrl)
            });
        }
    }
  );


  document.getElementById('crawl-shop').addEventListener('click', () => {
    autoCrawl();
  });