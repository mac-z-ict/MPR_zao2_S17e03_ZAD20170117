package domain.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import domain.helpers.PeselHelper.Gender;

public class PeselHelperTest {
	
	@Test
	public void checkPeselTest() {
		String correctPesel = "96061010469";
		boolean isCorrect = PeselHelper.check(correctPesel);
		assertTrue(isCorrect);
	}
	
	@Test
	public void checkPeselWithInvalidCharacterTest(){
	 
		assertFalse(PeselHelper.check("sdzjkbnfsdkjnjk"));
	}
	
	@Test
	public void checkPeselWithInvalidlengthTest(){
		assertFalse(PeselHelper.check("960610104691"));
	}
	
	@Test
	public void checkPeselWithInvalidCheckSumTest(){
		assertFalse(PeselHelper.check("96061010468"));
	}
	
	@Test
	public void extractDateFromPeselTest(){
		Date date = PeselHelper.getDate("96061010469");
		Calendar actual = new GregorianCalendar();
		actual.setTime(date);
		
		Calendar expected = new GregorianCalendar();
		expected.set(1996, 5, 10, 0, 0, 0);
		
// porownuje czy expected i date to ten sam obiekt zamiast porownac czy data w tych obiektach jest taka sama.
//		assertEquals(expected, actual);
		
		assertEquals(expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
		assertEquals(expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
		assertEquals(expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH));
	}
	
	@Test
	public void extractGenderFromPeselTest(){
		Gender gender = PeselHelper.getGender("96061010469");
		assertEquals(gender, Gender.Female);
	}
	
	
}
