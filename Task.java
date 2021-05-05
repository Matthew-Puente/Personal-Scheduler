package basePackage;

public class Task {
private Date startDate;
private int startTime;
private int endTime;
public Date getStartDate() {
	return startDate;
}
public void setStartDate(Date startDate) {
	this.startDate = startDate;
}
public int getStartTime() {
	return startTime;
}
public void setStartTime(int startTime) {
	this.startTime = startTime;
}
public int getEndTime() {
	return endTime;
}
public void setEndTime(int endTime) {
	this.endTime = endTime;
}
public void print()
{
	String start = getRealTime(startTime);
	String end = getRealTime(endTime);
	System.out.println("On "+ startDate.getMonth()+"/"+startDate.getDay()+", you have an appointment from "+start+ " to "+end);
}
public String getRealTime(int time)
{
	if(time/60 >12||time==720)
	{	if(time == 720)
			return"12:00 PM";
		if(time == 1440)
			return "12:00AM";
		if(time>720 && time<=765)
			return"12:"+time%60+"PM";
		else if(time%60 == 0)
			return (time/60)%12 +":"+ time%60 + "0PM";
		return (time/60)%12 +":"+ time%60 + "PM";
	}
	else
	{
		if(time == 0)
			return "12:00 AM";
		if(time >0 && time <=45)
			return "12:" + time%60+"AM";
		if(time%60 == 0)
			return (time/60)%12 +":"+ time%60 + "0AM";
		return (time/60)%12 +":"+ time%60 + "AM";
	}
}
}
