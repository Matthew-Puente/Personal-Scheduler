package basePackage;

import java.util.HashMap;
import java.util.LinkedList;

public class Calendar {
	
	private  static HashMap<Integer, HashMap<Integer,int[]>> calendar;
	private  static HashMap<Integer, String> months;
	private  static LinkedList<String> taskList;
	
	public Calendar()
	{
		calendar = new HashMap<>();
		months = new HashMap<>();
		taskList = new LinkedList<>();
		generateCalendar();
	}
	
	
	/**
	 * This method constructs the calendar data structure upon creation of a calendar object
	 */
	private static void generateCalendar()
	{

		String[] monthsArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		
		int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
		for(int i=0; i< monthsArray.length; i++)
		{
			months.put(i+1, monthsArray[i]); //Fills HashMap with number and corresponding month
			
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
	public void printDay(Date date)
	{
		int [] day = calendar.get(date.getMonth()).get(date.getDay());
		for(int i = 0; i<day.length;i++)
		{
			System.out.print(day[i]);
		}
		System.out.println();
	}
		
	//Prints all stored task
	public void printAllTask()
	{
		
		for(int i=0; i<taskList.size(); i++)
		{
			System.out.print(i+1 + ": " + taskList.get(i));
		
		}
		
	}
	
	
}
	
	
	
	


