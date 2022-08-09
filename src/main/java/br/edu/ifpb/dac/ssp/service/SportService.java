package br.edu.ifpb.dac.ssp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.Sport;
import br.edu.ifpb.dac.ssp.repository.SportRepository;

@Service
public class SportService {
	
	@Autowired
	private SportRepository sportRepository;
	
	public List<Sport> findAll() {
		return sportRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return sportRepository.existsById(id);
	}

	public boolean existsByName(String name) {
		return sportRepository.existsByName(name);
	}
	
	public Sport findById(Integer id) throws Exception {
		if (!existsById(id)) {
			throw new ObjectNotFoundException("esporte", "id", id);
		}
		return sportRepository.getById(id);
	}
	
	public Optional<Sport> findByName(String name) throws Exception {
		if (name == null || name.isBlank()) {
			throw new MissingFieldException("nome");
		}
		
		if (!existsByName(name)) {
			throw new ObjectNotFoundException("esporte", "nome", name);
		}
		return sportRepository.findByName(name);
	}
	
	public Sport save(Sport sport) throws Exception {
		if (sport.getName() == null || sport.getName().isBlank()) {
			throw new MissingFieldException("nome", "save");
		}
		
		if (existsByName(sport.getName())) {
			throw new ObjectAlreadyExistsException("Já existe um esporte com nome " + sport.getName());
		}
		
		return sportRepository.save(sport);
	}
	
	public Sport update(Sport sport) throws Exception {
		if (sport.getName() == null || sport.getName().isBlank()) {
			throw new MissingFieldException("nome", "update");
		}
		
		if (sport.getId() == null) {
			throw new MissingFieldException("id", "update");
		} else if (!existsById(sport.getId())) {
			throw new ObjectNotFoundException("esporte", "id", sport.getId());
		}
		
		if (existsByName(sport.getName())) {
			Sport sportSaved = findByName(sport.getName()).get();
			if (sportSaved.getId() != sport.getId()) {
				throw new ObjectAlreadyExistsException("Já existe um esporte com nome " + sport.getName());
			}
		}
		
		return sportRepository.save(sport);
	}
	
	public void delete(Sport sport) throws Exception {
		if (sport.getId() == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(sport.getId())) {
			throw new ObjectNotFoundException("esporte", "id", sport.getId());
		}
		
		sportRepository.delete(sport);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException("esporte", "id", id);
		}
		
		sportRepository.deleteById(id);
	}

}
