package com.projeto.vendas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/adm")
    public String accessMain() {
        return "adm/home";
    }
}
