package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@TestPropertySource(
        locations = "/application-test.properties",
        // Đường dẫn đến file configuration mà ta muốn sử dụng
        properties = "server.port=8081"
)
public class UsersControllerIntegrationTest {
}
