import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;

public class Calendar {
	
	private static HashMap<Integer, HashMap<Integer,int[]>> calendar;
	private static HashMap<Integer, String> months;
	private static LinkedList<String> taskList;
	private static GregorianCalendar cldr;
	
	public Calendar()
	{
		calendar = new HashMap<>();
		months = new HashMap<>();
		taskList = new LinkedList<>();
		cldr = new GregorianCalendar(2021, 1, 1);
		generateCalendar();
	}
	
	public static void main(String[] args)
	{
		GregorianCalendar cldr= new GregorianCalendar(2021, 1, 1);
		
		System.out.println(cldr.isLeapYear(2021));
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
	
	
	//Returns true if no schedule conflict and task could be added and false if not.
	private boolean addTask(Task currentTask)
	{
		
		taskList.add(currentTask.taskDescription);
		
		return true;
	}
	
	//Returns true if task was successfully removed
	private boolean removeTask(Task currentTask)
	{
		
		if(taskList.contains(currentTask.taskDescription))
		{
			taskList.remove(taskList.indexOf(currentTask.taskDescription));
			return true;
			
		}
		
		return false;
		
	}
	
	//Prints all stored task
	private void printAllTask()
	{
		
		for(int i=0; i<taskList.size(); i++)
		{
			System.out.print(i+1 + ": " + taskList.get(i));
		
		}
		
	}
	
	
}
	
	
	
	


