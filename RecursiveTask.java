package basePackage;

public class RecursiveTask extends Task {
private char frequency;
private String name;
private String type;
private int endDate;

public RecursiveTask(char frequency,String name, String type, int startDate, int endDate, int startTime, int endTime)
{
	this.setFrequency(frequency);
	this.setName(name);
	this.setType(type);
	this.setStartDate(startDate);
	this.setStartTime(startTime);
	this.setEndTime(endTime);
	this.setEndDate(endDate);
}

public char getFrequency() {
	return frequency;
}

public void setFrequency(char frequency) {
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

public int getEndDate() {
	return endDate;
}

public void setEndDate(int endDate) {
	this.endDate = endDate;
}
}
