package com.shalsh.viajes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
    private final TokenAutenticator ta;

    public SecurityConfig(TokenAutenticator ta) {
        this.ta = ta;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(
                        		"/viajes/reporte/**"
                        )/*.hasAnyRole("ADMIN")*/.permitAll()
                        .anyRequest().permitAll()/*.hasAnyRole("USER", "ADMIN")*/
                        
                )
                .addFilterBefore(ta, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
