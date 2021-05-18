
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
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
			
			//was having issues casting from a string to an int in a single line, this presumably can be
			//shortened but my priority is to make it work first.



			//Detect if it is a recursive or antitask and then get the StartDate field
			//TransientTasks use a Date field instead.
			Date startDate = new Date(1, 1, 0001);
			if(newTaskJSON.containsKey("EndDate") || newTaskJSON.get("Type") == "Cancellation"){
				String startDateStr = (String) String.valueOf(newTaskJSON.get("StartDate"));
				Integer startYear = Integer.valueOf(startDateStr.substring(0,4));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 6));
				Integer startDay = Integer.valueOf(startDateStr.substring(6));
				startDate = new Date(startMonth, startDay, startYear);
			}



			String startTimeStr = (String) String.valueOf(newTaskJSON.get("StartTime"));
			Integer startTime = (Integer) Integer.valueOf(startTimeStr)*60;

			//begin converting duration to time, then add it to start time to get end time.
			//multiply duration by 60. Should be an integer now.
			Double durationLong = Double.valueOf(newTaskJSON.get("Duration").toString())*60;
			Integer duration = (Integer) durationLong.intValue();
			//add start time * 60 to duration as endTime is in minutes in a day (1440 minutes in a day)
			Integer endTime = (Integer) startTime + duration;

			//common parameters end here


			//this is if it is a recursive task
			if(newTaskJSON.containsKey("EndDate")){
	

				//begin casting the variables into their intended datatypes
				
				String taskName = (String) newTaskJSON.get("Name");

				String taskType = (String) newTaskJSON.get("Type");


				String endDateStr = (String) String.valueOf(newTaskJSON.get("EndDate"));
				Integer endYear = Integer.valueOf(endDateStr.substring(0,4));
				Integer endMonth = Integer.valueOf(endDateStr.substring(4, 6));
				Integer endDay = Integer.valueOf(endDateStr.substring(6));
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

				long frequency = (long) newTaskJSON.get("Frequency");
				String frequencyStr;
				switch ((int) frequency) {
					case 1: frequencyStr = "D";
						break;
					case 7: frequencyStr = "W";
						break;
					default: frequencyStr = "M";
						break;
				}
				//String frequencyStr = (String) newTaskJSON.get("Frequency");
				//finished casting to intended datatypes

				RecursiveTask newTask = new RecursiveTask(frequencyStr, taskName, taskType, startDate, endDate, startTime, endTime);
				tasksFromFile.add(newTask);
				continue;
				//do things for recursive task
			}


			//detecting if the current task is a Transient or Anti-Task
			if(newTaskJSON.containsKey("Type")){
				//do things for normal tasks
				
				//again casting to datatypes
				String startDateStr = (String) String.valueOf(newTaskJSON.get("Date"));
				Integer startYear = Integer.valueOf(startDateStr.substring(0,4));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 6));
				Integer startDay = Integer.valueOf(startDateStr.substring(6));
				startDate = new Date(startMonth,startDay, startYear);


				String taskName = (String) newTaskJSON.get("Name");

				String taskType = (String) newTaskJSON.get("Type");
				//end casting datatypes
				if(taskType.toUpperCase().equals("CANCELLATION"))
				{
					AntiTask newTask = new AntiTask(startDate,startTime,endTime);
					tasksFromFile.add(newTask);
					continue;
				}
				TransientTask newTask = new TransientTask(startDate, startTime, endTime, taskName, taskType);
				tasksFromFile.add(newTask);
				continue;

			}
			//detecting if the current task is an antitask
			/*if(newTaskJSON.get("Type").equals("Cancellation")){
				//do things for normal tasks

				//again casting to datatypes
				String startDateStr = (String) String.valueOf(newTaskJSON.get("Date"));
				Integer startYear = Integer.valueOf(startDateStr.substring(0,4));
				Integer startMonth = Integer.valueOf(startDateStr.substring(4, 6));
				Integer startDay = Integer.valueOf(startDateStr.substring(6));
				startDate = new Date(startMonth,startDay, startYear);
				//end casting datatypes
				
				AntiTask newTask = new AntiTask(startDate, startTime, endTime);
				tasksFromFile.add(newTask);
				continue;

			}*/
		}
		return tasksFromFile;
	}
	
	public void writeFile(LinkedList<Task> linkedList, String filepath) throws IOException
	{
		//'filepath' is the absolute filepath (like C:\documents\options.txt)


		//incoming data is as a linked list. make sure to adhere to the pattern as written above
		//delimiter is <,> with the arrow/less then/greater than signs
		//recursive should be saved in this order:
		//int startMonth, int startDay, int endMonth, int endDay, String frequency, String name, String type, int startTime, int endTime
		Writer fileWriter = new FileWriter(filepath, true);
		//JSONObject fileJsonObject = new JSONObject();
		JSONArray fileJsonArray = new JSONArray();
		for (int i = 0; i < linkedList.size(); i++){

			//get the start date into a String so we can check if it's double digits (as in above 9)
			String startDateStrTemp = Integer.toString(linkedList.get(i).getStartDate().getMonth());
			String startDateStrTempTwo = Integer.toString(linkedList.get(i).getStartDate().getDay());
			Integer StartDateCompiled;
			//Check if our start month has two digits in it. If it does not, pad a 0 before the month digit.
			if(startDateStrTemp.length() == 1){
				if(startDateStrTempTwo.length() == 1)
				{
					StartDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
					Integer.toString(linkedList.get(i).getStartDate().getYear()),
					"0",
					Integer.toString(linkedList.get(i).getStartDate().getMonth()),"0", 
					Integer.toString(linkedList.get(i).getStartDate().getDay())
					));
				}
				else
				{
					StartDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
					Integer.toString(linkedList.get(i).getStartDate().getYear()),
					"0",
					Integer.toString(linkedList.get(i).getStartDate().getMonth()), 
					Integer.toString(linkedList.get(i).getStartDate().getDay())
					));
				}
			}
			else{
				if(!(startDateStrTempTwo.length() == 1))
				{
					StartDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
							Integer.toString(linkedList.get(i).getStartDate().getYear()),"0",
							Integer.toString(linkedList.get(i).getStartDate().getMonth()), 
							Integer.toString(linkedList.get(i).getStartDate().getDay())
							));
				}
				else
				{
					StartDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
							Integer.toString(linkedList.get(i).getStartDate().getYear()),"0",
							Integer.toString(linkedList.get(i).getStartDate().getMonth()),"0", 
							Integer.toString(linkedList.get(i).getStartDate().getDay())
							));
				}
			}
			
			//get the start time. startTime is in minutes per day so we divide it by 60.
			Integer startTime = Integer.valueOf(linkedList.get(i).getStartTime()/60);
			//int startTimeMinutes = Integer.valueOf(linkedList.get(i).getStartTime()%60);
			//begin converting duration to time, then add it to start time to get end time.
			//multiply duration by 60. Should be an integer now.
			//Integer duration = (Integer) listOfTasks.get(i).get("Duration")*60;
			//add start time * 60 to duration as endTime is in minutes in a day (1440 minutes in a day)


			//make the object we will insert all our data into. This object then goes into the array at the end.
			JSONObject fileJsonArrayObject = new JSONObject();
			if(linkedList.get(i) instanceof RecursiveTask){


				//Get things into the json in order (though it shouldnt matter)
				//consider changing the variables like "startTime" as it is the same as "StartTime"
				RecursiveTask recursiveTemp = (RecursiveTask) linkedList.get(i);
				fileJsonArrayObject.put("Name", recursiveTemp.getName());
				fileJsonArrayObject.put("Type", recursiveTemp.getType());
				fileJsonArrayObject.put("StartDate", StartDateCompiled);
				fileJsonArrayObject.put("StartTime", startTime);


				//get the duration as an int, endTime-startTime divided by 60 should return it.
				double duration = ((double)recursiveTemp.getEndTime() - (double)recursiveTemp.getStartTime())/60;
				fileJsonArrayObject.put("Duration", duration);


				//get the end date. This works the same as the start date but with getEndDate instead.
				String endDateStringTemp = Integer.toString(recursiveTemp.getEndDate().getMonth());
				String endDateStringTempTwo = Integer.toString(recursiveTemp.getEndDate().getDay());
				int EndDateCompiled;
				if(endDateStringTemp.length() == 1)
				{
					if(endDateStringTempTwo.length() == 1)
					{
						EndDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
								Integer.toString(recursiveTemp.getEndDate().getYear()),"0",
								Integer.toString(recursiveTemp.getEndDate().getMonth()),"0",
								Integer.toString(recursiveTemp.getEndDate().getDay())
					));
					}
					else
					{
						EndDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
								Integer.toString(recursiveTemp.getEndDate().getYear()),"0",
								Integer.toString(recursiveTemp.getEndDate().getMonth()),
								Integer.toString(recursiveTemp.getEndDate().getDay())
					));
					}
				}
				else
				{
					if(endDateStringTempTwo.length() == 1)
					{
						EndDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
								Integer.toString(recursiveTemp.getEndDate().getYear()),
								Integer.toString(recursiveTemp.getEndDate().getMonth()),"0",
								Integer.toString(recursiveTemp.getEndDate().getDay())
					));
					}
					else
					{
						EndDateCompiled = Integer.valueOf(startDateStrTemp.join("", 
								Integer.toString(recursiveTemp.getEndDate().getYear()),
								Integer.toString(recursiveTemp.getEndDate().getMonth()),
								Integer.toString(recursiveTemp.getEndDate().getDay())
					));
					}
				}
				fileJsonArrayObject.put("EndDate", EndDateCompiled);

				//check frequency and apply accordingly.
				String frequencyStr = recursiveTemp.getFrequency();
				long frequency = 0;
				switch (frequencyStr) {
					case "D": frequency = 1;
						break;
					case "W": frequency = 7;
						break;
					case "M": frequency = 10;
						break;
				}
				fileJsonArrayObject.put("Frequency", frequency);
				
			}
			if(linkedList.get(i) instanceof TransientTask){
				TransientTask transientTemp = (TransientTask) linkedList.get(i);
				fileJsonArrayObject.put("Name", transientTemp.getName());
				fileJsonArrayObject.put("Type", transientTemp.getType());
				fileJsonArrayObject.put("Date", StartDateCompiled);
				fileJsonArrayObject.put("StartTime", startTime);

				//get the duration as an int, endTime-startTime divided by 60 should return it.
				double duration = ((double)transientTemp.getEndTime() - (double)transientTemp.getStartTime())/60;
				fileJsonArrayObject.put("Duration", duration);
			}
			if(linkedList.get(i) instanceof AntiTask){
				AntiTask antitaskTemp = (AntiTask) linkedList.get(i);
				fileJsonArrayObject.put("Date", StartDateCompiled);
				fileJsonArrayObject.put("StartTime", startTime);
				fileJsonArrayObject.put("Type", "Cancellation");
				//get the duration as an int, endTime-startTime divided by 60 should return it.
				double duration = ((double)antitaskTemp.getEndTime() - (double)antitaskTemp.getStartTime())/60;
				fileJsonArrayObject.put("Duration", duration);

			}
			//add the object, as created in one of three conditions above, to the JSON array here.
			fileJsonArray.add(fileJsonArrayObject);

			//call writeJSONString to write the entire array all at once to the fileWriter Writer object
			//this object is declared up top before all these conditional blocks

		}
		//The professor did NOT have a label for the array within the object so presumably we can pass an empty string.
		//fileJsonObject.put(fileJsonArray);
		fileJsonArray.writeJSONString(fileWriter);
		fileWriter.close();
	}
}