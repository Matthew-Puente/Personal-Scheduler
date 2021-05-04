import java.util.HashMap;

public class Calendar {
	
	private static HashMap<Integer, HashMap<Integer,int[]>> calendar;
	private static final int MONTHS = 12;
	
	public Calendar()
	{
		calendar = new HashMap<>();
		generateCalendar();
	}
	
	
	/**
	 * This method constructs the calendar data structure upon creation of a calendar object
	 */
	private static void generateCalendar()
	{
		
		int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
		for(int i=0; i< MONTHS; i++)
		{			
			calendar.put(i+1, new HashMap<Integer, int[]>()); // Fills HashMap with month numbers and HashMap signifying days.
		}
		
		
		//This loop handles the inner hashMap which maps a day in a month to an array
		for(int i=0; i<daysInMonth.length; i++)
		{
			for(int j=1; j<=daysInMonth[i]; j++)
			{
				calendar.get(i+1).put((Integer)j, new int[96]);
				
			}
			
		}
		
	}
	
	
	//Returns array for specified day
	public int[] getDay(Date date)
	{
		return calendar.get(date.getMonth()).get(date.getDay());
	}
	
	
	//Updates the day array
	public boolean updateDay(Date date, int[] arr)
	{
		if(calendar.containsKey(date.getMonth()) && calendar.get(date.getMonth()).containsKey(date.getDay()))
		{
			calendar.get(date.getMonth()).put(date.getDay(), arr);
			return true;
			
		}
		
		return false;	
	}
	
}
	
	
	
	


