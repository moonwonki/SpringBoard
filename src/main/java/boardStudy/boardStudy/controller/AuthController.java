package boardStudy.boardStudy.controller;

import boardStudy.boardStudy.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Autowired
    private final UserAuthService userAuthService;


    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("nickname") String nickname){
        log.info("회원가입 -> 사용자명 : {}", username);
        userAuthService.register(username, email, password, nickname);

        return "html/loginForm";
    }




    @GetMapping("/loginPage")
    public String getAuthenticatePage(){
        return "html/loginForm";
    }

    @GetMapping("/register")
    public String getRegisterPage(){
        return "html/registerForm";
    }

}


