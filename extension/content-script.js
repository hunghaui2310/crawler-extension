// content.js

// chrome.runtime.sendMessage({action: "getRequests"}, function(response) {
//     console.log(response.requests);
//   });

  chrome.devtools.panels.create("Crawler", "", "devtools.html", function(panel) {
    // Optional callback function
    console.log('panel')
  });

  
  