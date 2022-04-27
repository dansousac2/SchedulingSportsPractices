package br.edu.ifpb.dac.ssp.service;

import java.time.LocalDate;
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
	
	public List<Scheduling> findAllByPlaceName(String placeName) {
		return schedulingRepository.findAllByPlaceName(placeName);
	}
	
	public List<Scheduling> findAllByPlaceNameAndScheduledDate(String placeName, LocalDate scheduledDate) {
		return schedulingRepository.findAllByPlaceNameAndScheduledDate(placeName, scheduledDate);
	}
	
	public List<Scheduling> findAllBySportName(String sportName) {
		return schedulingRepository.findAllBySportName(sportName);
	}
	
	public List<Scheduling> findAllBySportNameAndScheduledDate(String sportName, LocalDate scheduledDate) {
		return schedulingRepository.findAllBySportNameAndScheduledDate(sportName, scheduledDate);
	}
	
	public boolean existsById(Integer id) {
		return schedulingRepository.existsById(id);
	}
	
	public Scheduling findById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id");
		}
		
		if (!existsById(id)) {
			throw new ObjectNotFoundException("Scheduling", "id", id);
		}
		
		return schedulingRepository.getById(id);
	}
	
	public Scheduling save(Scheduling scheduling) {
		return schedulingRepository.save(scheduling);
	}
	
	public void delete(Scheduling scheduling) throws Exception {
		if (!existsById(scheduling.getId())) {
			throw new ObjectNotFoundException("Scheduling", "id", scheduling.getId());
		} 
		
		schedulingRepository.delete(scheduling);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException("Scheduling", "id", id);
		}
		
		schedulingRepository.deleteById(id);
	}
}
