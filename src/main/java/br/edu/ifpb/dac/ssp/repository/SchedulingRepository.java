package br.edu.ifpb.dac.ssp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpb.dac.ssp.model.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {
	
	public List<Scheduling> findAllByPlaceName(String placeName);
	public List<Scheduling> findAllByPlaceNameAndScheduledDate(String placeName, LocalDate scheduledDate);
	public List<Scheduling> findAllBySportName(String sportName);
	public List<Scheduling> findAllBySportNameAndScheduledDate(String sportName, LocalDate scheduledDate);
} 
