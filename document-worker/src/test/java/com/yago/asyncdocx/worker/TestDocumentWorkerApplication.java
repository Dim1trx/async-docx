package com.yago.asyncdocx.worker;

import org.springframework.boot.SpringApplication;

public class TestDocumentWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.from(DocumentWorkerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
