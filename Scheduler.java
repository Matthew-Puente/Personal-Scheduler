package basePackage;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Scheduler {
	
	private Calendar calendar;
	private int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private Date[] mostRecentRecursiveDates;
	private LinkedList<Task> taskList;
	private LinkedList<Task> transientTaskList;
	
	public Scheduler()
	{
		this.calendar = new Calendar();
		taskList = new LinkedList<>();
		transientTaskList = new LinkedList<>();
	}
	
	
	public boolean createTransientTask(TransientTask myTask)
	{		// Implements a TransientTask
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay());
		TransientTask task = new TransientTask(tempStartDate,myTask.getStartTime(),myTask.getEndTime(),myTask.getName(),myTask.getType());
		boolean YN = verifyTask(task);
		if(YN)
		{
			addTask(task);
			transientTaskList.add(task);
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
		/*System.out.println("Day after transient task added");
		for(int i = 0; i <tempArr.length;i++)
		{
			System.out.print(tempArr[i]);
		}
		System.out.println();
		*/
		calendar.updateDay(task.getStartDate(), tempArr);
		
		taskList.add(task);
		
	}
	public boolean createRecursiveTask(RecursiveTask myTask)
	{ // Implements a RecursiveTask
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay());
		Date tempEndDate = new Date(myTask.getEndDate().getMonth(),myTask.getEndDate().getDay());
		RecursiveTask task = new RecursiveTask(myTask.getFrequency(),myTask.getName(),myTask.getType(),tempStartDate,tempEndDate,myTask.getStartTime(),myTask.getEndTime());
		int counter = 0;
		if(verifyTask(task))
		{
			taskList.add(task);
			
			for(int i = 0; i<mostRecentRecursiveDates.length;i++)
			{
				TransientTask newTask = new TransientTask(mostRecentRecursiveDates[counter],task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
				//System.out.println("The Date is:"+mostRecentRecursiveDates[counter].getMonth()+"/"+mostRecentRecursiveDates[counter].getDay());
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
		for(int i = 0; i<recursiveTask.length;i++)
		{
			//System.out.println("Hello");
			TransientTask newTask = new TransientTask(recursiveTask[i],task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
			boolean valid = verifyTask(newTask);
			if(!valid)
				return false;

		}
		return true;
	}
	private void addRecursiveTask(TransientTask task)
	{
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		/*
		System.out.println("Day before recursive task added");
		for(int i = 0; i <tempArr.length;i++)
		{
			System.out.print(tempArr[i]);
		}
		System.out.println();
		*/
		for(int i = startIndex;i<endIndex;i++)
		{
			tempArr[i]= 2;
		}
		/*
		System.out.println("Day after recursive Task Added");
		for(int i = 0; i <tempArr.length;i++)
		{
			System.out.print(tempArr[i]);
		}
		System.out.println();
		*/
		calendar.updateDay(task.getStartDate(), tempArr);
		
		transientTaskList.add(task);
	}
	
	public boolean removeTask(AntiTask myTask)
	{   // implements an AntiTask
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay());
		AntiTask task = new AntiTask(tempStartDate,myTask.getStartTime(),myTask.getEndTime());
		int [] tempArr = calendar.getDay(task.getStartDate());
		if(verifyRecursiveTaskExists(task))
		{
			/*
			System.out.println("Day before recursive task removed");
			for(int i = 0; i <tempArr.length;i++)
			{
				System.out.print(tempArr[i]);
			}
			System.out.println();
			*/
			int startIndex = task.getStartTime()/15;
			int endIndex = task.getEndTime()/15;
			for(int i = startIndex;i<endIndex;i++)
			{
				tempArr[i]= 0;
			}
			/*
			System.out.println("Day after recursive task removed");
			for(int i = 0; i <tempArr.length;i++)
			{
				System.out.print(tempArr[i]);
			}
			*/
			calendar.updateDay(task.getStartDate(), tempArr);			
			taskList.add(task);
			for(int i =0;i<transientTaskList.size();i++)
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
	
	private boolean verifyRecursiveTaskExists(AntiTask task) {
		int [] tempArr = calendar.getDay(task.getStartDate());
		int startIndex = task.getStartTime()/15;
		int endIndex = task.getEndTime()/15;
		/*for(int i = 0; i <tempArr.length;i++)
		{
			System.out.print(tempArr[i]);
		}
		*/
		for(int i = startIndex;i<endIndex;i++)
		{
			//System.out.print(tempArr[i]+" ");
			if(tempArr[i] != 2)
			{
				return false;
			}
		}
		return true;
		
		
	}
	

	private int getFrequency(String frequency)
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
	private Date[] calculateDates(int frequency, Date startDate,Date endDate)
	{
		int estmNumOfDays = 0;
		for(int i = startDate.getMonth();i<= endDate.getMonth();i++)
		{
			estmNumOfDays += daysInMonth[i-1]; // gets approx. # of days between start and end date
			if(i == startDate.getMonth())
			{
				estmNumOfDays -= startDate.getDay();
			}
			if(i==endDate.getMonth())
			{
				estmNumOfDays-= daysInMonth[i-1]-endDate.getDay();
			}
		}
		if(frequency!= 0)
			 mostRecentRecursiveDates = new Date[(estmNumOfDays/frequency)+1];
		else
			mostRecentRecursiveDates = new Date[endDate.getMonth()-startDate.getMonth()+1];
		Date currentDate = startDate;
		int counter = 0;
		//System.out.println("Hi");
		if(frequency != 0) { // only worries about days when frequency is in weeks or days, not months
			if(frequency == 1)
			{
				while(!currentDate.equals(endDate)&&!currentDate.isAfter(endDate))
				{
					if(currentDate.getMonth()<endDate.getMonth())
					{
						for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
						{
							mostRecentRecursiveDates[counter] = new Date(currentDate.getMonth(),currentDate.getDay());
							counter++;
							currentDate.setDay(currentDate.getDay()+frequency);
						}
						currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
						currentDate.setDay(1); // reset days
					}
					else
					{
						for(int i = currentDate.getDay();i<=endDate.getDay();i += frequency)
						{
							mostRecentRecursiveDates[counter] = new Date(currentDate.getMonth(),currentDate.getDay());
							counter++;
							currentDate.setDay(currentDate.getDay()+frequency);
						}
					}
					//currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()]); // should get days into the next month;
				}
			}
			else if(frequency == 7)
			{
				while(!currentDate.equals(endDate)&& !currentDate.isAfter(endDate))
				{
					if(currentDate.getMonth()<endDate.getMonth())
					{
						for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
						{
							mostRecentRecursiveDates[counter] = new Date(currentDate.getMonth(),currentDate.getDay());
							counter++;
							currentDate.setDay(currentDate.getDay()+frequency);
						}
						currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
						currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()-1]); // should get days into the next month;
					}
					else
					{
						for(int i = currentDate.getDay();i<=endDate.getDay();i += frequency)
						{
							mostRecentRecursiveDates[counter] = new Date(currentDate.getMonth(),currentDate.getDay());
							counter++;
							currentDate.setDay(currentDate.getDay()+frequency);
						}
					}
					
				}
			}
		}
		else {
			while(!currentDate.equals(endDate)&& !currentDate.isAfter(endDate))
			{
				if(currentDate.getDay()>= daysInMonth[(currentDate.getMonth())]) // if the day we have doesn't exist in the next month
				{
					currentDate.setDay(daysInMonth[(currentDate.getMonth())]);
				}
				mostRecentRecursiveDates[counter] = new Date(currentDate.getMonth(),currentDate.getDay());
				counter++;
			
				currentDate.setMonth(currentDate.getMonth()+1);// increments month by 1
				if(currentDate.getMonth() >endDate.getMonth()||currentDate.getMonth() == endDate.getMonth() && currentDate.getDay()>endDate.getDay())
				{
					break;
				}
			}
		}
		if(currentDate.equals(endDate)) // makes sure endDate is in the recursive dates since the while loop would leave before adding it if it fell on the exact date. 
		{
			mostRecentRecursiveDates[counter] = endDate;
		}
		/*for(int i = 0; i <mostRecentRecursiveDates.length;i++)
		{
			if(mostRecentRecursiveDates[i]!=null)
				System.out.print(mostRecentRecursiveDates[i].getMonth()+ "/"+ mostRecentRecursiveDates[i].getDay()+" ");
			else
				System.out.print(" null");
		}
		*/
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
	
	public int compare(Task a, Task b)
	{
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

	
}


