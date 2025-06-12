package com.laze.ecommerceproject;

import com.laze.ecommerceproject.common.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceProjectApplication {

    static {
        new EnvLoader();
    }

    public static void main(String[] args) {
        SpringApplication.run(EcommerceProjectApplication.class, args);
    }

}
