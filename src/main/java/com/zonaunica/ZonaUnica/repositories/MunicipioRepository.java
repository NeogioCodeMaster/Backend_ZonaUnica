package com.zonaunica.ZonaUnica.repositories;

import com.zonaunica.ZonaUnica.models.MunicipioModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends MongoRepository<MunicipioModel, String>{
    
}
