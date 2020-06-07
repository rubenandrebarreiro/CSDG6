package fct.unl.pt.csd.Common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormatConverter {

	public static String getDataStringFormat(Date date) {
		
		return new SimpleDateFormat("EEEE', 'dd'/'MM'/'yyyy - HH:mm:ss a").format(date);
		
	}
	
}
