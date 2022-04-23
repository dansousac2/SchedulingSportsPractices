package br.edu.ifpb.dac.ssp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpb.dac.ssp.model.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {

	public Optional<Scheduling> findByName(String name);
	public boolean existsByName(String name);
}
