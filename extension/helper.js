let currentCatIdGlobal;
let currentUsername;

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

function getCurrentDay() {
  let currentDate = new Date();

    let day = currentDate.getDate();
    let month = currentDate.getMonth() + 1;
    let year = currentDate.getFullYear();
    if (day < 10) {
        day = '0' + day;
    }
    if (month < 10) {
        month = '0' + month;
    }
    let formattedDate = `${day}_${month}_${year}`;

    return formattedDate;
}

function getRandomTime() {
  // const randomSeconds = Math.floor(Math.random() * (72 - 40 + 1)) + 40;
  // const randomMilliseconds = randomSeconds * 1000;
  // return randomMilliseconds;
  return Math.floor(Math.random() * (30000 - 15000 + 1)) + 15000; // Random number between 30000ms (15s) and 60000ms (30s)
}

// starting web-socket
const socket = new WebSocket("ws://localhost:8765");

socket.onopen = function(event) {
    console.log("WebSocket connection established.");
};
socket.onmessage = function(event) {
  let str = event.data
  str = str.replace(/'/g, '"');
  const outputDiv = document.getElementById("output");
    outputDiv.innerHTML += "<p>Crawling categorys: " + event.data + "</p>";
  const { catid, username } = JSON.parse(str);
  // TODO assign catid to catid global
  currentCatIdGlobal = catid;
  currentUsername = username;
  document.getElementById("crawl-items-shop").click();
    
};
socket.onerror = function(error) {
    // console.error("WebSocket error:", error);
};

// document.getElementById('send').addEventListener('click', () => {
//   const message = {
//     catid: 11035568,
//     status: 1,
//     isActive: false, // check active for account
//     username: 'hathuhuong0605@gmail.com', // username of current account
//   }
//   sendMessage(JSON.stringify(message))
// })

function sendMessage(message) {
  socket.send(message);
}
