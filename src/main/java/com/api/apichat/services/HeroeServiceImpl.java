package com.api.apichat.services;

import com.api.apichat.entities.Heroe;
import com.api.apichat.repositories.HeroesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroeServiceImpl implements HeroeService{

    @Autowired
    private HeroesRepository heroesRepositoryObj;

    @Override
    public Heroe save(Heroe heroe) {
        return heroesRepositoryObj.save(heroe);
    }

    @Override
    public Heroe findById(Long id) {
        try{
            return heroesRepositoryObj.findById(id).get();
        }catch (Exception nu){
            return null;
        }
    }

    @Override
    public List<Heroe> findAll() {
        return heroesRepositoryObj.findAll();
    }

    @Override
    public void deleteById(Long id) {
        heroesRepositoryObj.deleteById(id);
    }
}
