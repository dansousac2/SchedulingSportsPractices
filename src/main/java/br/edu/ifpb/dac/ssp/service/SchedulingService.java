package br.edu.ifpb.dac.ssp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.RuleViolationException;
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.repository.SchedulingRepository;
import br.edu.ifpb.dac.ssp.service.RoleService.AVAILABLE_ROLES;

@Service
public class SchedulingService {

	@Autowired
	private SchedulingRepository schedulingRepository;
	@Autowired
	private LoginService loginService;
	@Autowired
	private RoleService roleService;
	
	public List<Scheduling> findAll() {
		List<Scheduling> list = schedulingRepository.findAll();
		return schedulingsBeginingToday(list);
	}
	
	public List<Scheduling> findAll(Scheduling filter) {
		Example<Scheduling> exp = Example.of(filter,
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		List<Scheduling> list = schedulingRepository.findAll(exp);
		System.out.println("Tamanho da lista retornada pelo repository.findAll(filtro): " + list.size());
		return schedulingsBeginingToday(list);
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
			throw new ObjectNotFoundException("agendamento", "id", id);
		}
		
		return schedulingRepository.getById(id);
	}
	
	public Scheduling save(Scheduling scheduling) throws Exception {
		User user = loginService.getLoggedUser();
		
		if (!scheduling.getPlace().isPublic() && 
			user.getAuthorities().contains(roleService.findByName(AVAILABLE_ROLES.STUDENT.name()))) {
			
			throw new RuleViolationException("Opera????o inv??lida! Estudantes s?? podem agendar pr??ticas para locais p??blicos");
		}
		
		scheduling.setCreator(user);
		
		return schedulingRepository.save(scheduling);
	}
	
	public void delete(Scheduling scheduling) throws Exception {
		if (!existsById(scheduling.getId())) {
			throw new ObjectNotFoundException("agendamento", "id", scheduling.getId());
		} 
		
		if (!scheduling.getCreator().equals(loginService.getLoggedUser())
				&& !loginService.getLoggedUser().getAuthorities().contains(roleService.findByName(AVAILABLE_ROLES.ADMIN.name()))) {
			
			throw new RuleViolationException("Opera????o inv??lida! Apenas o criador do agendamento pode exclui-lo");
		}
		
		schedulingRepository.delete(scheduling);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException("agendamento", "id", id);
		}
		
		Scheduling scheduling = findById(id);
		if (!scheduling.getCreator().equals(loginService.getLoggedUser())
				&& !loginService.getLoggedUser().getAuthorities().contains(roleService.findByName(AVAILABLE_ROLES.ADMIN.name()))) {
			throw new RuleViolationException("Opera????o inv??lida! Apenas o criador do agendamento pode exclui-lo");
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
	
	public boolean addSchedulingParticipant(Integer schedulingId, User user) throws Exception {
		Scheduling scheduling = findById(schedulingId);

		if (scheduling.getParticipants().size() >= scheduling.getPlace().getMaximumCapacityParticipants()) {
			return false;
		} 
		
		Set<User> setUser = new HashSet<>(scheduling.getParticipants());
		setUser.add(user);
		scheduling.setParticipants(setUser);
		
		save(scheduling);
		return true;
	}
	
	public boolean removeSchedulingParticipant(Integer schedulingId, User user) throws Exception {
		Scheduling scheduling = findById(schedulingId);
		
		if (scheduling.getParticipants().size() <= 0) {
			return false;
		}
		
		scheduling.getParticipants().remove(user);
		
		save(scheduling);
		return true;
	}
	
	private List<Scheduling> schedulingsBeginingToday(List<Scheduling> list) {
		Collections.sort(list, new ComparatorSchedulingDate());
		Collections.reverse(list);
		
		List<Scheduling> selectedList = new ArrayList<>();
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getScheduledDate().isBefore(LocalDate.now())) {
				break;
			}else {
				selectedList.add(list.get(i));
			}
		}
		
		Collections.reverse(selectedList);
		
		return selectedList;
	}
}
