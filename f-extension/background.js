
/**
When we receive the message, execute the given script in the given
tab.
*/
function handleMessage(request, sender, sendResponse) {
 
    if (sender.url != browser.runtime.getURL("panel.html")) {
      return;
    }
  
    browser.tabs.executeScript(
      request.tabId, 
      {
        code: request.script
      });
    
  }
  
  /**
  Listen for messages from our devtools panel.
  */
  browser.runtime.onMessage.addListener(handleMessage); 

  browser.tabs.onUpdated.addListener(function (tabId, changeInfo, tab) {
    if (changeInfo.status == "complete" && tab.url.includes('/verify')) {
        browser.runtime.sendMessage({code: 'veridyAccount'});
    }
  });