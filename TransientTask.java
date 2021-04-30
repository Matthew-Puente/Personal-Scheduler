package basePackage;

public class TransientTask extends Task {
	
	private String name;
	private String type;
	
	public TransientTask(int startDate, int startTime, int endTime, String name, String type)
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
}
