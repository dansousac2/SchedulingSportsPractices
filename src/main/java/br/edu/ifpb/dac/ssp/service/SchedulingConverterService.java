package br.edu.ifpb.dac.ssp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;

@Service
public class SchedulingConverterService {

	// TODO: Organizar melhor conversões e se possível diminuir dependências
	
	@Autowired 
	private DateConverterService dateConverter;
	
	// Tentar tirar esses dois daqui e colocar no controller
	@Autowired
	private PlaceService placeService;
	
	@Autowired
	private SportService sportService;
	
	public Scheduling dtoToScheduling(SchedulingDTO dto) throws Exception {
		if (dto != null) {
			Scheduling entity = new Scheduling();
			
			entity.setId(dto.getId());
			entity.setScheduledDate(dateConverter.stringToDate(dto.getScheduledDate()));
			entity.setDuration(dateConverter.stringToTime(dto.getDuration()));
			entity.setPlaceId(placeService.findByName(dto.getPlaceName()).get().getId());
			entity.setSportId(sportService.findByName(dto.getSportName()).get().getId());
			
			return entity;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
	
	public SchedulingDTO schedulingToDto(Scheduling entity) throws Exception {
		if (entity != null) {
			SchedulingDTO dto = new SchedulingDTO();
			dto.setId(entity.getId());
			dto.setScheduledDate(dateConverter.dateToString(entity.getScheduledDate()));
			dto.setDuration(dateConverter.timeToString(entity.getDuration()));
			dto.setPlaceName(placeService.findById(entity.getId()).getName());
			dto.setSportName(sportService.findById(entity.getId()).getName());
			
			return dto;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
	
	public List<Scheduling> dtosToScheduling(List<SchedulingDTO> dtoList) throws Exception {
		
		if (dtoList != null ) {
			List<Scheduling> entityList = new ArrayList<>();
			
			Scheduling entity = null;
			
			if (!dtoList.isEmpty()) {
				for (SchedulingDTO dto: dtoList) {
					entity = dtoToScheduling(dto);
					entityList.add(entity);
				}
			}
			
			return entityList;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
	
	public List<SchedulingDTO> schedulingToDtos(List<Scheduling> entityList) throws Exception {
		if (entityList != null) {
			List<SchedulingDTO> dtoList = new ArrayList<>();
			
			SchedulingDTO dto = null;
			
			if (!entityList.isEmpty()) {
				for (Scheduling scheduling: entityList) {
					dto = schedulingToDto(scheduling);
					dtoList.add(dto);
				}
			}
			
			return dtoList;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
}
