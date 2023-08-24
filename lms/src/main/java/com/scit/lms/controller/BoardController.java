package com.scit.lms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class BoardController {

    // 메인 화면
    @GetMapping("main")
    public String main(){

        return "boardView/main";
    }

    // 공지 폼 이동
    @GetMapping("notice")
    public String notice(){
        return "boardView/notice";
    }



}
