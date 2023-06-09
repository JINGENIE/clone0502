package com.kbstar.controller;

import com.kbstar.dto.Adm;

import com.kbstar.service.AdmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    AdmService admService;
    @RequestMapping("/")
    public String main() {
        return "index";
    }

    @RequestMapping("/charts")
    public String charts(Model model){
        model.addAttribute("center", "charts");
        return "index";
    }
    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("center", "login");
        return "index";
    }
    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("center", "register");
        return "index";
    }
    @RequestMapping("/livechart")
    public String livechart(Model model){
        model.addAttribute("center", "livechart");
        return "index";
    }
    @RequestMapping("/registerimpl")
    public String registerimpl(Model model,
                               Adm adm, HttpSession session) throws Exception {
        try {
            adm.setPwd(encoder.encode(adm.getPwd()));
            admService.register(adm);
            session.setAttribute("loginadm",adm);
        } catch (Exception e) {
            throw new Exception("register error");
        }
        model.addAttribute("radm", adm);
        model.addAttribute("center","registerok");
        return "index";
    }
    @RequestMapping("/loginimpl")
    public String loginimpl(Model model, String id, String pwd, HttpSession session) throws Exception {

        Adm adm = null;
        String nextPage = "loginfailed";
        try {
            adm = admService.get(id);
            if(adm != null && encoder.matches(pwd, adm.getPwd())){
                nextPage= "loginok";
                session.setMaxInactiveInterval(100000);
                session.setAttribute("loginadm", adm);
                //loginadm라는 이름으로 adm를 넣어준다.
            }
        } catch (Exception e) {
            throw new Exception("시스템 장애. 잠시 후 다시 로그인 하세여");
        }
        model.addAttribute("center",nextPage);
        return "index";
    }

}