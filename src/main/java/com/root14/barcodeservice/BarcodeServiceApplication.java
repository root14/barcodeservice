package com.root14.barcodeservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BarcodeServiceApplication {
    //todo consider add grafana?prometheus
    public static void main(String[] args) {
        SpringApplication.run(BarcodeServiceApplication.class, args);
    }
}
