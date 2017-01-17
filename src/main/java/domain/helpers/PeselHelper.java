package domain.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PeselHelper {
	
	public enum Gender { Male, Female };

	public static boolean check(String correctPesel) {
		if(!correctPesel.matches("[0-9]{11}")){			
			return false;
		}
		
		int a = 0;
		int sum = 0;
		int[] weights = {1, 3, 7, 9};
		int peselControl = 0;
		
		for(char number: correctPesel.toCharArray()){
			int n = Character.getNumericValue(number);
			
			if (a == 10){
				peselControl = n; 
				break;
			} else {
				sum += n * weights[a%4];
				a++;
			}			
		}
		int control = (10 - (sum % 10)) % 10;		
		if(control != peselControl)
			return false;
		
		return true;
	}
	
	public static Gender getGender(String pesel) {
		if (check(pesel)) {
			char c = pesel.charAt(9);
			int i = Character.getNumericValue(c);
			return i % 2 == 0 ? Gender.Female : Gender.Male;
		}
		throw new RuntimeException("Pesel is not valid!");
	}
	
	public static Date getDate(String pesel) {
		try {
			int year = Integer.parseInt(pesel.substring(0, 2));
			if (year >= 70) {
				year += 1900;
			} else {
				year += 2000;
			}
			int month = Integer.parseInt(pesel.substring(2, 4));
			int day = Integer.parseInt(pesel.substring(4, 6));
			Calendar calendar = new GregorianCalendar(year, month-1, day);
			return calendar.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
