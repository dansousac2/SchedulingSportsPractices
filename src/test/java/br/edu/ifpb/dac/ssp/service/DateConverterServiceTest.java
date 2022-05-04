package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import br.edu.ifpb.dac.ssp.exception.TimeParseException;

class DateConverterServiceTest {

	private static DateConverterService converter;
	
	@BeforeAll
	public static void setup() {
		converter = new DateConverterService();
	}

	@ParameterizedTest
	@ValueSource(strings = {"2020-01-01", "2024-02-29", "0001-01-01", "9999-01-01"}) // valid
	public void stringToDateInvalid(String s) { 
		
		assertDoesNotThrow(() -> converter.stringToDate(s));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "\n", "2020-13-01", "2024-02-32", "0000-01-01", "10000-01-01"}) // invalid
	public void stringToDateValid(String s) { 
		try {
			Throwable exc = assertThrows(TimeParseException.class, () -> converter.stringToDate(s));
			assertEquals("Could not convert " + s + " to LocalDate!", exc.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"23:59","00:00", "24:00"}) // valid
	public void stringToTimeValid(String s) { 
		assertDoesNotThrow(() -> converter.stringToTime(s));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"12h30min", "23:60", "24:01", "", "\n", "\t", "12-35"}) // invalid
	public void stringToTimeinvalid(String s) { 
		try {
			Throwable exc = assertThrows(TimeParseException.class, () -> converter.stringToTime(s));
			assertEquals("Could not convert " + s + " to LocalTime!", exc.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
