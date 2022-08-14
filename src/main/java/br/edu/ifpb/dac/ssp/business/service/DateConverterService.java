package br.edu.ifpb.dac.ssp.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.presentation.exception.TimeParseException;

@Service
public class DateConverterService {
	
	public LocalDate stringToDate(String dateString) throws Exception {
		LocalDate dateTime;
		
		try {
			dateTime = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {
			throw new TimeParseException("Não foi possível converter " + dateString);
		}
		
		return dateTime;
	}
	
	public String dateToString(LocalDate date) {
		return date.toString();
	}
	
	public LocalTime stringToTime(String timeString) throws Exception {
		LocalTime time;
		
		try {
			time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
		} catch (DateTimeParseException e) {
			throw new TimeParseException("Não foi possível converter " + timeString);
		}
		
		return time;
	}
	
	public String timeToString (LocalTime time) {
		return time.toString();
	}
	
	public LocalDateTime stringToDateTime(String dateTimeString) throws TimeParseException {
		LocalDateTime dateTime;
		
		try {
			dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		} catch (DateTimeParseException e) {
			throw new TimeParseException("Não foi possível converter " + dateTimeString);
		}
		
		return dateTime;
	}
	
	public String dateTimeToString (LocalDateTime dateTime) {
		return dateTime.toString();
	}
	
	public LocalDateTime dateTimeNow() {
		return LocalDateTime.now();
	}
}
