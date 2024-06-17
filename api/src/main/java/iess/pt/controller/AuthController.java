package iess.pt.controller;

import iess.pt.entity.Template.AuthenticationResponse;
import iess.pt.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import iess.pt.entity.Template.*;

import java.io.IOException;

@Controller
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{

    @Autowired
    private final AuthService service;

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
            )
    {
        logRequestDetails("POST /auth/signin");
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    )
    {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    private void logRequestDetails(String endpoint) {
        // You can use your preferred logging mechanism, e.g., slf4j
        System.out.println("Received request for endpoint: " + endpoint);
    }

}
