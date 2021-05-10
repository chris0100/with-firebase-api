package com.api.apichat.services;

import com.api.apichat.entities.Heroe;

import java.util.List;

public interface HeroeService {

    Heroe save(Heroe heroe);

    Heroe findById(Long id);

    List<Heroe> findAll();

    void deleteById(Long id);
}
