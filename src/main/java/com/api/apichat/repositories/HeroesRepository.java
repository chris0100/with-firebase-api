package com.api.apichat.repositories;

import com.api.apichat.entities.Heroe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroesRepository extends JpaRepository<Heroe, Long> {


}
