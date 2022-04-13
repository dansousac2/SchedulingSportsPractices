package br.edu.ifpb.dac.ssp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.dto.PlaceDTO;

@Service
public class PlaceConverterService {

	public Place dtoToPlace(PlaceDTO dto) {
		if (dto != null) {
			Place entity = new Place(dto.getId(), dto.getName(), dto.getReference(), dto.getMaximumCapacityParticipants(), dto.isPublic());
			
			return entity;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
	
	public PlaceDTO placeToDto(Place entity) {
		if (entity != null) {
			PlaceDTO dto = new PlaceDTO(entity.getId(), entity.getName(), entity.getReference(), entity.getMaximumCapacityParticipants(), entity.isPublic());
			
			return dto;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}

	public List<Place> dtoToPlace(List<PlaceDTO> dtoList) {
		
		if (dtoList != null) {
			List<Place> entityList = new ArrayList<>();
			
			Place entity = null;
			
			if (dtoList != null && !dtoList.isEmpty()) {
				for (PlaceDTO dto: dtoList) {
					entity = dtoToPlace(dto);
					entityList.add(entity);
				}
			}
			
			return entityList;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
	
	public List<PlaceDTO> placeToDto(List<Place> entityList) {
		if (entityList != null) {
			List<PlaceDTO> dtoList = new ArrayList<>();
			
			PlaceDTO dto = null;
			
			if (entityList != null && !entityList.isEmpty()) {
				for (Place place: entityList) {
					dto = placeToDto(place);
					dtoList.add(dto);
				}
			}
			
			return dtoList;
		}
		
		throw new NullPointerException("Could not convert because object is null");
	}
}
