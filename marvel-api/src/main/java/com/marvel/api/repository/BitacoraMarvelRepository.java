package com.marvel.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface BitacoraMarvelRepository extends CrudRepository<BitacoraAccesoMarvelEntity, Long> {
}