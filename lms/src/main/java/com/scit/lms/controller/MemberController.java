package com.scit.lms.controller;

import com.scit.lms.domain.Notice;
import com.scit.lms.util.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import com.scit.lms.domain.Member;
import com.scit.lms.service.MemberService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

//회원정보 관련 콘트롤러

@Slf4j
@Controller
@RequestMapping("member")
public class MemberController {

    @Autowired
    MemberService service;

    @Value("${member.servlet.multipart.location}")
    String uploadPath;

    //회원가입 폼으로 이동
    @GetMapping("join")
    public String join() {

        return "memberView/join";
    }


    //회원가입 처리
    @PostMapping("join")
    public String join(Member member) {
        log.debug("회원1:{}", member);
        int n = service.join(member);
        log.debug("회원2:{}", member);
        return "redirect:/";
    }

    //id중복확인 폼
    @GetMapping("idcheck")
    public String idcheck() {
        return "memberView/idcheck";
    }

    //id중복 확인 처리
    @PostMapping("idcheck")
    public String idcheck(String searchid, Model model) {
        log.debug("중복체크1:{}", searchid);
        boolean result = service.idcheck(searchid);
        log.debug("중복체크2:{}", searchid);
        model.addAttribute("searchid", searchid);
        model.addAttribute("result", result);

        return "memberView/idcheck";
    }


    //로그인 폼으로 이동
    @GetMapping("login")
    public String login() {
        return "memberView/login";

    }
    //회원정보 불러오기
    @GetMapping("information")
    public String information(@AuthenticationPrincipal UserDetails user, Model model){
        //로그인한 아이디로 회원정보 검색
        Member member = service.memberInfor(user.getUsername());


        //검색결과 모델에 저장
        model.addAttribute("user", member);
        log.debug("{}", member);
        return "memberView/memberInfo";
    }

    //회원정보 수정폼으로 이동
    @GetMapping("updateForm")
    public String updateForm(@AuthenticationPrincipal UserDetails user, Model model){
        Member member = service.memberInfor(user.getUsername());


        //검색결과 모델에 저장
        model.addAttribute("user", member);
        log.debug("** param :{}", member);
        return "memberView/updateForm";
    }

    @PostMapping("memberUpdate")
    public String memberUpdate(@AuthenticationPrincipal UserDetails user, Model model, Member member
            , MultipartFile upload){


        int n = service.memberUpdate(member);
        log.debug("11111111{}", member);
        Member m = service.memberInfor(user.getUsername());

        //검색결과 모델에 저장
        model.addAttribute("user", m);



//멤버사진 업데이트



        log.debug("dddddddddddddddddddddddddddddddddddddddd");
        log.debug("멀티파트파일:{}", upload);
        log.debug("멤버:{}", member);
        log.debug("이름이름이름{}", member.getMembername());

        if(member.getMemberphoto() != null && !member.getMemberphoto().isEmpty() && upload != null && !upload.isEmpty()) {
            String fullPath = uploadPath + "/" + member.getMemberphoto();
            FileService.deleteFile(fullPath);
        }

        if (upload != null && !upload.isEmpty()) {
            String savedfile=FileService.saveFile(upload, uploadPath);
            member.setMemberphoto(upload.getOriginalFilename());
            member.setMemberphoto(savedfile);

        }

        log.debug("업로드", upload);
        log.debug("업로드패스", uploadPath);
        member.setMemberid(user.getUsername());
        log.debug("멤버2:{}", member);

        service.memberphoto(member);
        model.addAttribute("user", member);


        return "memberView/memberInfo";
    }

    //비밀번호 확인
    @GetMapping("changePassword")
    public String checkPassword(){
        return "memberView/changePassword";
    }

    //비밀번호 변경
    @PostMapping("changePassword")
    public String changePassword(){
        return "memberView/memberInfo";
    }



    //사진변경폼으로 이동

    @GetMapping("insertPhoto")
    public String insertPhoto() {
        return "memberView/insertPhoto";
    }

    //사진 변경하기
    @PostMapping("updatePhoto")
    public String update(Member member, @AuthenticationPrincipal UserDetails user
            , MultipartFile upload, Model model) {

        log.debug("dddddddddddddddddddddddddddddddddddddddd");
        log.debug("멀티파트파일:{}", upload);
        log.debug("멤버:{}", member);
        log.debug("이름이름이름{}", member.getMembername());

        if(member.getMemberphoto() != null && !member.getMemberphoto().isEmpty() && upload != null && !upload.isEmpty()) {
            String fullPath = uploadPath + "/" + member.getMemberphoto();
            FileService.deleteFile(fullPath);
        }

        if (upload != null && !upload.isEmpty()) {
            String savedfile=FileService.saveFile(upload, uploadPath);
            member.setMemberphoto(savedfile);
            member.setPhotopath(uploadPath);


        }


        member.setMemberid(user.getUsername());
        log.debug("멤버2:{}", member);

        log.debug("업로드: {}", upload);
        log.debug("업로드패스: {}", uploadPath);

        service.memberphoto(member);
        model.addAttribute("user", member);


        Member m = service.memberInfor(user.getUsername());
        //검색결과 모델에 저장
        model.addAttribute("user", m);

        return "memberView/memberInfo";
    }



}
