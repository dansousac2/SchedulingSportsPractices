package br.edu.ifpb.dac.ssp.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpb.dac.ssp.model.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {

	public Optional<Scheduling> findByScheduledDate(LocalDateTime scheduledDate);
	public boolean existsByScheduledDate(LocalDateTime scheduledDate);
}
