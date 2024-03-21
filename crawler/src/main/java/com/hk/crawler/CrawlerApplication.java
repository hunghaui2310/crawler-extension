package com.hk.crawler;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args) {
//		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
		SpringApplication.run(CrawlerApplication.class, args);
	}
}
