package de.ipb_halle.curator;

import org.springframework.boot.SpringApplication;

public class TestCuratorApplication {

	public static void main(String[] args) {
		SpringApplication.from(CuratorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
