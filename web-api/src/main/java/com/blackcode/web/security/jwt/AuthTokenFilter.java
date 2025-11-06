package com.blackcode.web.security.jwt;

import com.blackcode.common.dto.user.UserRes;
import com.blackcode.common.dto.user.UserTokenRes;
import com.blackcode.common.exception.InvalidJwtException;
import com.blackcode.common.exception.TokenExpiredException;
import com.blackcode.common.exception.UnauthorizedException;
import com.blackcode.user.service.JwtUtilsService;
import com.blackcode.user.service.UserDetailsServiceImpl;
import com.blackcode.user.service.UserService;
import com.blackcode.user.service.UserTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter  extends OncePerRequestFilter {

    private final JwtUtilsService jwtUtilsService;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserTokenService userTokenService;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    public AuthTokenFilter(JwtUtilsService jwtUtilsService, UserDetailsServiceImpl userDetailsService, UserTokenService userTokenService, UserService userService) {
        this.jwtUtilsService = jwtUtilsService;
        this.userDetailsService = userDetailsService;
        this.userTokenService = userTokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(request);
            if(jwt != null){
                jwtUtilsService.assertValidToken(jwt);

                String username = jwtUtilsService.getUserNameFromJwtToken(jwt);

                validateUserToken(username, jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        } catch (TokenExpiredException | InvalidJwtException | UnauthorizedException e) {
            logger.warn("JWT error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
            throw new UnauthorizedException("Authentication error", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/auth/");
    }

    private void validateUserToken(String username, String jwt) throws UnauthorizedException {
        UserRes userRes = userService.findByEmail(username);
        UserTokenRes userTokenRes = userTokenService.findByUserId(userRes.getId(), jwt);



        System.out.println("username : "+username);
        System.out.println("userRes : "+userRes.getId());
        System.out.println("userTokenRes : "+userTokenRes.getToken());
        System.out.println("Jwt : "+jwt);

        if (!userTokenRes.getToken().equals(jwt)) {
            throw new UnauthorizedException("Token has been invalidated");
        }

        if (!userTokenRes.getIsActive()) {
            throw new UnauthorizedException("Token is inactive or expired");
        }
    }
}
