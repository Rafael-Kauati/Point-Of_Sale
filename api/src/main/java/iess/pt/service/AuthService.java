package iess.pt.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import iess.pt.config.JWT.JwtService;
import iess.pt.dataAccess.userRepository;
import iess.pt.entity.Template.AuthenticationRequest;
import iess.pt.entity.Template.AuthenticationResponse;
import iess.pt.entity.Template.RegisterRequest;
import iess.pt.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Date;

import iess.pt.dataAccess.TokenRepository;
import iess.pt.entity.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final userRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User u) {
        var user = User.builder()
                .name(u.getName())
                .email(u.getEmail())
                .password(passwordEncoder.encode(u.getPassword()))
                .address(u.getAddress())
                .HiredIn(java.time.LocalDate.now())
                .phone(u.getPhone())
                .role(u.getRole())
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                //.refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address("someplace")
                .HiredIn(java.time.LocalDate.now())
                .phone("111222333")
                .role(request.getRole())
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                //.refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("\n ========================================================================\n");
        System.out.println("   =================== Starting authentication in login ====================");
        System.out.println("\n ========================================================================\n");
        System.out.println("Email : "+request.getEmail()+" Password :  "+request.getPassword());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail());
        System.out.println("\n =================== User fetched  : "+user.toString()+" ===================\n");
        var jwtToken = jwtService.generateToken(user);
        System.out.println("\n =================== Token generated : "+jwtToken+" ===================\n");
        var refreshToken = jwtService.generateRefreshToken(user);
        System.out.println("\n =================== Token generated (refresed) : "+refreshToken+" ===================\n");
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                //.refreshToken(refreshToken)
                .uid(user.getId())
                .role(user.getRole())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userEmail(user.getEmail())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getEmail());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
