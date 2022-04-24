package br.edu.ifpb.dac.ssp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;

@Service
public class SchedulingConverterService {
	
	@Autowired 
	private DateConverterService dateConverter;
	
	public Scheduling dtoToScheduling(SchedulingDTO dto) throws Exception {
		if (dto != null) {
			Scheduling entity = new Scheduling();
			
			entity.setId(dto.getId());
			entity.setScheduledDate(dateConverter.stringToDate(dto.getScheduledDate()));
			entity.setScheduledStartTime(dateConverter.stringToTime(dto.getScheduledStartTime()));
			entity.setScheduledFinishTime(dateConverter.stringToTime(dto.getScheduledFinishTime()));
			entity.setPlaceName(dto.getPlaceName());
			entity.setSportName(dto.getSportName());
			
			return entity;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
	
	public SchedulingDTO schedulingToDto(Scheduling entity) throws Exception {
		if (entity != null) {
			SchedulingDTO dto = new SchedulingDTO();
			
			dto.setId(entity.getId());
			dto.setScheduledDate(dateConverter.dateToString(entity.getScheduledDate()));
			dto.setScheduledStartTime(dateConverter.timeToString(entity.getScheduledStartTime()));
			dto.setScheduledFinishTime(dateConverter.timeToString(entity.getScheduledFinishTime()));
			dto.setPlaceName(entity.getPlaceName());
			dto.setSportName(entity.getSportName());
			
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
