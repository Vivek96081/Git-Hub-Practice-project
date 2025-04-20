package com.app.config;
import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.service.JWTservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

//If any HttpRequest come with token then it automatically come in the JWTFilter class. in requests part.
@Component
public class JWTFilter extends OncePerRequestFilter{

    private JWTservice jwTservice;
    private UserRepository userRepository;

    public JWTFilter(JWTservice jwTservice, UserRepository userRepository) {
        this.jwTservice = jwTservice;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            String jwttoken = token.substring(8, token.length()-1);
            String username = jwTservice.getUsername(jwttoken);
            Optional<User> byUsername = userRepository.findByUsername(username);
            if(byUsername.isPresent()){
                User user = byUsername.get();
                //jwt token is valid then after i got the user details then after i found details in the
                // database like username
                // password and go to spring security and tell  for this user and password set in this Constent
                // setting and after setting grant me thr permission to access this particular ID.
                // using UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                      //here user and password is present in the user
                        user,
                        null,
                        null
                );
                // here user information is stored and this user is uses which url all these things are
                // present in the authentication token url.
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                // after setting authentication token then i will use this authentication token in spring security.
                // send to the spring security context.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        //It helps us to redirect to the login methid
        filterChain.doFilter(request,response);

           }
}
