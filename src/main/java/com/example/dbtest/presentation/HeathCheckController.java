package com.example.dbtest.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathCheckController {

    @GetMapping("/hcheck")
    public String hcheck() {
        return "OK";
    }
}
