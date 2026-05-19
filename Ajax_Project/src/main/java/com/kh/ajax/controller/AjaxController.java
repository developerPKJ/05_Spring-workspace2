package com.kh.ajax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxController {
    @ResponseBody
    @GetMapping("/ajax/jqAjax1")
	public String ajaxMethod1(String input) {
        return String.valueOf(input.length());
    }

    @ResponseBody
    @PostMapping("/ajax/jqAjax2")
    public String ajaxMethod2(String name, int age) {
        return name + "님의 나이는 " + age + "입니다.";
    }
}
