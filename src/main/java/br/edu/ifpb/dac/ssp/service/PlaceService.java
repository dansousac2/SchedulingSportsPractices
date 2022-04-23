package br.edu.ifpb.dac.ssp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.repository.PlaceRepository;

@Service
public class PlaceService {
	
	@Autowired
	private PlaceRepository placeRepository;
	
	public List<Place> findAll() {
		return placeRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return placeRepository.existsById(id);
	}
	
	public Place findById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id");
		}
		
		if (!existsById(id)) {
			throw new ObjectNotFoundException(id);
		}
		return placeRepository.getById(id);
	}
	
	public Optional<Place> findByName(String name) throws Exception {
		if (name == null || name.isBlank()) {
			throw new MissingFieldException("name");
		}
		
		if (!placeRepository.existsByName(name)) {
			throw new ObjectNotFoundException(name);
		}
		return placeRepository.findByName(name);
	}
	
	public Place save(Place place) throws Exception {
		if (place.getName() == null || place.getName().isBlank()) {
			throw new MissingFieldException("name", "save");
		}
		
		return placeRepository.save(place);
	}
	
	public Place update(Place place) throws Exception {
		if (place.getName() == null || place.getName().isBlank()) {
			throw new MissingFieldException("name", "update");
		}
		
		if (place.getId() == null) {
			throw new MissingFieldException("id", "update");
		} else if (!existsById(place.getId())) {
			throw new ObjectNotFoundException(place.getId());
		} 
		
		return placeRepository.save(place);
	}
	
	public void delete(Place place) throws Exception {
		if (place.getId() == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(place.getId())) {
			throw new ObjectNotFoundException(place.getId());
		}
		
		placeRepository.delete(place);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException(id);
		}
		
		placeRepository.deleteById(id);
	}
}

