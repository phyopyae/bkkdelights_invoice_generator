package com.psychojunior.invoice.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.psychojunior.invoice.template.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @SuppressWarnings({"removal"})
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/login", "/register", "/h2-console/**", "/css/**", "/images/**").permitAll()
                .anyRequest()
                .authenticated())
                .formLogin(login -> login.loginPage("/login").permitAll().defaultSuccessUrl("/", true))
                .logout(logout -> logout.permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login?invalid-session=true")
                        .maximumSessions(1)
                        .expiredUrl("/login?session-expired=true"));

		http.headers(headers -> headers.frameOptions().sameOrigin());

		return http.build();
	}

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder.userDetailsService(userService)
        .passwordEncoder(passwordEncoder);
        
        return authenticationManagerBuilder.build();
    }
}
