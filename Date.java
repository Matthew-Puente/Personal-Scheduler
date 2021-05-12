package basePackage;



public class Date {
	
	private int day;
	private int month;
	private int year;
	
	public Date(int month, int day,int year)
	{
		this.month = month;
		this.day = day;
		this.year = year;
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
		if(comparedDate.getDay() == day && comparedDate.getMonth() == month&& comparedDate.getYear() == year)
		{
			return true;
		}
		else 
			return false;
	}
	public boolean isAfter(Date comparedDate)
	{
		if((day>comparedDate.getDay() && month== comparedDate.getMonth()&& year == comparedDate.getYear())|| month>comparedDate.getMonth()&& year == comparedDate.getYear()||year>comparedDate.getYear())
		{
			return true;
		}
		else
			return false;
	}
	public void printDate()
	{
		System.out.println(month+"/"+day+ "/"+year);
	}

}
