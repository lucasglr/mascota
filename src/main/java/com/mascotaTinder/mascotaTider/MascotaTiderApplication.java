package com.mascotaTinder.mascotaTider;

import com.mascotaTinder.mascotaTider.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MascotaTiderApplication {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    public static void main(String[] args) {
        SpringApplication.run(MascotaTiderApplication.class, args);
    }
    
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
//        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
//    }
}
