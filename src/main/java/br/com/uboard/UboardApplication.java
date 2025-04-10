package br.com.uboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(UboardApplication.class, args);
    }

}
