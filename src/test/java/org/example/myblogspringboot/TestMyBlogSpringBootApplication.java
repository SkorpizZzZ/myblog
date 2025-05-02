package org.example.myblogspringboot;

import org.springframework.boot.SpringApplication;

public class TestMyBlogSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.from(MyBlogSpringBootApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
