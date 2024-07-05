let selectedCatid = null;

async function getAllCategoriesExcel() {
  const {
    data: { category_list },
  } = await getAllCategories();
  const flattenedDataCategoryTree = flattenChildren(category_list);
  CATES = flattenedDataCategoryTree;

  document.getElementById("category-list-download").innerHTML =
    flattenedDataCategoryTree
      .map((category) => {
        let categoryName;
        categoryName = category.display_parent
          ? category.display_parent + "->" + category.display_name
          : category.display_name;
        return `<option value="${category.catid}">${categoryName}</option>`;
      })
      .join("");

      selectedCatid = flattenedDataCategoryTree[0].catid;
}

getAllCategoriesExcel();

document.getElementById('category-list-download').addEventListener('change', (event) => {
    selectedCatid = event.target.value;
  });

document.getElementById("download-excel").addEventListener("click", () => {
  // const resultCrawl = document.getElementById("result-crawl");
  // const downloading = document.createElement("p");
  // downloading.textContent = "Downloading...";
  // resultCrawl.appendChild(downloading);
  document.getElementById("overlay-loading").classList.remove("is-hidden");
  const cateFound = CATES.find((item) => item.catid == selectedCatid);
  let nameCate;
  if (cateFound) {
    nameCate = cateFound.display_parent
      ? cateFound.display_parent + "_" + cateFound.display_name
      : cateFound.display_name;
  }
  downloadExcelAPI(selectedCatid).then((res) => {
    const url = window.URL.createObjectURL(res);
    const a = document.createElement("a");
    a.style.display = "none";
    a.href = url;
    // the filename you want
    a.download = getCurrentDay() + "-" + nameCate + ".xlsx";
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
    downloading.remove(downloading);
  }).finally(() => {
    document.getElementById("overlay-loading").classList.add("is-hidden");
  });
});

document.getElementById("download-excel-all").addEventListener("click", () => {
  // const resultCrawl = document.getElementById("result-crawl");
  // const downloading = document.createElement("p");
  // downloading.textContent = "Downloading...";
  // resultCrawl.appendChild(downloading);
  document.getElementById("overlay-loading").classList.remove("is-hidden");
  document.getElementById("warning-download").classList.remove("is-hidden");
  downloadExcelAllAPI().then((res) => {
    const url = window.URL.createObjectURL(res);
    const a = document.createElement("a");
    a.style.display = "none";
    a.href = url;
    // the filename you want
    a.download = getCurrentDay() + "_Hai_Duong.xlsx";
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
    downloading.remove(downloading);
  }).finally(() => {
    document.getElementById("overlay-loading").classList.add("is-hidden");
    document.getElementById("warning-download").classList.add("is-hidden");
  });
});
