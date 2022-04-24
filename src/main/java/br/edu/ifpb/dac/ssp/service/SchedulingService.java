package br.edu.ifpb.dac.ssp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.repository.SchedulingRepository;

@Service
public class SchedulingService {

	@Autowired
	private SchedulingRepository schedulingRepository;
	
	public List<Scheduling> findAll() {
		return schedulingRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return schedulingRepository.existsById(id);
	}
	
	public boolean existsByScheduledDate(LocalDateTime scheduledDate) {
		return schedulingRepository.existsByScheduledDate(scheduledDate);
	}
	
	public Scheduling findById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id");
		}
		
		if (!existsById(id)) {
			throw new ObjectNotFoundException(id);
		}
		
		return schedulingRepository.getById(id);
	}
	
	public Optional<Scheduling> findByScheduledDate(LocalDateTime scheduledDate) throws Exception {
		if (scheduledDate == null || scheduledDate.toString().isBlank()) {
			throw new MissingFieldException("scheduled date");
		}
		
		if (!schedulingRepository.existsByScheduledDate(scheduledDate)) {
			throw new ObjectNotFoundException(scheduledDate.toString());
		}
		
		return schedulingRepository.findByScheduledDate(scheduledDate);
	}
	
	public Scheduling save(Scheduling scheduling) throws Exception {
		if (!existsByScheduledDate(scheduling.getScheduledDate())) {
			return schedulingRepository.save(scheduling);
		}
		
		throw new TimeAlreadyScheduledException();
	}
	
	
	public void delete(Scheduling scheduling) throws Exception {
		if (!existsById(scheduling.getId())) {
			throw new ObjectNotFoundException(scheduling.getId());
		} 
		
		schedulingRepository.delete(scheduling);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException(id);
		}
		
		schedulingRepository.deleteById(id);
	}
}
