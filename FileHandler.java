import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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



	public void readFile(String filelocation) throws FileNotFoundException, IOException, ParseException{
		
		
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
			String startDateStr = (String) newTaskJSON.get("StartDate");
			Integer startYear = Integer.valueOf(startDateStr.substring(0,3));
			Integer startMonth = Integer.valueOf(startDateStr.substring(4, 5));
			Integer startDay = Integer.valueOf(startDateStr.substring(6,7));
			Date startDate = new Date(startMonth,startDay, startYear);

			String startTimeStr = (String) newTaskJSON.get("startTime");
			Integer startTime = Integer.valueOf(startTimeStr);

			//begin converting duration to time, then add it to start time to get end time.
			String durationStr = (String) newTaskJSON.get("Duration");
			String[] durationTimeSplit = durationStr.split("\\.");
			double decimal = Double.parseDouble(durationTimeSplit[0]);
			double real = Double.parseDouble(durationStr);
			double fraction = real - decimal;
			//multiply by 60 minutes in an hour, reusing fraction
			fraction = fraction * 60;
			//using up even more memory since I can't figure out how to single line them (I really tried)
			int fractionOfHour = (int) fraction;
			int realHour = (int) real;
			//put it all together with simple addition
			Integer endTime = (Integer) startTime + realHour + fractionOfHour;

			//common parameters end here


			//this is if it is a recursive task
			if(newTaskJSON.containsKey("EndDate")){

				//begin casting the variables into their intended datatypes
				String taskName = (String) newTaskJSON.get("Name");

				String taskType = (String) newTaskJSON.get("Type");

				/**
				 * Begin old startDate getter code, moved above.
				String startDateStr = (String) newTaskJSON.get("StartDate");
				Integer startYear = Integer.valueOf(startDateStr.substring(0,3));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 5));
				Integer startDay = Integer.valueOf(startDateStr.substring(6,7));
				Date startDate = new Date(startMonth,startDay, startYear);
				*/

				/**
				String startTimeStr = (String) newTaskJSON.get("startTime");
				Integer startTime = Integer.valueOf(startTimeStr);
				*/

				String endDateStr = (String) newTaskJSON.get("EndDate");
				Integer endYear = Integer.valueOf(endDateStr.substring(0,3));
				Integer endMonth = Integer.valueOf(endDateStr.substring(4, 5));
				Integer endDay = Integer.valueOf(endDateStr.substring(6,7));
				Date endDate = new Date(endMonth, endDay, endYear);

				//begin converting duration to time, then add it to start time to get end time.
				/**
				String durationStr = (String) newTaskJSON.get("Duration");
				String[] durationTimeSplit = durationStr.split("\\.");
				double decimal = Double.parseDouble(durationTimeSplit[0]);
				double real = Double.parseDouble(durationStr);
				double fraction = real - decimal;
				//multiply by 60 minutes in an hour, reusing fraction
				fraction = fraction * 60;
				//using up even more memory since I can't figure out how to single line them (I really tried)
				int fractionOfHour = (int) fraction;
				int realHour = (int) real;
				//put it all together with simple addition
				Integer endTime = (Integer) startTime + realHour + fractionOfHour;
				*/

				


				String frequencyStr = (String) newTaskJSON.get("frequency");
				//finished casting to intended datatypes

				RecursiveTask newTask = new RecursiveTask(frequencyStr, taskName, taskType, startDate, endDate, startTime, endTime);
				addRecursiveTask(newTask);
				continue;
				//do things for recursive task
			}
			//detecting if the current task is a Transient Task
			if(newTaskJSON.containsKey("Type")){
				//do things for normal tasks

				//again casting to datatypes
				/**
				String startDateStr = (String) newTaskJSON.get("StartDate");
				Integer startYear = Integer.valueOf(startDateStr.substring(0,3));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 5));
				Integer startDay = Integer.valueOf(startDateStr.substring(6,7));
				Date startDate = new Date(startMonth, startDay, startYear);
				*/
				/**
				String startTimeStr = (String) newTaskJSON.get("startTime");
				Integer startTime = Integer.valueOf(startTimeStr);
				*/
				String endTimeStr = (String) newTaskJSON.get("endTime");
				Integer endTime = Integer.valueOf(endTimeStr);

				String taskName = (String) newTaskJSON.get("Name");

				String taskType = (String) newTaskJSON.get("Type");
				//end casting datatypes
				
				TransientTask newTask = new TransientTask(startDate, startTime, endTime, taskName, taskType);
				addTask(newTask);
				continue;

			}
			//detecting if the current task is an antitask
			if(!newTaskJSON.containsKey("Type")){
				//do things for normal tasks

				//again casting to datatypes
				//pretty much all of this moved outside control loops
				/**
				String startDateStr = (String) newTaskJSON.get("StartDate");
				Integer startYear = Integer.valueOf(startDateStr.substring(0,3));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 5));
				Integer startDay = Integer.valueOf(startDateStr.substring(6,7));
				Date startDate = new Date(startMonth,startDay, startYear);
	
				String startTimeStr = (String) newTaskJSON.get("startTime");
				Integer startTime = Integer.valueOf(startTimeStr);
				
				String endTimeStr = (String) newTaskJSON.get("endTime");
				Integer endTime = Integer.valueOf(endTimeStr);
				*/
				String taskName = (String) newTaskJSON.get("Name");
				//end casting datatypes
				
				AntiTask newTask = new AntiTask(startDate, startTime, endTime, taskName);
				removeTask(newTask);
				continue;

			}
		}
		return;
	}
	public void writeFile(ArrayList<Task> listOfTasks, String filepath) throws IOException
	{
		//'filepath' is the absolute filepath (like C:\documents\options.txt)


		//incoming data is as a linked list. make sure to adhere to the pattern as written above
		//delimiter is <,> with the arrow/less then/greater than signs
		//recursive should be saved in this order:
		//int startMonth, int startDay, int endMonth, int endDay, String frequency, String name, String type, int startTime, int endTime
		Writer fileWriter = new FileWriter(filepath, true);
		JSONObject fileJsonObject = new JSONObject();
		JSONArray fileJsonArray = new JSONArray();
		for (int i = 0; i < listOfTasks.size(); i++){
			JSONObject fileJsonArrayObject = new JSONObject();
			if(listOfTasks.get(i) instanceof RecursiveTask){
				//do the shit
				fileJsonArrayObject.put("Name", listOfTasks.get(i).getName());
				fileJsonArrayObject.put("Type", listOfTasks.get(i).getType());
				//compile the year month and day into a single string then cast that into an integer.
				String startDateStrTemp = listOfTasks.get(i).getEndDate.getYear();
				Integer StartDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
															Integer.toString(listOfTasks.get(i).getEndDate.getMonth()), 
															Integer.toString(listOfTasks.get(i).getEndDate.getDay())
															));
				fileJsonArrayObject.put("StarDate", StartDateCompiled);
				fileJsonArrayObject.put("StartTime", listOfTasks.get(i).getStartTime());
				//calculate duration
				fileJsonArrayObject.put("Duration", listOfTasks.get(i).get)
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