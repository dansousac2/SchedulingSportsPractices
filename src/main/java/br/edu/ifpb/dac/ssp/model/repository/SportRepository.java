package br.edu.ifpb.dac.ssp.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.ssp.model.entity.Sport;

public interface SportRepository extends JpaRepository <Sport, Integer> {
	
	public Optional<Sport> findByName(String name);
	public boolean existsByName(String name);
	

}
