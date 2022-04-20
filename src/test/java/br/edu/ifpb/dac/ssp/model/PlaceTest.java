package br.edu.ifpb.dac.ssp.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class PlaceTest {

	@Test
	@ParameterizedTest
	@ValueSource(strings = {"", "    ", "123 de 456", "12.3", " 999,99","Fulano,f", "Fulano.de.tal"})
	void namePlaceIsInvalid(String candidate) {
		
	}
}
