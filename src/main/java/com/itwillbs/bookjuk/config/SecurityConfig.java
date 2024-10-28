package com.itwillbs.bookjuk.config;


import com.itwillbs.bookjuk.security.CustomOAuth2User;
import com.itwillbs.bookjuk.security.CustomUserDetails;
import com.itwillbs.bookjuk.security.SessionSyncFilter;
import com.itwillbs.bookjuk.service.login.CustomOAuth2UserService;
import com.itwillbs.bookjuk.service.login.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final SessionSyncFilter sessionSyncFilter;

    //BCrypt 암호화 설정
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //MIME type 에러
    //정적리소스는 시큐리트 접근권한에서 제외한다.
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(sessionSyncFilter, UsernamePasswordAuthenticationFilter.class);

        //접근 권한에 대한 설정 부분
        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/**").permitAll()
                .requestMatchers("/", "/join", "/login","/oauth2/**", "/checkData",
                        "/loginCheck", "/actuator/**", "/logs/**", "/find",
                        "/sendSmsCodeTest", "/codeValidate", "/verifySmsCode").permitAll()
                .requestMatchers("/phone").hasAnyRole("INACTIVE") //소셜로그인 회원은 전화번호 입력하지않으면 계속해서 전화번호 입력창으로 리다이렉트!
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/test").hasAnyRole("ADMIN", "USER") //여기에 로그인 된사람만 할수있는 페이지 추가
                .anyRequest().authenticated()
        );

        //권한이 없는 페이지에 접근할 때 설정
        http.exceptionHandling((auth) -> auth
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    //사용자가 권한이 없을 때 main 페이지로 리다이렉트
                    response.sendRedirect("/");
                })
        );

        //로그인 페이지 관련 설정
        http.formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginCheck") //로그인 체크 시 시큐리티가 자동으로 사용자 인증을 처리하는 경로
                .usernameParameter("userId") //userName 으로 인식하기 때문에 id 의 파라미터 값을 userId 로 변경
                .passwordParameter("userPassword") //password 으로 인식하기 때문에 password 의 파라미터 값을 userPassword 로 변경
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    // 로그인 성공 시 AJAX 요청에 대한 응답 처리
                    HttpSession session = request.getSession();
                    // 사용자 PK 를 세션에 저장
                    session.setAttribute("userNum", ((CustomUserDetails) authentication.getPrincipal()).getUserNum()); // 사용자 ID
                    // 사용자 롤을 세션에 저장
                    String role = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst() // 첫 번째 롤만 가져옵니다. (여러 롤이 있을 수 있음)
                            .orElse(null);
                    session.setAttribute("role", role); // 사용자 롤

                    // 로그인 성공 시 AJAX 요청에 대한 응답 처리
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                .failureHandler((request, response, exception) -> {
                    // 로그인 실패 시 AJAX 요청에 대한 응답 처리
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                })
        );

        //OAuth2 login
        http.oauth2Login((oauth2) -> oauth2
                .loginPage("/oauth2")
                .successHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();

                    // 사용자 고유 pk 를 세션에 저장
                    Long userNum = ((CustomOAuth2User) authentication.getPrincipal()).getUserNum();
                    session.setAttribute("userNum", userNum);

                    // 사용자 권한(롤)을 세션에 저장
                    String role = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst() // 첫 번째 권한만 가져오기
                            .orElse(null);
                    session.setAttribute("role", role);
                    // 로그로 세션 값 출력
                    log.info("UserNum: {}", userNum);
                    log.info("Role: {}", role);

                    //사용자 권한에 따라 리다이렉트 주소 변경
                    if ("ROLE_INACTIVE".equals(role)) {
                        response.sendRedirect("/phone");
                    } else {
                        response.sendRedirect("/");
                    }
                })
                .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(customOAuth2UserService))
                )
        );

        //자동로그인(Remember-me)설정
        http.rememberMe((auth) -> auth
                .key("BookJukBookJuk_rememberKey") //암호화 키
                .rememberMeParameter("rememberMe") //form 에서 받아올 파라미터 명
                .tokenValiditySeconds(86400) //쿠키 유효기간 (1일)
                .userDetailsService(customUserDetailsService)
                .useSecureCookie(false)  // HTTPS가 아닌 경우 false 설정
                .rememberMeCookieDomain("localhost")  // 도메인 설정
                .authenticationSuccessHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    session.setAttribute("userNum", ((CustomUserDetails) authentication.getPrincipal()).getUserNum());
                    // 사용자 권한(롤)을 세션에 저장
                    String role = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst() // 첫 번째 권한만 가져오기
                            .orElse(null);

                    response.sendRedirect("/");
                })
        );



        //로그아웃 설정
        http.logout((auth) -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .clearAuthentication(true)
                .permitAll()
        );

        //csrf 토큰 관련 설정(지금은 disable 로 비활성화
        http.csrf((auth) -> auth.disable());

        //다중로그인 설정
        http.sessionManagement((auth) -> auth
                .maximumSessions(1) //하나의 아이디에 대한 다중 로그인 허용 개수
                .maxSessionsPreventsLogin(true)
                //다중 로그인 개수를 초과하였을 경우 처리 방법
                //true -> 초과시 새로운 로그인 차단
                //false -> 초과시 기존 세션 하나 삭제
        );

        return http.build();
    }
}
