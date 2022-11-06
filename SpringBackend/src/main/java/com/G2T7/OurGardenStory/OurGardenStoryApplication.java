package com.G2T7.OurGardenStory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class OurGardenStoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OurGardenStoryApplication.class, args);
	}

}
