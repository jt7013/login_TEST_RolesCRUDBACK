package com.jt.crud.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jt.crud.entity.Producto;
import com.jt.crud.security.entity.Usuario;
import com.jt.crud.security.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    public boolean existsByEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }
    
    
    
    public Optional<Usuario> getOne(int id){
        return usuarioRepository.findById(id);
    }
    
    public List<Usuario> list(){
        return usuarioRepository.findAll();
    }
    
    public void delete(int id){
    	usuarioRepository.deleteById(id);
    }
    
    
    public boolean existsById(int id){
        return usuarioRepository.existsById(id);
    }

    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }
}
