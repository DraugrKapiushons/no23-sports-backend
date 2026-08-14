package org.no23sports.kitchenmenuservice.config;

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

        if (path.startsWith("/kitchen/menu/swagger-ui") ||
            path.startsWith("/kitchen/menu/v3/api-docs") ||
            path.startsWith("/kitchen/menu/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Kitchen menu (spec section 5, "NO23 Kitchen" / "Menü Sistemi") is
        // public catalogue content - visitors filter by High Protein / Low
        // Calorie / etc. without logging in. Only writes require a token.
        if ("GET".equalsIgnoreCase(request.getMethod()) && path.startsWith("/kitchen/menu")) {
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
