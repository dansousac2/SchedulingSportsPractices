package br.edu.ifpb.dac.ssp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.repository.SchedulingRepository;

@Service
public class SchedulingService {

	@Autowired
	private SchedulingRepository schedulingRepository;
	
	public List<Scheduling> findAll() {
		return schedulingRepository.findAll();
	}
	
	public List<Scheduling> findAllByPlaceId(Integer id) {
		return schedulingRepository.findAllByPlaceId(id);
	}
	
	public List<Scheduling> findAllByPlaceIdAndScheduledDate(Integer placeId, LocalDate scheduledDate) {
		return schedulingRepository.findAllByPlaceIdAndScheduledDate(placeId, scheduledDate);
	}
	
	public List<Scheduling> findAllBySportId(Integer id) {
		return schedulingRepository.findAllBySportId(id);
	}
	
	public List<Scheduling> findAllBySportIdAndScheduledDate(Integer sportId, LocalDate scheduledDate) {
		return schedulingRepository.findAllBySportIdAndScheduledDate(sportId, scheduledDate);
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
	
	public int getSchedulingQuantityOfParticipants(Integer id) throws Exception {
		Scheduling scheduling = findById(id);
		
		return scheduling.getParticipants().size();
	}
	
	public Set<User> getSchedulingParticipants(Integer id) throws Exception {
		Scheduling scheduling = findById(id);
		
		return scheduling.getParticipants();
	}
	
	public void addSchedulingParticipant(Integer schedulingId, User user) throws Exception {
		Scheduling scheduling = findById(schedulingId);
		
		if (scheduling.getParticipants().size() < scheduling.getPlace().getMaximumCapacityParticipants()) {
			scheduling.getParticipants().add(user);
		}
		
		save(scheduling);
	}
	
	public void removeSchedulingParticipant(Integer schedulingId, User user) throws Exception {
		Scheduling scheduling = findById(schedulingId);
		
		if (scheduling.getParticipants().size() > 0) {
			scheduling.getParticipants().remove(user);
		}
		
		save(scheduling);
	}
}
