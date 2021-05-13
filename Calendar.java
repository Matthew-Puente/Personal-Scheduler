package basePackage;

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
	
		for(int month=1; month<= NUMBEROFMONTHS; month++)
		{			
			// Fills the Year 2021 with 12 "months"
			PSS.get(CURRENTYEAR).put(month, new HashMap<Integer, int[]>());
			
			for(int day=1; day<=daysInMonth[month-1]; day++)
			{
				PSS.get(CURRENTYEAR).get(month).put(day, new int[96]);
				
			}
		}	
	}
		
	//Returns array for specified day if day is not present a new array is generated
	public int[] getDay(Date date)
	{
		int day = date.getDay();
		int month = date.getMonth();
		int year = date.getYear();
		
		if(PSS.containsKey(year))
		{
			if(PSS.get(year).containsKey(month))
			{
				if(PSS.get(year).get(month).containsKey(day))
				{
					return PSS.get(year).get(month).get(day);

				}
				
				else
				{
					PSS.get(year).get(month).put(day, new int[96]);
					return PSS.get(year).get(month).get(day);
				}
			}
			
			else
			{
				PSS.get(year).put(month, new HashMap<>());
				PSS.get(year).get(month).put(day, new int[96]);
				return PSS.get(year).get(month).get(day);
			}
		}
		
		else
		{	
			PSS.put(year, new HashMap<>());
			PSS.get(year).put(month, new HashMap<>());
			PSS.get(year).get(month).put(day, new int[96]);
			return PSS.get(year).get(month).get(day);
		}
	}
	
	//Returns true if year is present
	public boolean containsYear(Date date)
	{
		return PSS.containsKey(date.getYear());
	}
	
	//Returns true if month is present
	public boolean containsMonth(Date date)
	{
		return PSS.get(date.getYear()).containsKey(date.getMonth());
	}
	
	//Returns true if day is present
	public boolean containsDay(Date date)
	{
		return PSS.get(date.getYear()).get(date.getMonth()).containsKey(date.getDay());
	}
	
	
	//Updates the day array
	public boolean updateDay(Date date, int[] arr)
	{
		int day = date.getDay();
		int month = date.getMonth();
		int year = date.getYear();
		
		if(PSS.containsKey(year))
		{
			if(PSS.get(year).containsKey(month)) 
			{
				PSS.get(year).get(month).put(day, arr);
				return true;
				
			}
			
			else
			{
				PSS.get(year).put(month, new HashMap<>());
				PSS.get(year).get(day).put(day, arr);
				return true;
			}
		}
			
		else if((year >= CURRENTYEAR) && (year < 3000))
		{
			PSS.put(year, new HashMap<>());
			PSS.get(year).put(month, new HashMap<>());
			PSS.get(year).get(month).put(day, arr);
			return true;
		}
		
		return false;	
	}
	
}
	
	
	
	


