package org.no23sports.paymentservice.config;

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
        if (path.startsWith("/payments/swagger-ui") ||
            path.startsWith("/payments/v3/api-docs") ||
            path.startsWith("/payments/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Iyzico posts the checkout-form result back to this endpoint as a
        // server-to-server / browser redirect with no Authorization header -
        // it can't attach our JWT. The endpoint is safe to leave open because
        // it only accepts a `token`, which is re-verified against Iyzico's
        // own API (via CheckoutForm.create) before anything is persisted.
        if (path.equals("/payments/callback")) {
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
