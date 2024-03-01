package com.hyunrian.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequest) -> authorizeHttpRequest
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            .csrf((csrf) -> csrf //h2
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
            .headers((headers) -> headers //h2
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            .formLogin((formLogin) -> formLogin //로그인 설정
                .loginPage("/login")
                .defaultSuccessUrl("/music/start")
                .successHandler(authenticationSuccessHandler))
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)) //로그아웃 시 생성되어 있던 세션 삭제
        ;

        return http.build();
    }

    /**
     * 비밀번호 암호화
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security의 인증 처리(내부적으로 인증과 권한 부여 프로세스 처리)
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
