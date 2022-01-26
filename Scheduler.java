
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
public class Scheduler {
	
	private Calendar calendar;
	private int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private PriorityQueue<Date> mostRecentRecursiveDates = new PriorityQueue<>(new DateComparator());
	private PriorityQueue<Date> datesToVerify = new PriorityQueue<>(new DateComparator());
	private LinkedList<Task> taskList;
	private LinkedList<Task> transientTaskList;
	
	public Scheduler() // creates an empty scheduler with a blank calendar
	{
		this.calendar = new Calendar();
		taskList = new LinkedList<>();
		transientTaskList = new LinkedList<>();
	}
	
	
	public boolean createTransientTask(TransientTask myTask)
	{		// Implements a TransientTask
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay(),myTask.getStartDate().getYear());
		TransientTask task = new TransientTask(tempStartDate,myTask.getStartTime(),myTask.getEndTime(),myTask.getName(),myTask.getType()); // makes new transient task to avoid changing data weirdly
		if(!task.getType().toUpperCase().equals("COURSE") && !task.getType().toUpperCase().equals("STUDY") && !task.getType().toUpperCase().equals("SLEEP") && !task.getType().toUpperCase().equals("EXERCISE") && !task.getType().toUpperCase().equals("WORK") && !task.getType().toUpperCase().equals("MEAL")&& !task.getType().toUpperCase().equals("VISIT")&& !task.getType().toUpperCase().equals("SHOPPING")&& !task.getType().toUpperCase().equals("APPOINTMENT"))
		{	// checks for valid task type
			System.out.println("Task: "+task.getName()+ " not added because of Invalid Type");
			return false;
		}
		boolean YN = verifyTask(task); // checks if task can be added to calendar
		if(YN)
		{
			addTask(task); // adds task to calendar
			transientTaskList.add(task); // adds task to print out list
			return true;
		}
		else
			return false;
	}
	private boolean verifyTask(TransientTask task)
	{   // makes sure there is space for the Transient Task
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		for(int i = startIndex;i<endIndex;i++) // iterates through time slots 
		{
			if(tempArr[i] != 0) // if the time slots aren't empty it won't verify the task
			{
				return false;
			}
		}
		return true; // otherwise it will

	}
	private void addTask(TransientTask task) // adds task to calendar
	{
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		for(int i = startIndex;i<endIndex;i++) // iterates through calendar, filling up time slots with a transient task marker
		{
			tempArr[i]= 1;
		}
		calendar.updateDay(task.getStartDate(), tempArr); // updates the calendar with new day array
		
		taskList.add(task); // adds task to taksList for writing to file
		
	}
	public boolean createRecursiveTask(RecursiveTask myTask)
	{ // Implements a RecursiveTask
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay(),myTask.getStartDate().getYear());
		Date tempEndDate = new Date(myTask.getEndDate().getMonth(),myTask.getEndDate().getDay(),myTask.getEndDate().getYear());
		RecursiveTask task = new RecursiveTask(myTask.getFrequency(),myTask.getName(),myTask.getType(),tempStartDate,tempEndDate,myTask.getStartTime(),myTask.getEndTime());
		if(!task.getType().toUpperCase().equals("COURSE") && !task.getType().toUpperCase().equals("STUDY") && !task.getType().toUpperCase().equals("SLEEP") && !task.getType().toUpperCase().equals("EXERCISE") && !task.getType().toUpperCase().equals("WORK") && !task.getType().toUpperCase().equals("MEAL")&& !task.getType().toUpperCase().equals("CLASS"))
		{  // checks for valid "Type"
			System.out.println("Task: "+task.getName()+ " not added because of Invalid Type");
			return false;
		}
		if(verifyTask(task)) // verifies if the recursive task can be added
		{
			taskList.add(task); // adds recursive task to write file list
			int size = mostRecentRecursiveDates.size(); // unchaning allocation of max size since size will get smaller as tasks are popped from list
			for(int i = 0; i<size;i++)
			{
				TransientTask newTask = new TransientTask(mostRecentRecursiveDates.poll(),task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
				//Creates a new transient task for each day the recursive task effects
				addRecursiveTask(newTask); // adds that new task, marking it as a recursive task
			}
			return true;
		}
		return false;
	}

	private boolean verifyTask(RecursiveTask task)
	{
		int frequency = getFrequency(task.getFrequency()); // gets frequency as a #
		Date newStartDate = new Date(task.getStartDate().getMonth(),task.getStartDate().getDay(),task.getStartDate().getYear());
		Date newEndDate = new Date(task.getEndDate().getMonth(),task.getEndDate().getDay(),task.getEndDate().getYear());
		PriorityQueue<Date> recursiveTask = calculateDates(frequency, newStartDate,newEndDate);  // calculates all dates that the recursive task would effect
		for(int i = 0; i<recursiveTask.size();i++)
		{
			//Makes each task into a transient task and verifies that it fits
			TransientTask newTask = new TransientTask(datesToVerify.poll(),task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
			boolean valid = verifyTask(newTask);
			if(!valid) // if any of the tasks don't fit, the verification will return false
				return false;

		}
		return true;
	}
	private void addRecursiveTask(TransientTask task)
	{
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		for(int i = startIndex;i<endIndex;i++) // adds a task to the calendar, marking the middle time slots with 2, and the end slots with 3's
		{
			if(i == startIndex||i == endIndex -1)
			{
				tempArr[i] = 3;
			}
			else 
				tempArr[i] = 2;
			
		}
		calendar.updateDay(task.getStartDate(), tempArr); // updates the calendar
		
		transientTaskList.add(task); // adds task to print-out list
	}
	
	public boolean removeTask(AntiTask myTask)
	{   // implements an AntiTask
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay(),myTask.getStartDate().getYear());
		AntiTask task = new AntiTask(tempStartDate,myTask.getStartTime(),myTask.getEndTime());
		int [] tempArr = calendar.getDay(task.getStartDate());
		if(verifyRecursiveTaskExists(task)) // checks if an instance of a recursive task even exists in the given slot
		{
			int startIndex = task.getStartTime()/15; 
			int endIndex = task.getEndTime()/15;
			for(int i = startIndex;i<endIndex;i++) // erases the task from the specified time slots
			{
				tempArr[i]= 0;
			}
			calendar.updateDay(task.getStartDate(), tempArr); // updates calendar			
			taskList.add(task); // adds anti-task to write list
			for(int i =0;i<transientTaskList.size();i++) // removes task from print out list
			{
				if(transientTaskList.get(i).getStartDate().equals(task.getStartDate())&&transientTaskList.get(i).getStartTime() == task.getStartTime()&& transientTaskList.get(i).getEndTime() == task.getEndTime())
				{
					transientTaskList.remove(i);
					break;
				}
			}
			
			return true;
		}
		return false;
	}
	
	private boolean verifyRecursiveTaskExists(AntiTask task) { // makes sure the instance of a recursive task exists at a certain date
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		for(int i = startIndex;i<endIndex;i++) // makes sure that the ends of the task are 3 and the middle is all 2's, otherwise the recursive task is incorrect. 
		{
			if((i == startIndex|| i== endIndex-1) && tempArr[i] != 3)
			{
				return false;
			}
			if(i != startIndex && tempArr[i] != 2 && i != endIndex-1)
			{
				return false;
			}
		}
		return true;
		
		
	}
	

	private int getFrequency(String frequency) // returns numerical values for the frequency depending on the input
	{
		String tempFrequency = frequency.toUpperCase();
		if(tempFrequency.equals("D") )
		{
			return 1;
		}
		if(tempFrequency.contentEquals("W"))
		{
			return 7;
		}
		if(tempFrequency.contentEquals("M"))
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
	private PriorityQueue<Date> calculateDates(int frequency, Date startDate,Date endDate) // calculates the dates that a recursive task effects
	{
		Date currentDate = startDate;
		mostRecentRecursiveDates = new PriorityQueue<>(new DateComparator());
		datesToVerify = new PriorityQueue<>(new DateComparator());
		if(frequency != 0) { 
			if(frequency == 1) //if frequency is in days
			{
				while(!currentDate.equals(endDate)&&!currentDate.isAfter(endDate)) // as long as the date we are looking at isn't equal to or after the end date
				{
					if(currentDate.getYear()==endDate.getYear()) // if the years are the same
					{
						if(currentDate.getMonth()<endDate.getMonth()) // if current date is in a previous month
						{   // fills in all days to the end of the month
							for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
							{
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
							currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
							currentDate.setDay(1); // reset days
						}
						else
						{	// fills in all days up to the end day in the endDate month. 
							for(int i = currentDate.getDay();i<=endDate.getDay();i += frequency)
							{
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
						}
					}
					else // if the year is different
					{
						if(currentDate.getMonth()<13) // fills up all the months in the year until it exceeds december
						{
							for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
							{	// checks every day in a month
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
							currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
							currentDate.setDay(1); // reset days
						}
						else //once it's filled up to december 31st, starts a new year, and resets to January 1st
						{
							currentDate.setYear(currentDate.getYear()+1);
							currentDate.setDay(1);
							currentDate.setMonth(1);
						}
					}
				}
			}
			else if(frequency == 7) // checks if frequency is in weeks
			{
				while(!currentDate.equals(endDate)&& !currentDate.isAfter(endDate)) // so long as the current date doesn't equal or exceed the end date
				{
					if(currentDate.getYear()==endDate.getYear()) // if currentDate and endDate years are the same
					{
						if(currentDate.getMonth()<endDate.getMonth()) // if we're in a previous month
						{
							for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
							{   // adds all the dates in the month from current date to the end of the month
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
							currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
							currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()-1]); //sets currentDate to the amount of days into the next month it should be at
						}
						else // if we're in the same month as the end date
						{
							for(int i = currentDate.getDay();i<=endDate.getDay();i += frequency)
							{	// adds all the dates from current date to the endDate.
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
						}
					}
					else // if year is different
					{
						while(currentDate.getMonth()<13) // until the year increase
						{
							for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
							{	// goes through a month adding all the appropriate dates
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
							currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()-1]); // gets days into the next month;
							currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
	
						}
							currentDate.setYear(currentDate.getYear()+1); // once new year is reached, we increment the year
							currentDate.setMonth(1); // reset the months to January
							// Keep carry over date into the next month calculated previously
					}
					
				}
			}
		}
		else { //If the frequency is in months
			while(!currentDate.equals(endDate)&& !currentDate.isAfter(endDate)) // as long as the currentDate doesn't equal or exceed the endDate
			{		
					if(currentDate.getDay()>= daysInMonth[(currentDate.getMonth()-1)]) // if the day we have doesn't exist in the month we are checking
					{	// assuming this is because this month is shorter than the last
						currentDate.setDay(daysInMonth[(currentDate.getMonth()-1)]);  // we lower the current day to the largest day available in the month
					}
					mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
					datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
					currentDate.setMonth(currentDate.getMonth()+1);// increments month by 1
					if(currentDate.getMonth() == 13)// if month exceeds the year, we increment the year and reset the months
					{
						currentDate.setYear(currentDate.getYear()+1);
						currentDate.setMonth(1);
					}

			}
		}
		if(currentDate.equals(endDate)) // makes sure endDate is in the recursive dates since the while loop would leave before adding it if it fell on the exact date. 
		{
			mostRecentRecursiveDates.add(endDate);
			datesToVerify.add(endDate);
		}
		return mostRecentRecursiveDates;
	}
	
	//.print() for each task in transientTaskList
	public void printAllTask()
	{
		Collections.sort(transientTaskList, new SortByDate());
		
		for(Task task : transientTaskList)
			task.print();
	}
	public LinkedList<Task> getLinkedList()
	{
		return taskList;
	}
	
}


class SortByDate implements Comparator<Task>
{
	// sorts the transientTaskList by dates so that the printout is mostly in order (however doesn't sort by times)
	public int compare(Task a, Task b)
	{
		if(a.getStartDate().getYear() == b.getStartDate().getYear()) {
			if(a.getStartDate().getMonth() == b.getStartDate().getMonth())
			{
				if(a.getStartDate().getDay() == b.getStartDate().getDay())
				{
					return 1;
				}
				
				else
				{
					return a.getStartDate().getDay() - b.getStartDate().getDay();
				}
			}
			
			return a.getStartDate().getMonth() - b.getStartDate().getMonth();
		}
		else
			return a.getStartDate().getYear() - b.getStartDate().getYear();
	}

	
}

class DateComparator implements Comparator<Date> { // sorts Date list by earliest dates
    public int compare(Date temp0, Date temp1) {
        if (temp0.isAfter(temp1))
             return 1;
         else if(temp1.isAfter(temp0))
             return -1;
         return 0;
     }

}
//