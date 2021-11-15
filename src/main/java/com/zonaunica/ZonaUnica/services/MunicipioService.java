package com.zonaunica.ZonaUnica.services;


import java.util.List;
import java.util.Optional;

import com.zonaunica.ZonaUnica.models.MunicipioModel;
import com.zonaunica.ZonaUnica.repositories.MunicipioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MunicipioService {
    @Autowired
    MunicipioRepository municipioRepository;

    public void registrarMunicipio(MunicipioModel municipio){
        MunicipioRepository.save(municipio);
    }

    public List<MunicipioModel> traerMunicipios(){
        return MunicipioRepository.findAll();
    }

    public Boolean existById(String id){
        return MunicipioRepository.existsById(id);
    }

    public Optional<MunicipioModel> buscarById(String id){
        return MunicipioRepository.findById(id);
    }


    public void eliminarById(String id) {
       MunicipioRepository.deleteById(id);
    }
    
}

