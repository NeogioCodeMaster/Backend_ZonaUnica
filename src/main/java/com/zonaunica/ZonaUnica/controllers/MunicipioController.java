package com.zonaunica.ZonaUnica.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.zonaunica.ZonaUnica.models.MunicipioModel;
import com.zonaunica.ZonaUnica.services.MunicipioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

@RestController
@RequestMapping("/api")
public class MunicipioController {

    @Autowired
    MunicipioService municipioService;

    @GetMapping("/municipios")
    public List<MunicipioModel> traerMunicipios(){
        return municipioService.traerMunicipios();
    }

    @GetMapping("/municipios/{id}")
    public MunicipioModel obtenerById(@PathVariable("id") String id ){
        return municipioService.buscarById(id).get();
    }

    @PostMapping("/municipios")
    public ResponseEntity<Map<String, String>> guardarMunicipio(@Valid @RequestBody MunicipioModel municipio, Errors error){
        if(error.hasErrors()){
            throwError(error);
        }
        municipioService.registrarMunicipio(municipio);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("Mensaje", "El municipio se agregó correctamente");
        return ResponseEntity.ok(respuesta);
   }
   @PutMapping("/municipios")
    public ResponseEntity<Map<String, String>> actualizar(@RequestBody MunicipioModel municipio, Errors error){
        if(error.hasErrors()){
            throwError(error);
        }
        municipioService.registrarMunicipio(municipio);
        Map<String, String> respuesta= new HashMap<>();
        respuesta.put("Mensaje", "El municipio se actualizó correctamente");
        return ResponseEntity.ok(respuesta);
    }
    @DeleteMapping("/sitios/{id}")
    public ResponseEntity<Map<String, String>> eliminarById(@PathVariable String id){
        Boolean existe= municipioService.existById(id);
        Map<String, String> respuesta = new HashMap<>();
        if(!existe){
            respuesta.put("Mensaje", "Este existe ese id ");
            return ResponseEntity.ok(respuesta);
        } 
        municipioService.eliminarById(id);
        respuesta.put("Mensaje", "El id se eliminnó correctamente");
        return ResponseEntity.ok(respuesta);
    }

    private void throwError(Errors error) {
        String message="";
        int index=0;
        for(ObjectError e: error.getAllErrors()){
            if(index>0){
                message += " | ";
            }
            message += String.format("Parametro: %s - Mensaje: %s", e.getObjectName(),e.getDefaultMessage());
        }
        throw new CustomException(message);
    }
}







