class LocalStorageManager {
    constructor() {
      if (!window.localStorage) {
        throw new Error("localStorage is not supported by this browser");
      }
    }
  
    setItem(key, value) {
      localStorage.setItem(key, JSON.stringify(value));
    }
  
    getItem(key) {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : null;
    }
  
    removeItem(key) {
      localStorage.removeItem(key);
    }
  
    clear() {
      localStorage.clear();
    }
  
    getKeys() {
      const keys = [];
      for (let i = 0; i < localStorage.length; i++) {
        keys.push(localStorage.key(i));
      }
      return keys;
    }
  }
    