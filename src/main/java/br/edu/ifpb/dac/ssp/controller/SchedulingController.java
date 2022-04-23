package br.edu.ifpb.dac.ssp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;
import br.edu.ifpb.dac.ssp.service.SchedulingConverterService;
import br.edu.ifpb.dac.ssp.service.SchedulingService;

@RestController
@RequestMapping("api/scheduling")
public class SchedulingController {

	// TODO: Montar métodos
	
	@Autowired
	private SchedulingService schedulingService;
	
	@Autowired
	private SchedulingConverterService converterService;
	
	@GetMapping
	public ResponseEntity getAll() {
		List<Scheduling> entityList = schedulingService.findAll();
		
		// ...
		return null;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		return null;
	}
	
	@PostMapping
	public ResponseEntity save(@Valid @RequestBody SchedulingDTO dto) {
		return null;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Integer id, @Valid @RequestBody SchedulingDTO dto) {
		return null;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		return null;
	}
}
