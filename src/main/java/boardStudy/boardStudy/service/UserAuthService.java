package boardStudy.boardStudy.service;

import boardStudy.boardStudy.domain.Role;
import boardStudy.boardStudy.domain.User;
import boardStudy.boardStudy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
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

    public String register(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return token;
    }

    public String authenticate(String username, String password){

        //인증 확인하기
        if (isAuthenticated(username, password)){
            //여기 왔다는 건 인증 통과했다는 뜻
            User user = userRepository.findByUsername(username).orElseThrow();
            String token = jwtService.generateToken(user);

            return token;
        }
        //여기까지 올 일이 없음.
        else {
            System.out.println("에러");
            return "error";
        }

    }

    public boolean isAuthenticated(String username, String password) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return authenticate.isAuthenticated();
    }


    public String getUsername(HttpServletRequest request){
        final String cookie = request.getHeader("Cookie");
        final String jwt;
        final String username;

        //token이 없다면 다음 필터로 그냥 감.
        if (cookie == null || !cookie.contains("jwtToken=")){
            return "cookie is Wrong";
        }

        //토큰 parse하기.
        jwt = cookie.substring(cookie.indexOf("jwtToken=") + 9);
        username = jwtService.extractUsername(jwt);
        return username;
    }
}
