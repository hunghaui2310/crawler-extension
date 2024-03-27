package com.hk.max;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public class CapApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		log.info("Starting open Google Chrome in incognito mode");
		// Set path to the ChromeDriver executable
		System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
		// Path to the Chrome extension (.crx file)
		String extensionPath = "/Users/dabeeovina/Documents/MySelf/crawler-extension/extension.crx";

		// Path to the key file (.pem) if the extension is packed with a key
		String keyPath = "/Users/dabeeovina/Documents/MySelf/crawler-extension/extension.pem";

		// Create ChromeOptions to enable incognito mode and open the console
		ChromeOptions options = new ChromeOptions();
		options.addExtensions(new File(extensionPath));

		// If the extension is packed with a key, add it as well
		options.addArguments("--pack-extension-key=" + keyPath);

//		options.addArguments("--incognito", "--auto-open-devtools-for-tabs");

		// Initialize the ChromeDriver with the ChromeOptions
		WebDriver driver = new ChromeDriver(options);

		// Open Google Chrome
		driver.get("https://shopee.vn/");

		// At this point, Chrome should be opened in incognito mode with the console automatically opened

		// Close the WebDriver
//		driver.quit();
	}
}
