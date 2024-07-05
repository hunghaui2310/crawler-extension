import json
import os

root_directory = os.path.dirname(os.path.abspath(__file__))

def read_acc():
    with open(root_directory + '/accounts.json', 'r') as file:
        data = json.load(file)
    # Overwrite all status to 1
    for entry in data:
        entry["status"] = 1

    with open(root_directory + '/accounts.json', 'w') as f:
        # Write content to the file
        json.dump(data, f, indent=2)
    print("Status values updated successfully.")

read_acc()