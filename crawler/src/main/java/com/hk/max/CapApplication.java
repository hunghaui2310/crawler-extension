package com.hk.max;

import com.hk.max.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

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
		System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
		// Path to the Chrome extension (.crx file)
		String extensionPath = "/Users/dabeeovina/Documents/MySelf/crawler-extension/extension.crx";

		// Path to the key file (.pem) if the extension is packed with a key
		String keyPath = "/Users/dabeeovina/Documents/MySelf/crawler-extension/extension.pem";

		// Create ChromeOptions to enable incognito mode and open the console
		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--incognito");
		options.addExtensions(new File(extensionPath));
		// If the extension is packed with a key, add it as well
		options.addArguments("--pack-extension-key=" + keyPath);

		// Initialize the ChromeDriver with the ChromeOptions
		WebDriver driver = new ChromeDriver(options);

		driver.get("https://google.com/");
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
			String pythonCmd = "python3 /Users/dabeeovina/Documents/MySelf/crawler-extension/autorun.py";
			boolean result = AppUtils.RunCmd(pythonCmd);
			if (result) {
//				driver.get("https://shopee.vn");
//				this.autoLogin(driver);
				// send a socket to FE to auto crawl starting
			}
//			Runtime.getRuntime().exec("python3 /Users/dabeeovina/Documents/MySelf/crawler-extension/autorun.py");

		// Close the WebDriver
//		driver.quit();
	}

	private void autoLogin(WebDriver driver) {
		WebElement useremail = driver.findElement(By.xpath("//input[@placeholder='Email']"));
		WebElement password = driver.findElement(By.xpath("//input[@placeholder='Password']"));

		WebElement login = driver.findElement(By.xpath("//a[@class='btn-signin']"));

		useremail.sendKeys("abc@mailinator.com"); password.sendKeys("XXX");
		login.click(); String actualurl="url";

		String expectedurl= driver.getCurrentUrl();
//		Assert.assertEquals(expectedurl,actualurl);
	}
}
