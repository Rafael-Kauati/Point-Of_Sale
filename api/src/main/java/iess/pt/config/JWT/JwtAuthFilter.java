package iess.pt.config.JWT;

import iess.pt.dataAccess.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter
{
    @Qualifier("uniqueJwtService")
    private  JwtService service;
    private final UserDetailsService userDetailsService;
    private final TokenRepository repository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("\n\n ========= Servlet path : "+request.getServletPath()+"\n\n");
        if (request.getServletPath().startsWith("/auth/")) {
            System.out.println("\n ============================== request for /auth/  ============================== \n");
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getServletPath().startsWith("/update")) {
            System.out.println("\n ============================== request for /update/  ============================== \n");
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getServletPath().startsWith("/swagger")) {
            System.out.println("\n ============================== request for /swagger-ui/  ============================== \n");
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getServletPath().startsWith("/ws")) {
            System.out.println("\n ============================== request for /ws  ============================== \n");
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getServletPath().startsWith("/topic")) {
            System.out.println("\n ============================== request for /broadcast  ============================== \n");
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        /*
        if (request.getServletPath().startsWith("/api/") || request.getServletPath().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

         */
        System.out.println("\n ============================== request for /api/  ============================== \n");
        jwt = authHeader.substring(7);
        userEmail = service.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = repository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (service.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
