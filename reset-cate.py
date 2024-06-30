import json
import os

root_directory = os.path.dirname(os.path.abspath(__file__))

def read_category_shopee():
    with open(root_directory + '/extension/get_category_tree.json', 'r') as file:
        data = json.load(file)
    cates = []
    for item in data['data']['category_list']:
        obj = {
            "status": 0,
            "catid": item['catid']
        }
        cates.append(obj)
        for child in item['children']:
            obj2 = {
                "status": 0,
                "catid": child['catid']
            }
            cates.append(obj2)

    with open(root_directory + '/cates.json', 'w') as f:
        # Write content to the file
        json.dump(cates, f)

read_category_shopee()