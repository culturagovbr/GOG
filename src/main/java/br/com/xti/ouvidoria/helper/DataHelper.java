package br.com.xti.ouvidoria.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * @author samuel.guimaraes
 */
public class DataHelper {

    public static Date getDataMin(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    public static Date getDataMax(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }
    
    public static Date converterData(String dataTexto) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(dataTexto);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String converterData(Date data) {
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
    }
    
    public static int getDiferencaEntreDatasEmDias(Date date1, Date date2) {
        int dias = 0;
        
        DateTimeZone BRAZIL = DateTimeZone.forID("America/Sao_Paulo");
        DateTime start = new LocalDate(date1.getTime(), BRAZIL).toDateTimeAtStartOfDay();
        DateTime end = new LocalDate(date2.getTime(), BRAZIL).toDateTimeAtStartOfDay();
        Days days = Days.daysBetween(start, end);
        
        if(days.isGreaterThan(null)) {
        	dias = days.getDays();
        } else {
        	days = days.negated();
        	if(days.isGreaterThan(null)) {
        		dias = days.getDays();
        	}
        }
        
        return dias;
    }
    
    public static Date criarData(int mes, int ano) {
    	DateTime dateTime = new DateTime(ano, ++mes, 1, 0, 0, 0, 0);
    	return dateTime.toDate();
    }
    
    public static int getMes(Date data) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return cal.get(Calendar.MONTH);
    }
    
    public static int getAno(Date data) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(data);
    	return cal.get(Calendar.YEAR);
    }
	
	public static String getNomePeriodo(int mes, int ano) {
		DateTime dateTime = new DateTime(ano, ++mes, 1, 0, 0, 0, 0);
		String mesStr = dateTime.monthOfYear().getAsShortText(new Locale("pt", "BR"));
		
		return mesStr+"/"+ano;
	}
	
	public static StringBuilder getNomeSubtitulo(Date dataDe, Date dataAte) {
		StringBuilder nomeSubtitulo = new StringBuilder();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			nomeSubtitulo
			.append("'")
			.append(sdf.format(dataDe))
			.append(" à ")
			.append(sdf.format(dataAte))
			.append("'");
		} catch (Exception e) {
			nomeSubtitulo.setLength(0);
		}
			
		return nomeSubtitulo;
	}
    
}
