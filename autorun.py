import pyautogui
# import pygetwindow as gw
import time
import platform
import subprocess
import json
from datetime import datetime
import os
import asyncio
import websockets

urlShopee = 'https://shopee.vn/buyer/login?next=https%3A%2F%2Fshopee.vn%2F'
currentAccount = {}
isSuccess = True


def setCurrentAccount(new_value):
    global currentAccount
    currentAccount = new_value

# Starting websocket
# Store a reference to connected clients
connected_clients = set()

async def echo(websocket, path):
    # Add the client to the set of connected clients
    connected_clients.add(websocket)
    
    try:
        async for message in websocket:
            # Echo received messages back to the client
            await websocket.send(message)
    finally:
        # Remove the client from the set of connected clients when the connection is closed
        connected_clients.remove(websocket)

async def send_message():
    global isSuccess
    while isSuccess:
        # Send a message to each connected client
        for client in connected_clients:
            await client.send("Hello from server!")
            setIsSuccess(False)
        # Wait for 5 seconds before sending the next message
        await asyncio.sleep(5)

start_server = websockets.serve(echo, "localhost", 8765)

asyncio.get_event_loop().run_until_complete(start_server)

# def is_chrome_focused():
#     chrome_windows = gw.getActiveWindow()
#     return chrome_windows.strip() == 'Google Chrome'

root_directory = os.path.dirname(os.path.abspath(__file__))

def setIsSuccess(new_value):
    global isSuccess
    isSuccess = new_value

def read_account():
    with open(root_directory + '/accounts.json', 'r') as file:
        data = json.load(file)
    found_index = None
    max_time_difference = None
    for index, row in enumerate(data):
        last_update = datetime.strptime(row['lastUpdate'], '%Y-%m-%d %H:%M:%S')  # Assuming lastUpdate is in 'YYYY-MM-DD HH:MM:SS' format
        time_difference = datetime.now() - last_update
        # Check if this row has the furthest lastUpdate
        if max_time_difference is None or time_difference > max_time_difference:
            max_time_difference = time_difference
            found_index = index
            setCurrentAccount(row)
            
    if found_index is not None:
        # Update lastUpdate for the found row
        data[found_index]['lastUpdate'] = datetime.now().strftime('%Y-%m-%d %H:%M:%S')

        # Write updated data back to the JSON file
        with open(root_directory + '/accounts.json', 'w') as file:
            json.dump(data, file)


def open_chrome_incognito(url):
    system = platform.system()
    if system == 'Windows':
        command = 'chrome.exe --incognito'
    elif system == 'Darwin':  # macOS
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
        subprocess.run('taskkill /f /im chrome.exe', shell=True)
    elif system == 'Darwin':  # macOS
        subprocess.run('osascript -e \'quit app "Google Chrome"\'', shell=True)
    elif system == 'Linux':
        subprocess.run('pkill chrome', shell=True)
    else:
        print("Unsupported operating system")

def auto_press(key):
    time.sleep(0.6)
    pyautogui.press(key)    

def auto_open_console_and_nav_extension():
    system = platform.system()
    if system == 'Darwin':  # macOS
        pyautogui.hotkey('command' , 'option', 'j')
        time.sleep(1)
        for x in range(2):
            pyautogui.hotkey('command', '[')
            time.sleep(1)
    else:
        pyautogui.hotkey('ctrl', 'shift', 'j')
        time.sleep(1)
        for x in range(2):
            pyautogui.hotkey('ctrl', '[')
            time.sleep(1)
    time.sleep(3)
    asyncio.ensure_future(send_message())


def auto_login():
    username = currentAccount['username']
    usernames = list(username)
    # enter username
    for x in usernames:
        auto_press(x)
    # pyautogui.typewrite(username)
    # enter password
    time.sleep(1)
    pyautogui.press('tab')
    time.sleep(1)
    password = currentAccount['password']
    passwords = list(password)
    for x in passwords:
        auto_press(x)
    time.sleep(1)
    pyautogui.press('tab')
    time.sleep(1)
    pyautogui.press('enter')
    auto_open_console_and_nav_extension()


read_account()
open_chrome_incognito(urlShopee)
time.sleep(5)
# if is_chrome_focused():
auto_login()
# else:
#     print("Chrome window is not focused.")


# Start a task to send messages to clients asynchronously

# Start the event loop
asyncio.get_event_loop().run_forever()
# End websocket
