

public class Date {
	
	private int day;
	private int month;
	private int year;
	
	public Date(int month, int day)
	{
		this.month = month;
		this.day = day;
	}
	
	public void setDay(int day)
	{
		this.day =day;
	}
	
	public void setMonth(int month)
	{
		this.month = month;
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public int getMonth()
	{
		return month;	
	}
	
	public int getYear()
	{
		return year;
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
	public boolean isAfter(Date comparedDate)
	{
		if((day>comparedDate.getDay() && month== comparedDate.getMonth())|| month>comparedDate.getMonth())
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
