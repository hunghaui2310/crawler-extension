// Get phone number by some format
const getPhone = (inputString) => {

  const stringTest = "0123456789 0123.123.123 0123 123 123 0123-123-123 0.1.2.3.4.5.6.7.8.9 0123.1233.12 0123 1234 12 0123-1234-12";

  const phoneRegex = [
    /(\b0\d{9}\b)/g,
    /(\b0\d{3}\.\d{3}\.\d{3}\b)/g,
    /(\b0\d{3}\ \d{3}\ \d{3}\b)/g,
    /(\b0\d{3}\-\d{3}\-\d{3}\b)/g,
    /(\b0\d{3}\-\d{4}\-\d{2}\b)/g,
    /(\b0\d{2}\-\d{3}\-\d{4}\b)/g,
    /(\b0.\d{1}\.\d{1}\.\d{1}.\d{1}\.\d{1}\.\d{1}.\d{1}\.\d{1}\.\d{1}\b)/g
  ]
  let phoneNumbers = null;
  phoneRegex.forEach(item => {
    const phone = inputString.match(item);
    if (phone) phoneNumbers = phone;
  })
  return phoneNumbers;
}

//Get Address of shop
const getAddress = (inputString) => {
  // Get by: place, address, địa chỉ

  const stringTest = "Địa chỉ: 123 Đường ABC, Quận XYZ, Thành phố HCM, Việt Nam. Số điện thoại: 0123456789.";

  const addressRegex = /địa chỉ:|địa chỉ|place:|address:|(.+)/gi;
  
  let address;
  while ((address = addressRegex.exec(inputString.toLowerCase())) !== null) {
      // address[0] chứa toàn bộ phần khớp
      // address[1] chứa phần khớp của nhóm con (.+)
      return address;
      if (address[1]) {
         return address[0]
          // Đã tìm thấy địa chỉ, có thể xử lý ở đây
      }
  }
}