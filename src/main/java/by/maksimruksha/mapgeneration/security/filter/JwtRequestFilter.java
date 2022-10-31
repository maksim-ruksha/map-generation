package by.maksimruksha.mapgeneration.security.filter;

import by.maksimruksha.mapgeneration.api.repository.UserRepository;
import by.maksimruksha.mapgeneration.entities.User;
import by.maksimruksha.mapgeneration.security.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String TOKEN_BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() == null) {
            String requestToken = request.getHeader(HEADER_AUTHORIZATION);
            if (requestToken != null) {
                if (requestToken.startsWith(TOKEN_BEARER_PREFIX)) {
                    try {
                        int index = requestToken.indexOf(TOKEN_BEARER_PREFIX) + TOKEN_BEARER_PREFIX.length();
                        String token = requestToken.substring(index);
                        String username = jwtService.getUserName(token);
                        if (username != null) {
                            if (userRepository.existsUserByName(username)) {
                                User user = userRepository.findUserByName(username);
                                UserDetails userDetails = user;
                                if (jwtService.isValid(token, userDetails)) {
                                    // we're good
                                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails == null ? List.of() : userDetails.getAuthorities());
                                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                    securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
