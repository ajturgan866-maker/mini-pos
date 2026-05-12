package com.minipos.pos.controller.admin;

import org.springframework.stereotype.Controller;
import jakarta.annotation.PostConstruct;

@Controller
public class SettingsController {

    @PostConstruct
    public void check() {
        System.out.println(">>> SettingsController загружен в контекст Spring!");
    }

}