package com.hk.max;

import com.hk.max.service.ICacheService;
import com.hk.max.utils.AppUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@Slf4j
public class CapApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapApplication.class, args);
	}
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		System.setProperty("java.awt.headless", "false");
		log.info("Starting open Google Chrome in incognito mode");
		// Set path to the ChromeDriver executable
		System.setProperty("webdriver.chrome.driver", "/Users/dabeeovina/Downloads/driver/chromedriver");
		// Path to the Chrome extension (.crx file)
		String extensionPath = "/Users/dabeeovina/Desktop/ex/extension.crx";

//		System.setProperty("webdriver.chrome.driver", this.chromeDriverPath);
		Path currentWorkingDir = Paths.get("").toAbsolutePath();
		String rootDir = currentWorkingDir.getParent().toString();
//		String extensionPath = rootDir + "/extension.crx";
		// Path to the key file (.pem) if the extension is packed with a key
//		WebDriverManager.chromedriver().clearDriverCache().setup();
//		WebDriverManager.chromedriver().clearResolutionCache().setup();
		String keyPath = "/Users/dabeeovina/Desktop/ex/extension.pem";
//		String keyPath = rootDir + "/extension.pem";

		// Create ChromeOptions to enable incognito mode and open the console
		ChromeOptions options = new ChromeOptions();
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--incognito");
		options.addExtensions(new File(extensionPath));
//		options.addExtensions(new File(extensionPath));
		// If the extension is packed with a key, add it as well
		options.addArguments("--pack-extension-key=" + keyPath);
//		options.addArguments("--pack-extension-key=" + keyPath);

		// Initialize the ChromeDriver with the ChromeOptions
		WebDriver driver = new ChromeDriver(options);
//		WebDriver driver = new ChromeDriver(options);

		driver.get("https://shopee.vn/");
		// Wait for a while to let DevTools load
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// run python code to simulate key down
//			Robot robot = new Robot();
//
//			// Simulate pressing Cmd + Option + J
//			robot.keyPress(KeyEvent.VK_META);    // Press Cmd
//			robot.keyPress(KeyEvent.VK_ALT);     // Press Option
//			robot.keyPress(KeyEvent.VK_J);
//
//			robot.keyRelease(KeyEvent.VK_J);      // Release J
//			robot.keyRelease(KeyEvent.VK_ALT);    // Release Option
//			robot.keyRelease(KeyEvent.VK_META);
//		String pythonCmd = "python3 /Users/dabeeovina/Documents/MySelf/crawler-extension/autorun.py";
		// run python code to simulate key down
//		String pythonCmd = "python3 " + rootDir + "/autorun.py";
//		boolean result = AppUtils.RunCmd(pythonCmd);
//		if (result) {
//				driver.get("https://shopee.vn");
//				this.autoLogin(driver);
			// send a socket to FE to auto crawl starting
//		}
//			Runtime.getRuntime().exec("python3 /Users/dabeeovina/Documents/MySelf/crawler-extension/autorun.py");
//			if (result) {
//				driver.get(this.shopeeLink);
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
////				driver.get("https://shopee.vn");
////				this.autoLogin(driver);
//				// send a socket to FE to auto crawl starting
//			}

		// Close the WebDriver
//		driver.quit();
	}

//	private void autoLogin(WebDriver driver) {
//		WebElement useremail = driver.findElement(By.xpath("//input[@name='loginKey']"));
//		WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
//		useremail.sendKeys("dungzboo@gmail.com");
//		password.sendKeys("Vietdungzykh");
//
//		WebElement formElement = driver.findElement(By.cssSelector("div#main form"));
//		formElement.findElements(By.tagName("button")).get(1).click();

//		plants.get(1).click();

//		String expectedurl= driver.getCurrentUrl();
//		Assert.assertEquals(expectedurl,actualurl);
//	}
}
