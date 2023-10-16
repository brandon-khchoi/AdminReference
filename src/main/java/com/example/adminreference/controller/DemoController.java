package com.example.adminreference.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu1")
public class DemoController {

    @GetMapping("/submenu1")
    public String menu1Get() {
        return "success";
    }

    @PostMapping("/submenu1")
    public String menu1Post() {
        return "success";
    }
}
