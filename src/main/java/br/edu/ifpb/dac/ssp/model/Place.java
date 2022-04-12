package br.edu.ifpb.dac.ssp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "PRACTICE_PLACE")
@Entity
public class Place implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLACE_ID")
	private Integer id;
	
	@Column(name = "PLACE_NAME", nullable = false)
	private String name;
	
	@Column(name = "PLACE_REFERENCE", nullable = true)
	private String reference;
	
	@Column(name = "PLACE_MAXIMUM_CAPACITY_PEOPLE", nullable = true)
	private int maximumCapacityParticipants;
	
	@Column(name = "PLACE_IS_PUBLIC", nullable = false)
	private boolean isPublic;

}
