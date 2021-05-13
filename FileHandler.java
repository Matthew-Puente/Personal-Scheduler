import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;


public class FileHandler{
	
	public FileHandler()
	{
		//driver code for file handler object creation
	}



	public ArrayList<Task> readFile(String filelocation) throws FileNotFoundException, IOException, ParseException{
		
		
		ArrayList<Task> tasksFromFile = new ArrayList<Task>();
		/*
		Object obj = parser.parse(new FileReader(filelocation));
		JSONArray jsonArrayFromFile = (JSONArray) jsonObject.get();
		Iterator jsonIterator
		*/
        JSONParser parser = new JSONParser();
		JSONArray jsonFileArray = (JSONArray) parser.parse(new FileReader(filelocation));
		for (Object o : jsonFileArray){
			JSONObject newTaskJSON = (JSONObject) o;

			//create startDate object with substring method:
			//String sub = s.substring(0, 10);
			//this slices everything up to the 10th index (INCLUDING 0)

			//was having issues casting from a string to an int in a single line, this presumably can be
			//shortened but my priority is to make it work first.

			if(newTaskJSON.containsKey("EndDate")){

				//begin casting the variables into their intended datatypes
				String taskName = (String) newTaskJSON.get("Name");

				String taskType = (String) newTaskJSON.get("Type");

				String startDateStr = (String) newTaskJSON.get("StartDate");
				Integer startYear = Integer.valueOf(startDateStr.substring(0,3));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 5));
				Integer startDay = Integer.valueOf(startDateStr.substring(6,7));
				Date startDate = new Date(startMonth,startDay);
	
				String startTimeStr = (String) newTaskJSON.get("startTime");
				Integer startTime = Integer.valueOf(startTimeStr);

				String endDateStr = (String) newTaskJSON.get("EndDate");
				Integer endYear = Integer.valueOf(endDateStr.substring(0,3));
				Integer endMonth = Integer.valueOf(endDateStr.substring(4, 5));
				Integer endDay = Integer.valueOf(endDateStr.substring(6,7));
				Date endDate = new Date(endMonth,endDay);

				String endTimeStr = (String) newTaskJSON.get("endTime");
				Integer endTime = Integer.valueOf(endTimeStr);

				String frequencyStr = (String) newTaskJSON.get("frequency");
				//finished casting to intended datatypes

				RecursiveTask newTask = new RecursiveTask(frequencyStr, taskName, taskType, startDate, endDate, startTime, endTime);
				tasksFromFile.add(newTask);
				//do things for recursive task
			}
			//detecting if the current task is a Transient Task
			if(newTaskJSON.containsKey("Type")){
				//do things for normal tasks

				//again casting to datatypes
				String startDateStr = (String) newTaskJSON.get("StartDate");
				Integer startYear = Integer.valueOf(startDateStr.substring(0,3));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 5));
				Integer startDay = Integer.valueOf(startDateStr.substring(6,7));
				Date startDate = new Date(startMonth,startDay);
	
				String startTimeStr = (String) newTaskJSON.get("startTime");
				Integer startTime = Integer.valueOf(startTimeStr);

				String endTimeStr = (String) newTaskJSON.get("endTime");
				Integer endTime = Integer.valueOf(endTimeStr);

				String taskName = (String) newTaskJSON.get("Name");

				String taskType = (String) newTaskJSON.get("Type");
				//end casting datatypes
				
				TransientTask newTask = new TransientTask(startDate, startTime, endTime, taskName, taskType);
				tasksFromFile.add(newTask);

			}
			//detecting if the current task is an antitask
			if(!newTaskJSON.containsKey("Type")){
				//do things for normal tasks

				//again casting to datatypes
				String startDateStr = (String) newTaskJSON.get("StartDate");
				Integer startYear = Integer.valueOf(startDateStr.substring(0,3));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 5));
				Integer startDay = Integer.valueOf(startDateStr.substring(6,7));
				Date startDate = new Date(startMonth,startDay);
	
				String startTimeStr = (String) newTaskJSON.get("startTime");
				Integer startTime = Integer.valueOf(startTimeStr);

				String endTimeStr = (String) newTaskJSON.get("endTime");
				Integer endTime = Integer.valueOf(endTimeStr);

				String taskName = (String) newTaskJSON.get("Name");
				//end casting datatypes
				
				AntiTask newTask = new AntiTask(startDate, startTime, endTime, taskName);
				tasksFromFile.add(newTask);

			}
		}
		return tasksFromFile;
	}
	public void writeFile(ArrayList<Task> listOfTasks, String filepath) throws IOException
	{
		//'filepath' is the absolute filepath (like C:\documents\options.txt)


		//incoming data is as a linked list. make sure to adhere to the pattern as written above
		//delimiter is <,> with the arrow/less then/greater than signs
		//recursive should be saved in this order:
		//int startMonth, int startDay, int endMonth, int endDay, String frequency, String name, String type, int startTime, int endTime
		Writer fileWriter = new FileWriter(filepath, true);
		for (int i = 0; i < listOfTasks.size(); i++){
			if(listOfTasks.get(i) instanceof RecursiveTask){
				//do the shit
			}
			if(listOfTasks.get(i) instanceof TransientTask){

			}
			if(listOfTasks.get(i) instanceof AntiTask){

			}

			//fileWriter.write(listOfTasks.get(i).);
		fileWriter.close();
		}
	}
}