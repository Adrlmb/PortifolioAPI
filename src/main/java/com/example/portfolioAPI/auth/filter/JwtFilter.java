package com.example.portfolioAPI.auth.filter;

import com.example.portfolioAPI.auth.jwt.JwtService;
import com.example.portfolioAPI.users.entity.UserEntity;
import com.example.portfolioAPI.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        System.out.println(">>> Entrou no JwtFilter");

        String header = request.getHeader("Authorization");
        System.out.println("Header recebido: " + header);

        String email = null;

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                email = jwtService.getEmailFromToken(token);
            } catch (Exception e) {
                System.out.println("Erro ao extrair email do token: " + e.getMessage());
            }
        }

        if (email != null) {
            UserEntity user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // sempre chamar chain.doFilter!
        chain.doFilter(request, response);
    }
}
