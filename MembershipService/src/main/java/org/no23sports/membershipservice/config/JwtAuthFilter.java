package org.no23sports.membershipservice.config;

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

        if (path.startsWith("/membership/swagger-ui") ||
            path.startsWith("/membership/v3/api-docs") ||
            path.startsWith("/membership/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Membership packages (spec section 4, "Üyelik Paketleri" - START/PLUS/PRO/ELITE)
        // are public pricing content. User-specific memberships and all writes
        // require authentication.
        if ("GET".equalsIgnoreCase(request.getMethod()) && path.startsWith("/membership/packages")) {
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
