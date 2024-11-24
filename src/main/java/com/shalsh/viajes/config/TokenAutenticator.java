package com.shalsh.viajes.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAutenticator extends OncePerRequestFilter {

	@Autowired
	private RestTemplate rt;
	@Value("localhost:8090/auth/validatetoken")
	private String authUrl;
	
	public TokenAutenticator() {
		
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (token != null && token.startsWith("Bearer ")) {
			HttpHeaders header = new HttpHeaders();
			header.set(HttpHeaders.AUTHORIZATION, token);
			HttpEntity<String> req = new HttpEntity<>(header);
			ResponseEntity<String> validation = rt.exchange(authUrl, HttpMethod.POST, req, String.class);
			
			if(validation.getStatusCode().equals(HttpStatus.OK)) {
				Map<String,String> body = new ObjectMapper().readValue(validation.getBody(),new TypeReference<Map<String,String>>() {});
				try {
					String rol = "ROLE_" + body.get("role").toUpperCase();
					Authentication auth = new AuthHandler(List.of(new SimpleGrantedAuthority(rol)),token);
					SecurityContextHolder.getContext().setAuthentication(auth);
				} catch (Exception e) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
			}
			else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
				
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
