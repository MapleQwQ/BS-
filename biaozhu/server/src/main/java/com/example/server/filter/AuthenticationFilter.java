package com.example.server.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.server.entity.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.example.server.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.server.security.SecurityConstants.SECRET;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        //设置默认登录地址
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        return super.attemptAuthentication(request, response);
        try {
//            System.out.println("=================attemptAuthentication========================");
            AppUser appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            appUser.getUserName(),
                            appUser.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            System.out.println("Error in attemptAuthentication");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
        String token = JWT.create().withSubject(((User)authResult.getPrincipal()).getUsername())
                .withExpiresAt((new Date(System.currentTimeMillis()+EXPIRATION_TIME)))
                .sign(Algorithm.HMAC256(SECRET));
        System.out.println(token);
        String body = ((User)authResult.getPrincipal()).getUsername() + " "  + token;
        response.getWriter().write(body);
        response.getWriter().flush();
        response.addHeader("token",token);
//        Date exp = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
//        Key key = Keys.hmacShaKeyFor(KEY.getBytes());
//        Claims claims = Jwts.claims().setSubject(((User) authResult.getPrincipal()).getUsername());
//        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, key).setExpiration(exp).compact();
//        response.addHeader("token", token);
    }
}