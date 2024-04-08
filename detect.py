import re
import json
import phonenumbers
from phonenumbers import carrier, geocoder, timezone


# def find_phone_numbers(text):
#     phone_numbers = re.findall(r'\b(?:\d{3}[-\.\s]??\d{3}[-\.\s]??\d{4}|\(\d{3}\)\s*\d{3}[-\.\s]??\d{4}|\d{3}[-\.\s]??\d{4})\b', text)
#     return phone_numbers

# json_string = ''

# # Parse JSON
# data = json.loads(json_string)

# # Flatten JSON to get all string values
# def flatten_dict(d):
#     for k, v in d.items():
#         if isinstance(v, dict):
#             yield from flatten_dict(v)
#         else:
#             yield v

# all_values = list(flatten_dict(data))

# # Find phone numbers
# phone_numbers = []
# for value in all_values:
#     if isinstance(value, str):
#         phone_numbers.extend(find_phone_numbers(value))

# print("Phone numbers found:", phone_numbers)



# def find_phone_numbers(text):
#     phone_numbers = re.findall(r'\b\d{1,2}\.\d{1,2}\.\d{1,2}\.\d{1,2}\.\d{1,2}\.\d{1,2}\.\d{1,2}\.\d{1,2}\b', text)
#     return phone_numbers

# text = "Chào mừng bạn đã đến với cửa hàng online của TTSHOP,đến với TTSHOP các bạn thỏa sức mua sắm các mặt hàng áo thun tay lỡ quần baggy quần jean và các mặt hàng thời trang các cũng như các mặt hàng thời trang unisex -địa điểm : Hải dương - Tele : 0.9.3.74.0.6.7.3.2 - thời gian trả lời chat từ 8h-17h -nhập VUMITDTK-giảm 5% đơn hàng từ 99k cảm ơn các bạn đã ghé qua shop"
text = "chuyên lông,hoodie,áo dạ sỉ zalo 0357692321"

# phone_numbers = find_phone_numbers(text)
# print("Phone numbers found:", phone_numbers)


# Sample text containing phone numbers
# text = "Please contact me at +1 (555) 123-4567 or 123-456-7890. Thank you!"

# Parse the text and extract phone numbers
for match in phonenumbers.PhoneNumberMatcher(text, "VN"):
    phone_number = phonenumbers.format_number(match.number, phonenumbers.PhoneNumberFormat.E164)
    print("Phone number found:", phone_number)