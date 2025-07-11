package com.players.LoginHome_service.SecurityConfig;

import com.players.LoginHome_service.Service.AuthenticationService.UserService;
import com.players.LoginHome_service.Util.jwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
/*To authenticate users on every request by:
   Extracting the JWT from the request.
   Validating the token.
   Setting the authenticated user in the Spring Security context if the token is valid.*/
/*ensures this filter runs only once per request.
This is ideal for JWT because we don’t want to repeatedly process the same request in a chain.
*/
public class JwtAuthenticationFilter extends OncePerRequestFilter {
        @Autowired
        private final jwtUtil jwtUtil;

        @Autowired
        private final UserService userDetailsService;


        /*This is the main method that intercepts and processes each HTTP request*/
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            final String authHeader = request.getHeader("Authorization");
            final String jwtToken;
            final String username;
       /*Check if Header is Present and Starts with “Bearer ”*/
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("JWT token is intercepted");
                filterChain.doFilter(request, response);
                return;
            }

            jwtToken = authHeader.substring(7);
            username = jwtUtil.getUsernameFromToken(jwtToken);
            /*Check if User is Already Authenticated*/
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.userDetailsService().loadUserByUsername(username);

                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               /*If token is valid, set the authenticated user into Spring's SecurityContext.
               From now on, Spring Security treats this request as authenticated*/
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            /*Pass the request to the next filter or the actual controller*/
            filterChain.doFilter(request, response);
        }
    }







