package boardStudy.boardStudy.configuration;

import boardStudy.boardStudy.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;//interface를 쓸 수도 있지만 우리는 우리가 custom하게 만든 db에서 가져와서 비교할 것이기 때문에 주입받는다.
    /*
    request가 올 때마다 가로채서 해당하는 request와 response를 관여할 수 있다.
    왜?
    OncePerRequestFilter는 스프링이 요청이 올 때마다 그 요청들을 Filter에서 거르는 역할을 하기 때문이다.
    해당 클래스를 extends해서 우리는 그 filter를 세부 구현할 수 있다.

    Parameter에 있는 filterChain은 이 filter 다음으로 실행될 Filter들의 묶음 같은 느낌이다.
    우리가 세부 구현한 내용이 끝나고 filterChain.doFilter()를 통해 스프링이 내부 구현한 다른 필터들이 다시 시작하도록 할 수 있다.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        //Authorization 헤더에서 가져오기
        final String cookie = request.getHeader("Cookie");
        final String jwt;
        final String username;

        //token이 없다면 다음 필터로 그냥 감.
        if (cookie == null || !cookie.contains("jwtToken=")){
            filterChain.doFilter(request, response);
            return;
        }

        //토큰 parse하기.
        jwt = cookie.substring(cookie.indexOf("jwtToken=") + 9);
        username = jwtService.extractUsername(jwt);

        //
        //SecurityContextHolder를 통해 호출하는 것은 user가 아직 Authenticated했는지를 파악하는 것.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );//Spring과 securityContextHolder가 필요로 하는 것.

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //securityContextHolder 업데이트하기
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);


    }
}
