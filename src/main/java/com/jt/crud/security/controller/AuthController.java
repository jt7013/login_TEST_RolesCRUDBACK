package com.jt.crud.security.controller;

import springfox.documentation.annotations.ApiIgnore;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.jt.crud.dto.Mensaje;
import com.jt.crud.dto.ProductoDto;
import com.jt.crud.entity.Producto;
import com.jt.crud.security.dto.JwtDto;
import com.jt.crud.security.dto.LoginUsuario;
import com.jt.crud.security.dto.NuevoUsuario;
import com.jt.crud.security.entity.Rol;
import com.jt.crud.security.entity.Usuario;
import com.jt.crud.security.enums.RolNombre;
import com.jt.crud.security.jwt.JwtProvider;
import com.jt.crud.security.service.RolService;
import com.jt.crud.security.service.UsuarioService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        
    	if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        Usuario usuario =
                new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()));
//        List<Rol> roles = new ArrayList<>();
        Set<Rol> roles = new HashSet<>();
        
        
        	Optional<Rol> rn = rolService.getByRolNombre(RolNombre.ROLE_USER);
        	if (!rn.isEmpty())
        	{
	        	roles.add(rn.get());
	         
	            usuario.setRoles(roles);
        	}
       

        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }
    
    
    @PreAuthorize("hasRole('ADMIN')" )
    @PostMapping("/createRol")
    public ResponseEntity<?> create(@RequestBody Rol rol){
        if(StringUtils.isBlank(rol.getRolNombre().toString()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if(rolService.getByRolNombre(rol.getRolNombre()) != null)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        Rol roltmp = new Rol(rol.getRolNombre());
        rolService.save(roltmp);
        return new ResponseEntity(new Mensaje("producto creado"), HttpStatus.OK);
    }
    
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable("id")int id, @RequestBody Usuario user){
    
        Usuario usTMP = usuarioService.getOne(id).get();
        
        usTMP.setNombreUsuario(user.getNombreUsuario());
        usTMP.setNombre(user.getNombre());
        usTMP.setEmail(user.getEmail());
        
        Set<Rol> roles = new HashSet<>();  
        for (Rol r : user.getRoles()) {
        	
        	RolNombre q = r.getRolNombre();
        	Rol rolid =  rolService.getByRolNombre(q).get();
        	
        	roles.add(rolid);
            
        }
        
        //usTMP.setPassword(user.getPassword());// aqui no se actualizara pass
        usTMP.setRoles(roles);
        usuarioService.save(usTMP);
        return new ResponseEntity(new Mensaje("user actualizado"), HttpStatus.OK);
    }
    
    @ApiIgnore
    @GetMapping("/detail/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable("id") int id){
        if(!usuarioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Usuario usuario = usuarioService.getOne(id).get();
        return new ResponseEntity(usuario, HttpStatus.OK);
    }
    
    
    @GetMapping("/lista")
    public ResponseEntity<List<Usuario>> list(){
        List<Usuario> list = usuarioService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @GetMapping("/listroles")
    public ResponseEntity<List<Rol>> listRoles(){
        List<Rol> lista = rolService.list();
        return new ResponseEntity(lista, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwt = new JwtDto(token);
        return new ResponseEntity(jwt, HttpStatus.OK);
    }
    
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if(!usuarioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        usuarioService.delete(id);
        return new ResponseEntity(new Mensaje("usuario eliminado"), HttpStatus.OK);
    }
}
