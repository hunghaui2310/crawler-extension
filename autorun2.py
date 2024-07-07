# import pyautogui
import pygetwindow as gw
import time
import platform
import subprocess
import json
from datetime import datetime
import os
import asyncio
import websockets
import random
from pynput.keyboard import Key, Controller
import subprocess

keyboard = Controller()

urlShopee = 'https://shopee.vn/buyer/login'
currentAccount = None
isSuccess = True

def find_cate_to_craw():
    with open(root_directory + '/cates.json', 'r') as file:
        data = json.load(file)
    found_index = None
    for index, row in enumerate(data):
        if row['status'] == 0:
            found_index = index
            break
    return data[found_index]['catid']


def setCurrentAccount(new_value):
    global currentAccount
    currentAccount = new_value


def randomSleep():
    sleep_time = random.uniform(30, 60)
    time.sleep(sleep_time)


# Starting websocket
# Store a reference to connected clients
connected_clients = set()


async def echo(websocket, path):
    # Add the client to the set of connected clients
    connected_clients.add(websocket)

    # Echo received messages back to the client
    # try:
    async for message in websocket:
        parsed_data = json.loads(message)
        isActive = parsed_data['isActive']
        if bool(isActive) is True:
            isNext = update_cate(parsed_data)
            if isNext:
                setIsSuccess(True)
                randomSleep()
                asyncio.ensure_future(send_message())
            else:
                connected_clients.remove(websocket)
                run_job_again()
        else:
            update_acc_inactive(parsed_data)
            connected_clients.remove(websocket)
            run_job_again()
    # finally:
    #     print(f'remove')
        # Remove the client from the set of connected clients when the connection is closed
        # connected_clients.remove(websocket)


def run_job_again():
    time.sleep(3)
    close_chrome_tab()
    setIsSuccess(True)
    randomSleep()
    auto_run()

async def send_message():
    global isSuccess
    while isSuccess:
        # Send a message to each connected client
        for client in connected_clients:
            catid = find_cate_to_craw()
            if currentAccount is None:
                await client.send(str({ "catid": catid, "username": 'No user to login' }))
            else:
                await client.send(str({ "catid": catid, "username": currentAccount['username'] }))
            setIsSuccess(False)
        # Wait for 5 seconds before sending the next message
        await asyncio.sleep(5)

start_server = websockets.serve(echo, "localhost", 8765)

asyncio.get_event_loop().run_until_complete(start_server)

def is_chrome_focused():
    # chrome_windows = gw.getActiveWindow()
    # return chrome_windows.strip() == 'Google Chrome'
    active_window = gw.getActiveWindow()
    if active_window is None:
        return False
    return "Google Chrome" in active_window.title

root_directory = os.path.dirname(os.path.abspath(__file__))


def setIsSuccess(new_value):
    global isSuccess
    isSuccess = new_value


def update_acc_inactive(parsed_data):
    username = parsed_data['username']
    isActive = parsed_data['isActive']
    if bool(isActive) is False:
        with open(root_directory + '/accounts.json', 'r') as file:
            data = json.load(file)
    found_index = None
    for index, row in enumerate(data):
        if username == row['username']:
            found_index = index

    if found_index is not None:
        data[found_index]['status'] = 2

        # Write updated data back to the JSON file
        with open(root_directory + '/accounts.json', 'w') as file:
            json.dump(data, file)


def update_cate(parsed_data):
    status = parsed_data['status']
    catid = parsed_data['catid']
    with open(root_directory + '/cates.json', 'r') as file:
        data = json.load(file)
    found_index = None
    for index, row in enumerate(data):
        if int(row['catid']) == int(catid):
            found_index = index
    if found_index is not None:
        data[found_index]['status'] = 1
        with open(root_directory + '/cates.json', 'w') as file:
            json.dump(data, file)
    if int(status) == 1:
        return False
    if int(status) == 3:
        return True

def read_account():
    with open(root_directory + '/accounts.json', 'r') as file:
        data = json.load(file)
    found_index = None
    max_time_difference = None
    for index, row in enumerate(data):
        # Assuming lastUpdate is in 'YYYY-MM-DD HH:MM:SS' format
        last_update = datetime.strptime(row['lastUpdate'], '%Y-%m-%d %H:%M:%S')
        time_difference = datetime.now() - last_update
        # Check if this row has the furthest lastUpdate
        if (max_time_difference is None or time_difference > max_time_difference) and int(row['status']) == 1:
            max_time_difference = time_difference
            found_index = index
            setCurrentAccount(row)

    if found_index is not None:
        # Update lastUpdate for the found row
        data[found_index]['lastUpdate'] = datetime.now().strftime(
            '%Y-%m-%d %H:%M:%S')

        # Write updated data back to the JSON file
        with open(root_directory + '/accounts.json', 'w') as file:
            json.dump(data, file)


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


def open_firefox_incognito(url):
    system = platform.system()
    if system == 'Windows':
        chrome_path = 'C:/Program Files/Mozilla Firefox/firefox.exe'
        subprocess.run([chrome_path, "-private-window", url])
        return
    elif system == 'Darwin':  # macOS
        # chrome_path = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
        # subprocess.Popen([chrome_path, "--incognito"])
        # return
        command = 'open -na "Google Chrome" --args --incognito'
    elif system == 'Linux':
        command = 'google-chrome --incognito'
    else:
        print("Unsupported operating system")
        return
    try:
        subprocess.run(command + ' ' + url, shell=True)
    except Exception as e:
        print("Error when open Google Incognito:", e)


def close_chrome_tab():
    system = platform.system()
    if system == 'Windows':
        subprocess.run('taskkill /f /im firefox.exe', shell=True)
    elif system == 'Darwin':  # macOS
        subprocess.run('osascript -e \'quit app "Google Chrome"\'', shell=True)
    elif system == 'Linux':
        subprocess.run('pkill chrome', shell=True)
    else:
        print("Unsupported operating system")


def auto_press(key):
    keyboard.press(key)
    time.sleep(0.5)
    keyboard.release(key)
    time.sleep(0.2)

def press_hold(keys):
    for key in keys:
        keyboard.press(key)
    for key in reversed(keys):
        keyboard.release(key)


def auto_open_console_and_nav_extension():
    system = platform.system()
    if system == 'Darwin':  # macOS
        press_hold([Key.cmd, Key.alt, 'j'])
        time.sleep(1)
        keyboard.press(Key.cmd)
        for x in range(2):
            time.sleep(0.5)
            keyboard.press('[')
        keyboard.release('[')
        keyboard.release(Key.cmd)
    else:
        press_hold([Key.ctrl, Key.shift, 'c'])
        time.sleep(1)
        keyboard.press(Key.ctrl)
        for x in range(1):
            time.sleep(0.5)
            keyboard.press('[')
        keyboard.release('[')
        keyboard.release(Key.ctrl)
    time.sleep(3)
    asyncio.ensure_future(send_message())


def auto_login():
    if currentAccount is None:
        return
    username = currentAccount['username']
    usernames = list(username)
    # enter username
    for x in usernames:
        auto_press(x)
    # pyautogui.typewrite(username)
    # enter password
    time.sleep(1)
    auto_press(Key.tab)
    time.sleep(1)
    password = currentAccount['password']
    passwords = list(password)
    for x in passwords:
        auto_press(x)
    auto_press(Key.tab)
    time.sleep(1)
    auto_press(Key.enter)

# read_category_shopee()


# open_firefox_incognito(urlShopee)
# print("Running app")
def auto_run():
    read_account()
    if currentAccount is not None:
        open_firefox_incognito(urlShopee)
        time.sleep(7)
    # if is_chrome_focused():
        auto_login()
        auto_open_console_and_nav_extension()
    else:
        print("Chrome window is not focused.")

auto_run()

# Start a task to send messages to clients asynchronously

# Start the event loop
asyncio.get_event_loop().run_forever()
# End websocket
