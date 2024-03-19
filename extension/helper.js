// Get phone number by some format
const getPhone = (inputString) => {
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
  let phoneNumbers = null;
  phoneRegex.forEach((item) => {
    const phone = inputString.match(item);
    if (phone) phoneNumbers = phone[0];
  });
  return phoneNumbers;
};

//Get Address of shop
const getAddress = (inputString) => {
  // Get by: place, address, địa chỉ
  const stringTest = `Chào mừng bạn đã đến với cửa hàng online của TTSHOP,đến với TTSHOP các bạn thỏa sức mua sắm các mặt hàng áo thun tay lỡ quần baggy quần jean và các mặt hàng thời trang các cũng như các mặt hàng thời trang unisex-địa điểm : Hải dương- sdt : 0937406732- thời gian trả lời chat từ 8h-17h-nhập VUMITDTK-giảm 5% đơn hàng từ 99kscảm ơn các bạn đã ghé qua shop`;

  let address = null;
  const keywords = [
    "địa điểm",
    "địa chỉ",
    "place",
    "address",
    "location",
    "đ/c",
    "đ/đ",
    "dc",
    "đc",
    "d/c",
    "đ/c",
  ];

  for (const keyword of keywords) {
    const startIndex = inputString.toLowerCase().indexOf(keyword);
    if (startIndex !== -1) {
      const subString = inputString.substring(startIndex);
      const endIndex = subString.search(/",|\n/);
      // Nếu tìm thấy vị trí kết thúc
      if (endIndex !== -1) {
        return subString.substring(0, endIndex);
      } else {
        return subString;
      }
    }
  }
  return null;
};
