import java.util.HashMap;

public class Calendar {
	
	private static HashMap<Integer, HashMap<Integer, HashMap<Integer,int[]>>> PSS;
	private static final int NUMBEROFMONTHS = 12;
	private static final int CURRENTYEAR = 2021;
	
	public Calendar()
	{
		PSS = new HashMap<>();
		generateCalendar();
	}
	
	
	/**
	 * This method constructs the calendar data structure upon creation of a calendar object
	 */
	private static void generateCalendar()
	{
		
		int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		
		// Initializes our PSS with a 2021 calendar
		PSS.put(CURRENTYEAR, new HashMap<>());
	
		for(int currentMonth=1; currentMonth<= NUMBEROFMONTHS; currentMonth++)
		{			
			// Fills the Year 2021 with 12 "months"
			PSS.get(CURRENTYEAR).put(currentMonth, new HashMap<Integer, int[]>());
			
			for(int currentDay=1; currentDay<=daysInMonth[currentMonth-1]; currentDay++)
			{
				PSS.get(CURRENTYEAR).get(currentMonth).put(currentDay, new int[96]);
				
			}
		}	
	}
		
	//Returns array for specified day if day is not present a new array is generated
	public int[] getDay(Date date)
	{
		int day = date.getDay();
		int month = date.getMonth();
		int year = date.getYear();
		
		if(PSS.containsKey(year) && PSS.get(year).containsKey(month) && PSS.get(year).get(month).containsKey(day))
			return PSS.get(date.getYear()).get(date.getMonth()).get(date.getDay());
		
		else
		{	
			PSS.put(year, new HashMap<>()).put(month, new HashMap<>()).put(day, new int[96]);
			return PSS.get(year).get(month).get(day);
		}
	}
	
	//Returns true if year is present;
	public boolean containsYear(Date date)
	{
		return PSS.containsKey(date.getYear());
	}
	
	
	//Updates the day array
	public boolean updateDay(Date date, int[] arr)
	{
		int day = date.getDay();
		int month = date.getMonth();
		int year = date.getYear();
		
		if(PSS.containsKey(year))
		{
			if(PSS.get(year).containsKey(month) && PSS.get(year).get(month).containsKey(day))
			{
				PSS.get(year).get(month).put(day, arr);
				return true;
			}
			
			else
			{
				PSS.get(year).put(month, new HashMap<>()).put(day, arr);
				
			}
		}
		
		else if((year >= CURRENTYEAR) && (year < 3000))
		{
			PSS.put(year, new HashMap<>()).put(month, new HashMap<>()).put(day, arr);
			return true;
		}
		return false;	
	}
	
}
	
	
	
	


