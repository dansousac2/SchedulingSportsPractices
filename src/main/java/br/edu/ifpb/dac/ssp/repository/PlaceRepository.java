package br.edu.ifpb.dac.ssp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.ssp.model.Place;


public interface PlaceRepository extends JpaRepository<Place, Integer>{
	
	public Optional<Place> findByName(String name);
	public boolean existsByName(String name);
}
