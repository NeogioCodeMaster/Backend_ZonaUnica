package com.zonaunica.ZonaUnica.controllers;

import java.util.HashMap;
import java.util.Map;

import com.zonaunica.ZonaUnica.models.UsuarioModel;
import com.zonaunica.ZonaUnica.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;

    
    @PostMapping("/usuarios") //POST 
    public ResponseEntity<Map<String, String>> guardarUsuarios(@RequestBody UsuarioModel usuario){
        
        
        Map<String, String> respuesta= new HashMap<>();

        UsuarioModel u=this.usuarioService.buscarUsername(usuario.getUsername());
        if(u.getId()==null){
            this.usuarioService.guardarUsuario(usuario);
            respuesta.put("mensaje","Se agregó correctamente");
        }else{
            respuesta.put("mensaje","Usuario ya existe");
        }

        return ResponseEntity.ok(respuesta);
    }

    public ResponseEntity<Map<String, String>> EliminarUsuarios(@RequestBody UsuarioModel usuario){
        
        
        Map<String, String> respuesta= new HashMap<>();

        UsuarioModel u=this.usuarioService.buscarUsername(usuario.getUsername());
        if(u.getId()==null){
            this.usuarioService.eliminarPorId(usuario.getId());
            respuesta.put("mensaje","Se elimino correctamente");
        }else{
            respuesta.put("mensaje","Usuario no se pudo eliminar");
        }

        return ResponseEntity.ok(respuesta);
    }
}


