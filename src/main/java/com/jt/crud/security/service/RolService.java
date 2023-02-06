package com.jt.crud.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jt.crud.security.entity.Rol;
import com.jt.crud.security.entity.Usuario;
import com.jt.crud.security.enums.RolNombre;
import com.jt.crud.security.repository.RolRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
    
    public List<Rol> list(){
        return rolRepository.findAll();
    }
    
    public Optional<Rol> getOne(int id){
        return rolRepository.findById(id);
    }
}
