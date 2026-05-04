package com.fullstack3.fullstack3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Column(name = "APATERNO", nullable = false, length = 100)
    private String apaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Column(name = "AMATERNO", nullable = false, length = 100)
    private String amaterno;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(name = "CORREO", nullable = false, unique = true, length = 100)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Column(name = "ROL", nullable = false, length = 20)
    private String rol;

    public Usuario() {}

    public Usuario(Long id, String nombre, String apaterno, String amaterno, String correo, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    public String getApaterno() { return apaterno; }
    public void setApaterno(String apaterno) { this.apaterno = apaterno; }

    public String getAmaterno() { return amaterno; }
    public void setAmaterno(String amaterno) { this.amaterno = amaterno; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}