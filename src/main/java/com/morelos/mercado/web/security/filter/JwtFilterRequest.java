package com.morelos.mercado.web.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morelos.mercado.domain.service.MercadoUserDetailsService;
import com.morelos.mercado.web.security.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component

public class JwtFilterRequest extends OncePerRequestFilter { // OncePerRequestFilter se ejecuta cada que se hace una petición
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private MercadoUserDetailsService mercadoUserDetailsService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("success", false);
        //path auth
        if (!request.getRequestURI().substring(request.getContextPath().length()).endsWith("authenticate")) {

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                String jwt = authorizationHeader.substring(7);
                String username = null;
                try {
                    username = jwtUtil.extractUsername(jwt);
                }catch (Exception e){
                    //System.out.println(e.getMessage());
                    errorDetails.put("error", "JWT expired");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                   // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    mapper.writeValue(response.getOutputStream (), errorDetails);
                    //mapper.writeValue(response.getWriter(), e.getMessage());
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = mercadoUserDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } else {


                errorDetails.put("error", "Invalid token");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
               // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getOutputStream(), errorDetails);
            }

        }


        filterChain.doFilter(request, response);
    }
}
