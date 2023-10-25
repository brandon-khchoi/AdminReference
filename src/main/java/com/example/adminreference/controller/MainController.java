package com.example.adminreference.controller;

import com.example.adminreference.annotation.NoLogAspect;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class MainController {
    @GetMapping("/")
    public String welcome() {
        return "WELCOME TO CELLOOK ADMIN API";
    }

    @NoLogAspect
    @GetMapping(value = "/health/checker")
    public String healthChecker() {
        return "Success";
    }
}
