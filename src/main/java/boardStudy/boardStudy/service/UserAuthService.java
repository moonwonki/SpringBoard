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

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(String username, String email, String password, String nickname) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setNickname(nickname);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return token;
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
}
