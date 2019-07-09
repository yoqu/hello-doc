package com.uyoqu.hello.docs.runner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello-docs")
public class StaticController {

    @RequestMapping(value = {"","/"})
    public String index(){
        return "redirect:hello-docs/index.html";
    }
}
