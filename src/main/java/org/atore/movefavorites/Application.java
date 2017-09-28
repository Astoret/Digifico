package org.atore.movefavorites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static final String REALM = "FAVAPP";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
