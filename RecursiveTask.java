package basePackage;

public class RecursiveTask extends Task {
private String frequency;
private String name;
private String type;
private Date endDate;

public RecursiveTask(String frequency,String name, String type, Date startDate, Date endDate, int startTime, int endTime)
{
	this.setFrequency(frequency);
	this.setName(name);
	this.setType(type);
	this.setStartDate(startDate);
	this.setStartTime(startTime);
	this.setEndTime(endTime);
	this.setEndDate(endDate);
}

public String getFrequency() {
	return frequency;
}

public void setFrequency(String frequency) {
	this.frequency = frequency;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public Date getEndDate() {
	return endDate;
}

public void setEndDate(Date endDate2) {
	this.endDate = endDate2;
}
}
