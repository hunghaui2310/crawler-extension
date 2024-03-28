import pyautogui
import pygetwindow as gw

def is_chrome_focused():
    chrome_windows = gw.getActiveWindow()
    return chrome_windows.strip() == 'Google Chrome'

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

    for x in range(12):
        pyautogui.keyDown('command')
        pyautogui.keyDown(']')
        pyautogui.keyUp(']')
        pyautogui.keyUp('command')

if is_chrome_focused():
    auto_open_console_and_nav_extension()
else:
    print("Chrome window is not focused.")