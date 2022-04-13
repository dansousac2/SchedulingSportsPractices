package br.edu.ifpb.dac.ssp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.dto.PlaceDTO;
import br.edu.ifpb.dac.ssp.service.PlaceConverterService;
import br.edu.ifpb.dac.ssp.service.PlaceService;

@RestController
@RequestMapping("/api/place")
public class PlaceController {
	
	@Autowired
	private PlaceService placeService;
	@Autowired
	private PlaceConverterService converterService;
	
	// Falta organizar o getAll para funcionar com um filtro para name também (utilizando Example)
	// Falta completar as validações e inserir as exceções necessárias
	
	@GetMapping
	public ResponseEntity getAll() {
		List<Place> entityList = placeService.findAll();
		
		List<PlaceDTO> dtoList = converterService.placeToDto(entityList);
		
		return ResponseEntity.ok().body(dtoList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		
		try {
			Place entity = placeService.findById(id);
			PlaceDTO dto = converterService.placeToDto(entity);
			
			return ResponseEntity.ok().body(dto);
		
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody PlaceDTO dto) {
		
		try {
			Place entity = converterService.dtoToPlace(dto);
			entity = placeService.save(entity);
			dto = converterService.placeToDto(entity);
			
			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Integer id, @RequestBody PlaceDTO dto) {
		
		try {
			dto.setId(id);
			Place entity = converterService.dtoToPlace(dto);
			entity = placeService.update(entity);
			dto = converterService.placeToDto(entity);
			
			return ResponseEntity.ok().body(dto);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		
		try {
			placeService.deleteById(id);
			
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
