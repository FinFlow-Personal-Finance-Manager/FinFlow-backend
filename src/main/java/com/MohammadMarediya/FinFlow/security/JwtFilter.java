package com.MohammadMarediya.FinFlow.security;

import com.MohammadMarediya.FinFlow.Constant.ApiEndpoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extract Token from Header
        String header = request.getHeader(AUTH_HEADER);
        String token = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            token = header.substring(TOKEN_PREFIX.length());
            log.info("JWT Token extracted from header");
        } else {
            log.info("Authorization header is missing or does not start with Bearer");
        }

        // 2. Extract Email from Token
        String email = null;
        if (token != null && !token.isEmpty()) {
            try {
                email = jwtUtil.extractEmail(token);
                log.info("Email extracted from JWT: {}", email);
            } catch (Exception e) {
                log.warn("Failed to extract email from token: {}", e.getMessage());
            }
        }

        // 3. Authenticate User if Email exists
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                if (jwtUtil.verifyToken(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("JWT authentication successful for user: {}", email);
                } else {
                    log.warn("JWT verification failed for user: {}", email);
                }
            } catch (Exception e) {
                log.error("Error during authentication process for user {}: {}", email, e.getMessage());
            }
        } else {
            if (email == null) {
                log.info("No email found in token; skipping authentication");
            } else {
                log.info("SecurityContext already contains authentication for user: {}", email);
            }
        }

        // 4. Continue Filter Chain
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith(ApiEndpoint.AUTH_ENDPOINT + ApiEndpoint.REGISTER_ENDPOINT) ||
                path.startsWith(ApiEndpoint.AUTH_ENDPOINT + ApiEndpoint.LOGIN_ENDPOINT);
    }

}
