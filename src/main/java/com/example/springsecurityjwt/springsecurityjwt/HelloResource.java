package com.example.springsecurityjwt.springsecurityjwt;

import com.example.springsecurityjwt.springsecurityjwt.model.AuthenticationRequest;
import com.example.springsecurityjwt.springsecurityjwt.model.AuthenticationResponse;
import com.example.springsecurityjwt.springsecurityjwt.services.MyUserDetailsService;
import com.example.springsecurityjwt.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @RequestMapping({"/hello"})
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);

        }
  final UserDetails userDetails = userDetailsService
          .loadUserByUsername(authenticationRequest.getUserName());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return  ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
