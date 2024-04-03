let currentCatIdGlobal;

// Get phone number by some format
const getPhone = (inputString) => {
  if (!inputString) {
    return null;
  }
  const stringTest =
    "0123456789 0123.123.123 0123 123 123 0123-123-123 0.1.2.3.4.5.6.7.8.9 0123.1233.12 0123 1234 12 0123-1234-12";

  const phoneRegex = [
    /(\b0\d{9}\b)/g,
    /(\b0\d{3}\.\d{3}\.\d{3}\b)/g,
    /(\b0\d{3}\ \d{3}\ \d{3}\b)/g,
    /(\b0\d{3}\-\d{3}\-\d{3}\b)/g,
    /(\b0\d{3}\-\d{4}\-\d{2}\b)/g,
    /(\b0\d{2}\-\d{3}\-\d{4}\b)/g,
    /(\b0.\d{1}\.\d{1}\.\d{1}.\d{1}\.\d{1}\.\d{1}.\d{1}\.\d{1}\.\d{1}\b)/g,
  ];
  let phoneNumbers = [];

  phoneRegex.forEach((item) => {
    const phones = inputString.match(item);
    if (phones) {
      phoneNumbers = [...phoneNumbers, ...phones];
    }
  });

  return phoneNumbers.join('\n ');
};

//Get Address of shop
const getAddress = (inputString) => {
  if (!inputString) {
    return null;
  }
  // Get by: place, address, địa chỉ
  // const inputString = `"\u003c3 Tuyển ctv toàn quốc lấy hàng giá tận gốc\nVàng Bạc Khải Khải - Trang Sức Bạc 9999 cam kết :\n1 - Sản phẩm được chế tác từ LÀNG NGHỀ VÀNG BẠC LƯƠNG NGỌC\n2 - Shop cam kết cả về chất liệu vàng bạc  cũng như KIỂU DÁNG 100% GIỐNG ẢNH \n3 - bảo hành miễn phí làm sáng trọn đời\n4 - Sau quá trình sử dụng có thể bán lại \n5 - Hoàn tiền 100% nếu không phải vàng bạc \n📞 hotline: 0839463999🏡 Add: Lương Ngọc - Thúc Kháng- Bìng Giang-Hải Dương\n😄 Admin: KHẢI KHẢI"`;

  const keywords = [
    "địa điểm",
    "địa chỉ",
    "place",
    "address",
    "location",
    "đ/c",
    "đ/đ",
    "đc",
    "d/c",
    "đ/c",
    "hải dương",
    "hai duong",
    "hd",
    "add"
  ];

  let addressesSet = new Set();

  for (const keyword of keywords) {
    let startIndex = -1;
    while ((startIndex = inputString.toLowerCase().indexOf(keyword, startIndex + 1)) !== -1) {
      const startKey = inputString.lastIndexOf('\n', startIndex);
      const endIndex = inputString.indexOf('\n', startIndex);
      // console.log('test', inputString.substring(startKey, endIndex + 1));
      // Nếu tìm thấy vị trí kết thúc
      if (endIndex !== -1) {
        addressesSet.add(inputString.substring(startKey, endIndex + 1));
        continue;
      }
  }
}

// Chuyển Set thành mảng và nối các địa chỉ thành một chuỗi
const uniqueAddresses = Array.from(addressesSet);
return uniqueAddresses.join('\n|');
};

// starting web-socket
const socket = new WebSocket("ws://localhost:8765");

socket.onopen = function(event) {
    console.log("WebSocket connection established.");
};
socket.onmessage = function(event) {
  const catid = event.data;
  // TODO assign catid to catid global
  // currentCatIdGlobal = catid;
  document.getElementById("crawl-shop").click();
    // const outputDiv = document.getElementById("output");
    // outputDiv.innerHTML += "<p>Server says: " + event.data + "</p>";
};
socket.onerror = function(error) {
    console.error("WebSocket error:", error);
};
function sendMessage(message) {
  socket.send(message);
}
