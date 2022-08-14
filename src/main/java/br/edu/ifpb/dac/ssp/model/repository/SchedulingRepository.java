package br.edu.ifpb.dac.ssp.model.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.ssp.model.entity.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {
	
	public List<Scheduling> findAllByPlaceId(Integer id);
	public List<Scheduling> findAllByPlaceIdAndScheduledDate(Integer placeId, LocalDate scheduledDate);
	public List<Scheduling> findAllBySportId(Integer id);
	public List<Scheduling> findAllBySportIdAndScheduledDate(Integer sportId, LocalDate scheduledDate);
} 
