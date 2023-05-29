package boardStudy.boardStudy.service;

import boardStudy.boardStudy.domain.Role;
import boardStudy.boardStudy.domain.User;
import boardStudy.boardStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Map<String, Boolean> register(String username, String email, String password, String nickname) {
        Map<String, Boolean> registerValid = new HashMap<>();

        if (userRepository.existsUserByUsername(username)) registerValid.put("username", false);
        else registerValid.put("username", true);
        if (userRepository.existsUserByEmail(email)) registerValid.put("email", false);
        else registerValid.put("email", true);
        if (userRepository.existsUserByNickname(nickname)) registerValid.put("nickname", false);
        else registerValid.put("nickname", true);

        if (registerValid.get("username") && registerValid.get("email") && registerValid.get("nickname")){
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.USER);
            user.setNickname(nickname);
            userRepository.save(user);
        }

        return registerValid;
    }


    public boolean isAuthenticated(String username, String password) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return authenticate.isAuthenticated();
    }


    public String getUsername(String cookieExtractedJwt){
        //String 자체가 jwt 토큰임.
        //cookie 가져오기
        if (cookieExtractedJwt == null){
            return "cookie is Wrong";
        }

        //토큰 parse하기.
        String username = jwtService.extractUsername(cookieExtractedJwt);
        return username;
    }

    public String getNicknameByUsername(String username){
        return userRepository.findByUsername(username).get().getNickname();
    }

    public Long getIdByUsername(String username){
        return userRepository.findByUsername(username).get().getId();
    }
}
