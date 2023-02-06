package com.jt.crud.security.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.jt.crud.security.enums.RolNombre;

@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;

    public Rol() {
    }
    
    public Rol(String rolNombre) {
    	
    	RolNombre rn =  RolNombre.valueOf(rolNombre);
    	this.rolNombre=rn;
    }

    public Rol(@NotNull RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RolNombre getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }
}
