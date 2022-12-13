package com.morelos.mercado.web.controller;


import com.morelos.mercado.domain.JsonResponse;
import com.morelos.mercado.domain.dto.AuthenticationRequest;
import com.morelos.mercado.domain.dto.AuthenticationResponse;
import com.morelos.mercado.domain.service.MercadoUserDetailsService;
import com.morelos.mercado.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //Inyectado de dependencias
    @Autowired //Anotación que indica que spring inicializa esta instancia
    private AuthenticationManager authenticationManager;
    @Autowired
    private MercadoUserDetailsService mercadoUserDetailsService;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/authenticate")

    //public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request) {

    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest request) {
        JsonResponse json = new JsonResponse(false, new ArrayList<>(), "");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails user = mercadoUserDetailsService.loadUserByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(user);

            json.success = true;
            ArrayList<AuthenticationResponse> data = new ArrayList<AuthenticationResponse>();
            data.add(new AuthenticationResponse(jwt));
            json.data = data;

        } catch (BadCredentialsException e) {
            json.error = e.getMessage();

        }

        return new ResponseEntity<>(json, HttpStatus.OK);
    }




}
