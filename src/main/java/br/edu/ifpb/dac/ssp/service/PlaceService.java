package br.edu.ifpb.dac.ssp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.repository.PlaceRepository;

@Service
public class PlaceService {
	
	// Falta terminar de incluir as validações e exceções correspondentes
	
	@Autowired
	private PlaceRepository placeRepository;
	
	public List<Place> findAll() {
		return placeRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return placeRepository.existsById(id);
	}
	
	public Place findById(Integer id) {
		return placeRepository.getById(id);
	}
	
	public Optional<Place> findByName(String name) throws Exception {
		if (name == null) {
			throw new MissingFieldException("name");
		}
		
		return placeRepository.findByName(name);
	}
	
	public Place save(Place place) {
		return placeRepository.save(place);
	}
	
	public Place update(Place place) throws Exception {
		if (place.getId() == null) {
			throw new MissingFieldException("id");
		}
		
		return placeRepository.save(place);
	}
	
	public void delete(Place place) throws Exception {
		if (place.getId() == null) {
			throw new MissingFieldException("id");
		}
		
		placeRepository.delete(place);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id");
		}
		
		placeRepository.deleteById(id);
	}
}
