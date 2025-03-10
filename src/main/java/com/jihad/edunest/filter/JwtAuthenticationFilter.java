    package com.jihad.edunest.filter;

    import com.jihad.edunest.service.implimentations.CustomUserDetailsService;
    import com.jihad.edunest.service.implimentations.JwtService;
    import io.jsonwebtoken.ExpiredJwtException;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;

    @Component
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtUtil;
        private final CustomUserDetailsService userDetailsService;

        public JwtAuthenticationFilter(JwtService jwtUtil, CustomUserDetailsService userDetailsService) {
            this.jwtUtil = jwtUtil;
            this.userDetailsService = userDetailsService;
        }
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
            try {
                String authHeader = request.getHeader("Authorization");
                String refreshHeader = request.getHeader("X-Refresh-Token");

                if (refreshHeader != null) {
                    handleRefreshToken(refreshHeader, response);
                    return;
                }

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    String email = jwtUtil.extractEmail(token);

                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                        if (!jwtUtil.isTokenExpired(token) && jwtUtil.isTokenValid(token, userDetails.getUsername())) {
                            var authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        } else if (jwtUtil.isTokenExpired(token)) {
                            throw new ExpiredJwtException(null, null, "Access token has expired");
                        }
                    }
                }
            } catch (ExpiredJwtException e) {
                // Handle expired access token
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Access token expired\"}");
                response.getWriter().flush();
                return;
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}", e);
            }

            chain.doFilter(request, response);
        }

        private void handleRefreshToken(String refreshToken, HttpServletResponse response) throws IOException {
            try {
                String email = jwtUtil.extractEmail(refreshToken);
                if (jwtUtil.isTokenValid(refreshToken, email)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    // Generate new tokens and send them in response
                    String newAccessToken = jwtUtil.generateToken(email);
                    String newRefreshToken = jwtUtil.generateRefreshToken(email);

                    // Mettre à jour le contexte de sécurité pour cette requête
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Envoyer les tokens dans la réponse
                    response.setContentType("application/json");
                    response.getWriter().write("{\"accessToken\": \"" + newAccessToken +
                            "\", \"refreshToken\": \"" + newRefreshToken + "\"}");
                    response.getWriter().flush();
                } else {
                    throw new ExpiredJwtException(null, null, "Refresh token is invalid or expired");
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Refresh token expired\"}");
                response.getWriter().flush();
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid refresh token\"}");
                response.getWriter().flush();
            }
        }
    }
