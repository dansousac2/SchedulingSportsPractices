package br.edu.ifpb.dac.ssp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.dto.PlaceDTO;

@Service
public class PlaceConverterService {

	public Place dtoToPlace(PlaceDTO dto) {
		Place entity = new Place(dto.getId(), dto.getName(), dto.getReference(), dto.getMaximumCapacityParticipants(), dto.isPublic());
		
		return entity;
	}
	
	public PlaceDTO placeToDto(Place entity) {
		PlaceDTO dto = new PlaceDTO(entity.getId(), entity.getName(), entity.getReference(), entity.getMaximumCapacityParticipants(), entity.isPublic());
		
		return dto;
	}

	public List<Place> dtoToPlace(List<PlaceDTO> dtoList) {
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
	
	public List<PlaceDTO> placeToDto(List<Place> entityList) {
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
}
