package br.edu.ifpb.dac.ssp.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

	private Integer id;
	private String name;
	private String reference;
	private int maximumCapacityParticipants;
	private boolean isPublic;
}
