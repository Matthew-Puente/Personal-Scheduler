package basePackage;

public class Scheduler {
	private Calendar calendar;
	
	public Scheduler(Calendar calendar)
	{
		this.calendar = calendar;
	}
	public boolean checkCalendar(TransientTask task)
	{
		//Not Implemented Yet
		return false;
	}
	public void createTask(int startDate, int startTime, int endTime,String name, String type)
	{
		TransientTask task = new TransientTask(startDate,startTime,endTime,name,type);
		// implement addition to calendar
	}
	public void createTask(char frequency, String name, String type,int startDate, int startTime, int endDate, int endTime)
	{
		RecursiveTask task = new RecursiveTask(frequency,name,type,startDate,endDate,startTime,endTime);
		// implement addition to calendar
	}
	public void printSchedule()
	{
		//Not Implemented Yet
	}

}
