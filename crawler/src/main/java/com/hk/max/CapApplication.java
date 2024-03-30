package com.hk.max;

import com.hk.max.service.ICacheService;
import com.hk.max.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@Slf4j
@EnableCaching
public class CapApplication {

	@Value("${spring.app.root.dir}")
	private String appRootDir;

	@Autowired
	private ICacheService cacheService;

	public static void main(String[] args) {
		SpringApplication.run(CapApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		System.setProperty("java.awt.headless", "false");
		log.info("Starting open Google Chrome in incognito mode");
		// Set path to the ChromeDriver executable
//		System.setProperty("webdriver.chrome.driver", this.chromeDriverPath);
//		String extensionPath = rootDir + "/extension.crx";
		// Path to the key file (.pem) if the extension is packed with a key
//		String keyPath = rootDir + "/extension.pem";

		// Create ChromeOptions to enable incognito mode and open the console
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--incognito");
//		options.addExtensions(new File(extensionPath));
		// If the extension is packed with a key, add it as well
//		options.addArguments("--pack-extension-key=" + keyPath);

		// Initialize the ChromeDriver with the ChromeOptions
//		WebDriver driver = new ChromeDriver(options);

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
		// run python code to simulate key down
		String pythonCmd = "python3 " + this.appRootDir + "/autorun.py";
		boolean result = AppUtils.RunCmd(pythonCmd);
			if (result) {
				cacheService.set("login", "1");
//				driver.get(this.shopeeLink);
			}

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
