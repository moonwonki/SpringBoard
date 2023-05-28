package boardStudy.boardStudy.controller;

import boardStudy.boardStudy.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ContributorController {

    private final UserAuthService userAuthService;

    @Autowired
    public ContributorController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping("/contributor")
    public String contributor(@CookieValue("jwtToken") String cookie){
        log.info("기여자 페이지 접속 : {}", userAuthService.getUsername(cookie));
        return "/html/contributor";
    }
}
