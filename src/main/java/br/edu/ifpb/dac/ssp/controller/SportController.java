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

import br.edu.ifpb.dac.ssp.model.Sport;
import br.edu.ifpb.dac.ssp.model.dto.SportDTO;
import br.edu.ifpb.dac.ssp.service.SportConverterService;
import br.edu.ifpb.dac.ssp.service.SportService;

@RestController
@RequestMapping("/api/sport")
public class SportController {
	
	@Autowired
	private SportService sportService;
	
	@Autowired
	private SportConverterService converterService;
	
	@GetMapping
	public ResponseEntity getAll() {
		List<Sport> entityList = sportService.findAll();
		
		List<SportDTO> dtoList = converterService.sportToDto(entityList);
		
		return ResponseEntity.ok().body(dtoList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		
		try {
			Sport entity = sportService.findById(id);
			SportDTO dto = converterService.sportToDto(entity);
			
			return ResponseEntity.ok().body(dto);
		
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody SportDTO dto) {
		
		try {
			Sport entity = converterService.dtoToSport(dto);
			entity = sportService.save(entity);
			dto = converterService.sportToDto(entity);
			
			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Integer id, @RequestBody SportDTO dto) {
		
		try {
			dto.setId(id);
			Sport entity = converterService.dtoToSport(dto);
			entity = sportService.update(entity);
			dto = converterService.sportToDto(entity);
			
			return ResponseEntity.ok().body(dto);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		
		try {
			sportService.deleteById(id);
			
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
