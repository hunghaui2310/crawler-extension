import pyautogui
import pygetwindow as gw
import time
import platform
import subprocess

urlShopee = 'https://shopee.vn/buyer/login'
jscode = "document.querySelector('input[name=\"loginKey\"]').value = '03456789';"

def is_chrome_focused():
    chrome_windows = gw.getActiveWindow()
    return chrome_windows.strip() == 'Google Chrome'

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

def auto_open_console_and_nav_extension():
    pyautogui.keyDown('command')
    # Press and hold Option key
    pyautogui.keyDown('option')
    # Press J key
    pyautogui.press('j')
    # Release J key
    pyautogui.keyUp('j')

    # Release Option key
    pyautogui.keyUp('option')
    # Release Cmd key
    pyautogui.keyUp('command')

    pyautogui.keyDown('command')
    # time.sleep(1)
    for x in range(2):
        pyautogui.keyDown('[')
        time.sleep(0.5)
        pyautogui.keyUp('[')
    pyautogui.keyUp('command')

def auto_login(username, password):    
    # enter username
    pyautogui.typewrite(username)
    # enter password
    time.sleep(1)
    pyautogui.press('tab')
    pyautogui.typewrite(password)
    time.sleep(1)
    pyautogui.press('tab')
    time.sleep(1)
    pyautogui.press('enter')
    auto_open_console_and_nav_extension()
    
open_chrome_incognito(urlShopee)
time.sleep(3)
if is_chrome_focused():
    auto_login('dungzboo@gmail.com', 'Vietdungzykh')
else:
    print("Chrome window is not focused.")