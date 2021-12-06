package com.zonaunica.ZonaUnica.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zonaunica.ZonaUnica.models.UsuarioModel;
import com.zonaunica.ZonaUnica.services.UsuarioService;
import com.zonaunica.ZonaUnica.utils.Autorizacion;
import com.zonaunica.ZonaUnica.utils.BCrypt;
import com.zonaunica.ZonaUnica.Exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost"})
@RestController
@RequestMapping("/api")
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;


    @GetMapping("/verificar")
    public ResponseEntity<Map<String, Boolean>> validarToken(){
        Map<String, Boolean> respuesta= new HashMap<>();
        respuesta.put("ok", true);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/usuarios")
    public List<UsuarioModel> traerTodos(){
        return this.usuarioService.traerTodos();
    }

    @GetMapping("/usuarios/{id}")
    public UsuarioModel buscarID(@PathVariable String id){
        return this.usuarioService.buscarPorId(id).get();
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, String>> guardarUsuario(@RequestBody UsuarioModel usuario, Errors error){

        if(error.hasErrors()){
            throwError(error);
        }
        Map<String, String> respuesta = new HashMap<>();
        usuario.setPassword(BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()));

        UsuarioModel u = usuarioService.buscarUsername(usuario.getUsername());

        if(u.getId()==null){
            usuarioService.guardarUsuario(usuario);
            respuesta.put("mensaje", "Se registró el usuario correctamente");
        }else{
            respuesta.put("mensaje", "El usuario ya se encuentra registrado");
        }
        return ResponseEntity.ok(respuesta);
        
    }

    @PostMapping("/usuarios/login")
    public ResponseEntity<UsuarioModel> login(@RequestBody UsuarioModel usuario){
        UsuarioModel u= usuarioService.buscarUsername(usuario.getUsername());
        if(u.getNombre()== null){
            throw new CustomException("Usuario o contraseña incorrectos");
        }
        if(!BCrypt.checkpw(usuario.getPassword(), u.getPassword())){
            throw new CustomException("Usuario o contraseña incorrectos");
        }
        String hash="";
        long tiempo= System.currentTimeMillis();

        if(u.getId()!=""){
            hash=Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, Autorizacion.KEY)
            .setSubject(u.getNombre())
            .setIssuedAt(new Date (tiempo))
            .setExpiration(new Date (tiempo+9000000))
            .claim("username", u.getUsername())
            .claim("correo", u.getCorreo())
            .compact();
        }
        u.setHash(hash);
        return ResponseEntity.ok(u);
    }

    @PutMapping("/usuarios")
    public ResponseEntity<Map<String, String>> actualizar(@RequestBody UsuarioModel usuario, Errors error){
        if(error.hasErrors()){
            throwError(error);
        }
        usuarioService.guardarUsuario(usuario);
        Map<String,String> respuesta=new HashMap<>();
        respuesta.put("Mensaje","La información de usuario se ha actualizado");
        return ResponseEntity.ok(respuesta);
    }


    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Map<String,String>> eliminarPorId(@PathVariable("id") String id){
        Boolean existe =  this.usuarioService.existById(id);
        Map<String, String> respuesta=new HashMap<>();
        if(!existe){
            respuesta.put("Mensaje","No existe el Registro con id");
            return ResponseEntity.ok(respuesta);
        }
        usuarioService.eliminarPorId(id);
        respuesta.put("Mensaje", "Registro eliminado correctamente");
        return ResponseEntity.ok(respuesta);
    }

    private void throwError(Errors error) {
        String message="";
        int index=0;
        for(ObjectError e: error.getAllErrors()){
            if(index>0){
                message += " | "; 
            }
            message += String.format("Parametro: %s - Mensaje: %s", e.getObjectName(), e.getDefaultMessage());
        }
        throw new CustomException(message);
    }
}


