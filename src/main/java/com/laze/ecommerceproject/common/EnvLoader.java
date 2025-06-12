package com.laze.ecommerceproject.common;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    static {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}
