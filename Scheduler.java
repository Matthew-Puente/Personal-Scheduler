
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
	
	public Scheduler()
	{
		this.calendar = new Calendar();
		taskList = new LinkedList<>();
		transientTaskList = new LinkedList<>();
	}
	
	
	public boolean createTransientTask(TransientTask myTask)
	{		// Implements a TransientTask
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay(),myTask.getStartDate().getYear());
		TransientTask task = new TransientTask(tempStartDate,myTask.getStartTime(),myTask.getEndTime(),myTask.getName(),myTask.getType());
		if(!task.getType().toUpperCase().equals("COURSE") && !task.getType().toUpperCase().equals("STUDY") && !task.getType().toUpperCase().equals("SLEEP") && !task.getType().toUpperCase().equals("EXERCISE") && !task.getType().toUpperCase().equals("WORK") && !task.getType().toUpperCase().equals("MEAL")&& !task.getType().toUpperCase().equals("VISIT")&& !task.getType().toUpperCase().equals("SHOPPING")&& !task.getType().toUpperCase().equals("APPOINTMENT"))
		{
			System.out.println("Task: "+task.getName()+ " not added because of Invalid Type");
			return false;
		}
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
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay(),myTask.getStartDate().getYear());
		Date tempEndDate = new Date(myTask.getEndDate().getMonth(),myTask.getEndDate().getDay(),myTask.getEndDate().getYear());
		RecursiveTask task = new RecursiveTask(myTask.getFrequency(),myTask.getName(),myTask.getType(),tempStartDate,tempEndDate,myTask.getStartTime(),myTask.getEndTime());
		if(!task.getType().toUpperCase().equals("COURSE") && !task.getType().toUpperCase().equals("STUDY") && !task.getType().toUpperCase().equals("SLEEP") && !task.getType().toUpperCase().equals("EXERCISE") && !task.getType().toUpperCase().equals("WORK") && !task.getType().toUpperCase().equals("MEAL")&& !task.getType().toUpperCase().equals("CLASS"))
		{
			System.out.println("Task: "+task.getName()+ " not added because of Invalid Type");
			return false;
		}
		if(verifyTask(task))
		{
			taskList.add(task);
			int size = mostRecentRecursiveDates.size();
			for(int i = 0; i<size;i++)
			{
				TransientTask newTask = new TransientTask(mostRecentRecursiveDates.poll(),task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
				//System.out.println("The Date is:"+mostRecentRecursiveDates[counter].getMonth()+"/"+mostRecentRecursiveDates[counter].getDay());
				addRecursiveTask(newTask);
			}
			return true;
		}
		return false;
	}

	private boolean verifyTask(RecursiveTask task)
	{
		int frequency = getFrequency(task.getFrequency());
		Date newStartDate = new Date(task.getStartDate().getMonth(),task.getStartDate().getDay(),task.getStartDate().getYear());
		Date newEndDate = new Date(task.getEndDate().getMonth(),task.getEndDate().getDay(),task.getEndDate().getYear());
		PriorityQueue<Date> recursiveTask = calculateDates(frequency, newStartDate,newEndDate);
		for(int i = 0; i<recursiveTask.size();i++)
		{
			//System.out.println("Hello");
			TransientTask newTask = new TransientTask(datesToVerify.poll(),task.getStartTime(),task.getEndTime(),task.getName(),task.getType());
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
		Date tempStartDate = new Date(myTask.getStartDate().getMonth(),myTask.getStartDate().getDay(),myTask.getStartDate().getYear());
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
	private PriorityQueue<Date> calculateDates(int frequency, Date startDate,Date endDate)
	{
		Date currentDate = startDate;
		//System.out.println("Hi");
		mostRecentRecursiveDates = new PriorityQueue<>(new DateComparator());
		datesToVerify = new PriorityQueue<>(new DateComparator());
		if(frequency != 0) { // only worries about days when frequency is in weeks or days, not months
			if(frequency == 1)
			{
				while(!currentDate.equals(endDate)&&!currentDate.isAfter(endDate))
				{
					if(currentDate.getYear()==endDate.getYear())
					{
						if(currentDate.getMonth()<endDate.getMonth())
						{
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
						{
							for(int i = currentDate.getDay();i<=endDate.getDay();i += frequency)
							{
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
						}
					}
					else
					{
						if(currentDate.getMonth()<13)
						{
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
						{
							currentDate.setYear(currentDate.getYear()+1);
							currentDate.setDay(1);
							currentDate.setMonth(1);
						}
					}
					//currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()]); // should get days into the next month;
				}
			}
			else if(frequency == 7)
			{
				while(!currentDate.equals(endDate)&& !currentDate.isAfter(endDate))
				{
					if(currentDate.getYear()==endDate.getYear())
					{
						if(currentDate.getMonth()<endDate.getMonth())
						{
							for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
							{
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
							currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
							currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()-1]); // should get days into the next month;
						}
						else
						{
							for(int i = currentDate.getDay();i<=endDate.getDay();i += frequency)
							{
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
						}
					}
					else
					{
						while(currentDate.getMonth()<13)
						{
							for(int i = currentDate.getDay();i<=daysInMonth[currentDate.getMonth()-1];i += frequency)
							{
								mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
								currentDate.setDay(currentDate.getDay()+frequency);
							}
							currentDate.setDay(currentDate.getDay()% daysInMonth[currentDate.getMonth()-1]); // should get days into the next month;
							currentDate.setMonth(currentDate.getMonth()+1);//increments month by 1
	
						}
							currentDate.setYear(currentDate.getYear()+1);
							currentDate.setMonth(1);
					}
					
				}
			}
		}
		else {
			while(!currentDate.equals(endDate)&& !currentDate.isAfter(endDate))
			{
					if(currentDate.getDay()>= daysInMonth[(currentDate.getMonth()-1)]) // if the day we have doesn't exist in the next month
					{
						currentDate.setDay(daysInMonth[(currentDate.getMonth()-1)]);
					}
					mostRecentRecursiveDates.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
					datesToVerify.add(new Date(currentDate.getMonth(),currentDate.getDay(),currentDate.getYear()));
					currentDate.setMonth(currentDate.getMonth()+1);// increments month by 1
					if(currentDate.getMonth() == 13)
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

class DateComparator implements Comparator<Date> {
    public int compare(Date temp0, Date temp1) {
        if (temp0.isAfter(temp1))
             return 1;
         else if(temp1.isAfter(temp0))
             return -1;
         return 0;
     }

}
