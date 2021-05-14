package basePackage;

public class TransientTask extends Task {
	
	private String name;
	private String type;
	
	public TransientTask(Date startDate, int startTime, int endTime, String name, String type)
	{
		this.setName(name);
		this.setType(type);
		this.setStartDate(startDate);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
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
	public void print()
	{
		String start = getRealTime(getStartTime());
		String end = getRealTime(getEndTime());
		System.out.println("On "+ getStartDate().getMonth()+"/"+getStartDate().getDay()+"/"+getStartDate().getYear()+", you have a "+ type + " appointment called " + name +" from "+start+ " to "+end);
	}
}
