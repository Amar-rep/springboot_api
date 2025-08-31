package com.codewithmosh.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "Home");
        return "index";
    }
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "samir");

        return "index";
    }
}
