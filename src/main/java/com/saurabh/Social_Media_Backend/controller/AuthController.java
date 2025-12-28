package com.saurabh.Social_Media_Backend.controller;

import com.saurabh.Social_Media_Backend.dto.LoginResponse;
import com.saurabh.Social_Media_Backend.models.Users;
import com.saurabh.Social_Media_Backend.repo.LoginUserDto;
import com.saurabh.Social_Media_Backend.repo.RegisterUserDto;
import com.saurabh.Social_Media_Backend.service.AuthService;
import com.saurabh.Social_Media_Backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;

    @Autowired
    public AuthController(JwtService jwtService,AuthService authService){
        this.authService=authService;
        this.jwtService=jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Users> register(@RequestBody RegisterUserDto registerUserDto){
        Users registerUser=authService.signup(registerUserDto);
        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        Users authenticated=authService.authenticate(loginUserDto);
        String jwtToken=jwtService.generateToken(authenticated);

        LoginResponse response=new LoginResponse();

        response.setToken(jwtToken);
        response.setExpirationDate(jwtService.getExpiration());

        ResponseCookie cookie=ResponseCookie.from("accessToken",jwtToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60*60)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body(response);
    }

}
