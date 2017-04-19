package com.ibm.mobilefirstplatform.appid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dominik Matta
 */
@SpringBootApplication
@RestController
public class ServerSdkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerSdkApplication.class, args);
    }

    @GetMapping("/something")
    public String testEndpoint() {
        return "works";
    }
}
