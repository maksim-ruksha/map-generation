package by.maksimruksha.mapgeneration.security.filter;

import by.maksimruksha.mapgeneration.api.repository.UserRepository;
import by.maksimruksha.mapgeneration.api.service.UserService;
import by.maksimruksha.mapgeneration.dto.UserDto;
import by.maksimruksha.mapgeneration.entities.User;
import by.maksimruksha.mapgeneration.security.UserDetails;
import by.maksimruksha.mapgeneration.security.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String TOKEN_BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    //private final UserService userService;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() == null) {
            String requestToken = request.getHeader(HEADER_AUTHORIZATION);
            if (requestToken != null) {
                if (requestToken.startsWith(TOKEN_BEARER_PREFIX)) {
                    int index = requestToken.indexOf(TOKEN_BEARER_PREFIX);
                    String token = requestToken.substring(index);
                    String username = jwtService.getUserName(token);
                    if (username != null) {
                        if(userRepository.existsUserByName(username)) {
                            User user = userRepository.findUserByName(username);
                            UserDetails userDetails = mapper.map(user, UserDetails.class);
                            if (jwtService.isValid(token, userDetails)) {
                                // we're good
                                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
                                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                            }
                        }
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
