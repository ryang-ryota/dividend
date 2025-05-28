package io.github.ryang_ryota.dividend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DividendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendApplication.class, args);
    }

}
