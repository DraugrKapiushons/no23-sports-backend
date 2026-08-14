package org.no23sports.faqservice.config;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtVerifier jwtVerifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/faq/swagger-ui") ||
            path.startsWith("/faq/v3/api-docs") ||
            path.startsWith("/faq/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        // FAQ content (spec section 13, "Sık Sorulan Sorular") is public
        // site content - anyone browsing the site should be able to read
        // it without logging in. Only writes (create/publish/delete, all
        // admin panel actions) require a bearer token.
        if ("GET".equalsIgnoreCase(request.getMethod()) && path.startsWith("/faq")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtVerifier.verifyAndExtract(token);
            request.setAttribute("email", jwtVerifier.extractEmail(claims));
            request.setAttribute("role", jwtVerifier.extractRole(claims));
            request.setAttribute("nameSurname", jwtVerifier.extractNameSurname(claims));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
