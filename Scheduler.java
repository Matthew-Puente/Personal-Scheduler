package basePackage;

public class Scheduler {
	private Calendar calendar;
	private int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private Date[] mostRecentRecursiveDates;
	
	public Scheduler()
	{
		this.calendar = new Calendar();
	}
	public boolean createTransientTask(Date startDate, int startTime, int endTime,String name, String type)
	{		// Implements a TransientTask
		TransientTask task = new TransientTask(startDate,startTime,endTime,name,type);
		boolean YN = verifyTask(task);
		if(YN)
		{
			addTask(task);
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
		
		for(int i = startIndex;i<endIndex;i++)
		{
			if(tempArr[i] != 0)
			{
				return false;
			}
		}
		return true;

	}
	private void addTask(TransientTask task)
	{
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		for(int i = startIndex;i<endIndex;i++)
		{
			tempArr[i]= 1;
		}
		calendar.updateDay(task.getStartDate(), tempArr);
		
	}
	public boolean createRecursiveTask(String frequency, String name, String type,Date startDate, int startTime, Date endDate, int endTime)
	{ // Implements a RecursiveTask
		RecursiveTask task = new RecursiveTask(frequency,name,type,startDate,endDate,startTime,endTime);
		int counter = 0;
		if(verifyTask(task))
		{
			while(mostRecentRecursiveDates[counter]!=null)
			{
				TransientTask newTask = new TransientTask(mostRecentRecursiveDates[counter],task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
				addRecursiveTask(newTask);
				counter++;
			}
			return true;
		}
		return false;
	}

	private boolean verifyTask(RecursiveTask task)
	{
		int frequency = getFrequency(task.getFrequency());
		Date [] recursiveTask = calculateDates(frequency, task.getStartDate(),task.getEndDate());
		int counter = 0;
		while(recursiveTask[counter]!=null)
		{
			//System.out.println("Hello");
			TransientTask newTask = new TransientTask(recursiveTask[counter],task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
			boolean valid = verifyTask(newTask);
			if(!valid)
				return false;
			counter++;
		}
		return true;
	}
	private void addRecursiveTask(TransientTask task)
	{
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		for(int i = startIndex;i<endIndex;i++)
		{
			tempArr[i]= 2;
		}
		calendar.updateDay(task.getStartDate(), tempArr);
	}
	public boolean removeTask(Date startdate, int startTime, int endTime)
	{   // implements an AntiTask
		AntiTask task = new AntiTask(startdate,startTime,endTime);
		if(verifyRecursiveTaskExists(task))
		{
			int [] tempArr = calendar.getDay(task.getStartDate());
			int startIndex = task.getStartTime()/15;
			int endIndex = task.getEndTime()/15;
			for(int i = startIndex;i<endIndex;i++)
			{
				tempArr[i]= 0;
			}
			calendar.updateDay(task.getStartDate(), tempArr);
			return true;
		}
		return false;
	}
	private boolean verifyRecursiveTaskExists(AntiTask task) {
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		
		for(int i = startIndex;i<endIndex;i++)
		{
			//System.out.print(tempArr[i]+" ");
			if(tempArr[i] != 2)
			{
				//return false;
			}
		}
		return true;
		
		
	}
	public void printSchedule()
	{
		calendar.printAllTask();
	}	
	private int getFrequency(String frequency)
	{
		if(frequency.equals("D") )
		{
			return 1;
		}
		if(frequency.contentEquals("W"))
		{
			return 7;
		}
		if(frequency.contentEquals("M"))
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
	private Date[] calculateDates(int frequency, Date startDate,Date endDate)
	{
		int estmNumOfDays = 0;
		for(int i = startDate.getMonth();i<= endDate.getMonth();i++)
		{
			estmNumOfDays += daysInMonth[i];
		}
		if(frequency!= 0)
			 mostRecentRecursiveDates = new Date[estmNumOfDays/frequency];
		else
			mostRecentRecursiveDates = new Date[12];
		Date currentDate = startDate;
		int counter = 0;
		while(!currentDate.equals(endDate))
		{
			//System.out.println("Hi");
			if(frequency != 0) { // only worries about days when frequency is in weeks or days, not months
				for(int i = currentDate.getDay();i<daysInMonth[startDate.getMonth()];i += frequency)
				{
					mostRecentRecursiveDates[counter] = currentDate;
					counter++;
					currentDate.setDay(currentDate.getDay()+frequency);
				}
				currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()]); // should get days into the next month;
			}
			else // if frequency is in months
			{
				if(currentDate.getDay()>= daysInMonth[(currentDate.getMonth()+1)]) // if the day we have doesn't exist in the next month
				{
					currentDate.setDay(daysInMonth[(currentDate.getMonth()+1)]);
				}
				mostRecentRecursiveDates[counter] = currentDate;
				counter++;
			}
			currentDate.setMonth(currentDate.getMonth()+1); // increments month by 1
			if(currentDate.getMonth() >endDate.getMonth()||currentDate.getMonth() == endDate.getMonth() && currentDate.getDay()>endDate.getDay())
			{
				break;
			}
		}
		if(currentDate.equals(endDate)) // makes sure endDate is in the recursive dates since the while loop would leave before adding it if it fell on the exact date. 
		{
			mostRecentRecursiveDates[counter] = endDate;
		}
		return mostRecentRecursiveDates;
	}

}
