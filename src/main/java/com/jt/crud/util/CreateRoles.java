package com.jt.crud.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jt.crud.security.entity.Rol;
import com.jt.crud.security.enums.RolNombre;
import com.jt.crud.security.service.RolService;


@Component
public class CreateRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {
//         Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
//        Rol rolUser = new Rol(RolNombre.ROLE_USER);
//        Rol rolProd = new Rol(RolNombre.ROLE_PROD);
//        rolService.save(rolAdmin);
//        rolService.save(rolUser);
//        rolService.save(rolProd);
        
    	
         
    }
}
