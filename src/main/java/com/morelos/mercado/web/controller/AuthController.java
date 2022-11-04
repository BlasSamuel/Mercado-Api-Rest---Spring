package com.morelos.mercado.web.controller;


import com.morelos.mercado.domain.dto.AuthenticationRequest;
import com.morelos.mercado.domain.dto.AuthenticationResponse;
import com.morelos.mercado.domain.service.MercadoUserDetailsService;
import com.morelos.mercado.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails user = mercadoUserDetailsService.loadUserByUsername(request.getUsername());
            //System.out.println(user);
            //user.map(user -> {new ResponseEntity<>(user, HttpStatus.OK);}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            String jwt = jwtUtil.generateToken(user);
            return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);


        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

/*
* public ResponseEntity<Product> getProduct(
            // @Apiparam() anotación para identificador que recibe la petición, es obligatorio
            @ApiParam(value = "El id del producto", required = true, example = "7") @PathVariable("id") int productId) {
        return productService.getProduct(productId).map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
* */


}
