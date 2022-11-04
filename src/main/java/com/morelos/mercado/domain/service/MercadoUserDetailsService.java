package com.morelos.mercado.domain.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MercadoUserDetailsService  implements UserDetailsService {
   /* @Autowired
    UserRepository userRepository;



    public Optional<Usuario> loadUserByUsername(String username, String password) throws UsernameNotFoundException {
        //{noop} permite agregar la contraseña sin estar encriptada
       // return new User("admin", "{noop}Morelos.2022", new ArrayList<>());
        return userRepository.findByUsernameAndPassword(username, password);
       /* public Optional<Product> getProduct(int productId){
            return productRepository.getProduct(productId);
        }*/
    //}*/
    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //{noop} permite agregar la contraseña sin estar encriptada
        return new User("admin", "{noop}Morelos.2022", new ArrayList<>());

    }
}
