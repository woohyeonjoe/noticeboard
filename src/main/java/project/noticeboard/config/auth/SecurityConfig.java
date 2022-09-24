package project.noticeboard.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import project.noticeboard.domain.user.Role;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity      //Spring Security 설정 활성화
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()     //h2-console 화면을 사용하기 위해 해당 옵션 disable 처리
                .and()
                .authorizeHttpRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()      //antMatchers(): 열람 권한 부여
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()       //anyRequest(): 설정된 값들 이외 나머지 URL, authenticated(): 나머지 URL들은 모두 인증된 사용자들에게만 허용
                .and()
                .logout()
                .logoutSuccessUrl("/")  //로그아웃 성공시 "/"로 이동
                .and()
                .oauth2Login()      //OAuth2 로그인 기능 설정을위한 진입점
                .userInfoEndpoint() //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
                .userService(customOAuth2UserService);  //로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록, 소셜 서비스에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시

        return http.build();
    }

    /**
     * 최신 코드 - SecurityFilterChain 작성 방식 변경 (람다식 방식)
     * 위 코드도 무리없이 돌아는 감
     */
//    public SecurityFilterChain newFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .headers().frameOptions().disable()
//                .and()
//                .authorizeHttpRequests(authorize -> authorize
//                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
//                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
//                        .anyRequest().authenticated())
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/"))
//                .oauth2Login(oauth2Login -> oauth2Login
//                        .userInfoEndpoint()
//                        .userService(customOAuth2UserService));
//
//        return http.build();
//    }
}
