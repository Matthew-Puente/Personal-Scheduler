package basePackage;

public class Date {
	
	private int month;
	private int day;
	
	public Date(int month, int day)
	{
		this.month = month;
		this.day = day;
	}
	public void setMonth(int month)
	{
		this.month = month;
	}
	public void setDay(int day)
	{
		this.day =day;
	}
	public int getMonth()
	{
		return month;
		
	}
	
	public int getDay()
	{
		return day;
	}
	
	public boolean equals(Date comparedDate)
	{
		if(comparedDate.getDay() == day && comparedDate.getMonth() == month)
		{
			return true;
		}
		else 
			return false;
	}
	public void printDate()
	{
		System.out.println(month+"/"+day);
	}

}
