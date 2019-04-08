package application.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
	static String formattedDateTime;
	
	public DateTime() {
		
	}
	
	/**
	 * Retrieves the current date
	 * @return
	 */
	public static String retrieveCurrentDate() {
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
		formattedDateTime = dateFormat.format(currentDate);
		return formattedDateTime;
	}
}
