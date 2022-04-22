package br.edu.ifpb.dac.ssp.service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateConverterService {

	// TODO: Finalizar
	
	public Date stringToDate(String data) {
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		formatoData.setLenient(false);
		
		try {
			Date dataConvertida = formatoData.parse(data);
			return dataConvertida;
		} catch (Exception e) {
			// lançar exceção
		}
		
		return null;
	}
	
	public String dateToString(Date date) {
		// Implementar depois
		return null;
	}
	
	public LocalTime stringToTime(String time) {
		return null;
	}
}
