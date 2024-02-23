package com.hyunrian.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberJoinController {

    @GetMapping("/join")
    public String join() {
        return "member/join";
    }
}
