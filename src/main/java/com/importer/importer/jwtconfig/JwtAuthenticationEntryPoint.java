package com.importer.importer.jwtconfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		out.print("{\"message\":\"Wrong JWT token\"}");
		out.flush();
	}

}