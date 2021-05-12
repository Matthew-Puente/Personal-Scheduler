import java.util.HashMap;

public class Calendar {
	
	private static HashMap<Integer, HashMap<Integer, HashMap<Integer,int[]>>> PSS;
	private static final int MONTHS = 12;
	
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
		PSS.put(2021, new HashMap<>());
	
		for(int i=0; i< MONTHS; i++)
		{			
			// Fills the Year 2021 with 12 "months"
			PSS.get(2021).put(i+1, new HashMap<Integer, int[]>());
			
			for(int j=1; j<=daysInMonth[i]; j++)
			{
				PSS.get(2021).get(i+1).put((Integer)j, new int[96]);
				
			}
		}	
	}
		
	
	
	//Returns array for specified day
	public int[] getDay(Date date)
	{
		return PSS.get(date.getYear()).get(date.getMonth()).get(date.getDay());
	}
	
	//Returns true if year is present;
	public boolean containsYear(Date date)
	{
		return PSS.containsKey(date.getYear());
	}
	
	
	//Updates the day array
	public boolean updateDay(Date date, int[] arr)
	{
		if(PSS.containsKey(date.getYear()))
		{
			if(PSS.get(date.getYear()).containsKey(date.getMonth()) && PSS.get(date.getYear()).get(date.getMonth()).containsKey(date.getDay()))
			{
				PSS.get(date.getYear()).get(date.getMonth()).put(date.getDay(), arr);
				return true;
				
			}
			
		}
		
		
		return false;	
	}
	
}
	
	
	
	


