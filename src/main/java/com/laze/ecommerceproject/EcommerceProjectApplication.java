package com.laze.ecommerceproject;

import com.laze.ecommerceproject.common.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.laze.ecommerceproject.repository")
@EnableElasticsearchRepositories(basePackages = "com.laze.ecommerceproject.esrepository")
public class EcommerceProjectApplication {

    static {
        new EnvLoader();
    }

    public static void main(String[] args) {
        SpringApplication.run(EcommerceProjectApplication.class, args);
    }

}
