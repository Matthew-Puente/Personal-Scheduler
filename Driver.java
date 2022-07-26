

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class Driver
{

	public static void main(String[] args)
	{
		Scheduler scheduler = new Scheduler();

		Scanner scanner = new Scanner(System.in);

		Driver driver = new Driver();

		FileHandler fileHandler = new FileHandler();

		String filePath = "";

		String input = "";

		char choice = '0';

		while (choice != '7')
		{
			driver.printOptions();

			input = scanner.nextLine();

			while (input.length() != 1)
			{
				System.out.println("Invalid choice, select your choice again:");
				driver.printOptions();
				input = scanner.nextLine();
			}

			choice = input.charAt(0);

			switch (choice)
			{	// 1. print the schedule
				case '1':
					scheduler.printAllTask();
					break;

				// 2. create a transient task
				case '2':
					driver.createTransientTask(scheduler);
					break;

				// 3. create a recursive task
				case '3':
					driver.createRecursiveTask(scheduler);
					break;

				// 4. create an anti task
				case '4':
					driver.createAntiTask(scheduler);
					break;

				// 5. read from a .json file
				case '5':
					System.out.println("Enter the file path to read:");
					filePath = scanner.nextLine();
					driver.createTasksFromFile(scheduler, fileHandler, filePath);
					break;

				// 6. write to a .json file
				case '6':
					System.out.println("Enter the file path to write:");
					filePath = scanner.nextLine();
				try {
					fileHandler.writeFile(scheduler.getLinkedList(), filePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					break;

				// 7. end the program
				case '7':
					System.out.println("Ending program");
					break;

				default:
					System.out.println("Invalid choice, select your choice again:");
					break;

			}
		}

	}

	public void printOptions()
	{
		System.out.println("1. Display current schedule");
		System.out.println("2. Create a transient task");
		System.out.println("3. Create a recursive task");
		System.out.println("4. Create an anti task");
		System.out.println("5. Read tasks from a .json file");
		System.out.println("6. Write tasks to a .json file");
		System.out.println("7. quit");
	}

	public boolean dateIsValid(String d)
	{
		java.util.Calendar calendar = new GregorianCalendar();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		simpleDateFormat.setLenient(false);

		// using java's Date class with the full path, different from our custom Date class
		java.util.Date date = null;

		try
		{
			date = simpleDateFormat.parse(d);
			calendar.setTime(date);
			int year = calendar.get(java.util.Calendar.YEAR);
			// keep years 4 digits
			if (year < 1000 || year > 9999)
			{
				return false;
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public int getDay(String d)
	{
		// create a substring of the original date string starting at the index after first slash
		int startIndex = d.indexOf("/") + 1;
		// trim this string to only have dd/yyyy
		String trimmedDate = d.substring(startIndex, d.length());

		// narrowed down to 2 cases, length will be 7 with a 2 digit day
		if (trimmedDate.length() == 7)
		{
			return Integer.parseInt(trimmedDate.substring(0,2));
		}
		// length will be 6 with a 1 digit day
		else
		{
			return Integer.parseInt(trimmedDate.substring(0,1));
		}
	}

	public int getMonth(String d)
	{
		// 1/ 2/ 3/ 4/ 5/ 6/ 7/ 8/ 9/
		if (d.charAt(0) != '0' && d.charAt(1) == '/')
		{
			return Integer.parseInt(d.substring(0,1));
		}
		// 01/ 02/ 03/ 04/ 05/ 06/ 07/ 08/ 09/ 10/ 11/ 12/
		else
		{
			return Integer.parseInt(d.substring(0,2));
		}

	}

	public int getYear(String d)
	{
		// only 3 cases after parsing date with dateIsValid()
		if (d.length() == 10)
		{
			return Integer.parseInt(d.substring(6,10));
		}
		else if (d.length() == 9)
		{
			return Integer.parseInt(d.substring(5,9));
		}
		else // d.length() == 8
		{
			return Integer.parseInt(d.substring(4,8));
		}

	}

	public boolean timeIsValid(String t)
	{
		/*
		Regex (regular expression) to check if the time is valid in 12-hour format
		( represents the start of the group.
		1[012]|[1-9] represents time start with 10, 11, 12 or 1, 2, …. 9
		) represents the end of the group.
		: represents time must be followed by colon(:).
		[0134][05] represents the time followed by 00 or 15 or 30 or 45
		(\\s)? represents white space, time followed by a white space which is optional.
		(?i) represents case insensitive, ‘am’, ‘pm’ or ‘AM’, ‘PM’ are same respectively.
		(am|pm) represents time should end with ‘am’, ‘pm’ or ‘AM’, ‘PM’.
		 */
		String regexPattern = "(1[012]|[1-9]):[0134][05](\\s)?(?i)(am|pm)";

		// compile the regex
		Pattern compiledPattern = Pattern.compile(regexPattern);

		if (t == null)
		{
			return false;
		}

		Matcher m = compiledPattern.matcher(t);

		return m.matches();
	}
	// this takes the hours of the time given mod 12 multiplies that number by 60 then adds the minutes, lastly if the time has PM add 720 to convert to PM
	public int getTime(String t)
	{
		// if the colon is the 2nd subscript of the time string then this time has a single digit hour e.g. 1:00pm
		if (t.charAt(1) == ':')
		{
			// if in any case of having a pm with or without a space, add 720 to the time
			// length of the string will be 6 if the user did not put a space, if they did put a space length will be 7
			if (t.length() == 6)
			{
				if (t.substring(4,6).equals("pm") || t.substring(4,6).equals("PM"))
				{
					return Integer.parseInt(t.substring(0,1)) % 12 * 60 + Integer.parseInt(t.substring(2,4)) + 720;
				}
				// otherwise it is am
				else
				{
					return Integer.parseInt(t.substring(0,1)) % 12 * 60 + Integer.parseInt(t.substring(2,4));
				}
			}

			if (t.length() == 7)
			{
				if (t.substring(5,7).equals("pm") || t.substring(5,7).equals("PM"))
				{
					return Integer.parseInt(t.substring(0,1)) % 12 * 60 + Integer.parseInt(t.substring(2,4)) + 720;
				}
				// otherwise it is am
				else
				{
					return Integer.parseInt(t.substring(0,1)) % 12 * 60 + Integer.parseInt(t.substring(2,4));
				}
			}

		}
		// otherwise it is a double digit hour e.g. 12:00 AM
		else
		{
			// if in any case of having a pm with or without a space, add 720 to the time
			// length of the string will be 7 if the user did not put a space, if they did put a space length will be 8
			if (t.length() == 7)
			{
				if (t.substring(5,7).equals("pm") || t.substring(5,7).equals("PM"))
				{
					return Integer.parseInt(t.substring(0,2)) % 12 * 60 + Integer.parseInt(t.substring(3,5)) + 720;
				}
				// otherwise it is am
				else
				{
					return Integer.parseInt(t.substring(0,2)) % 12 * 60 + Integer.parseInt(t.substring(3,5));
				}
			}

			if (t.length() == 8)
			{
				if (t.substring(6,8).equals("pm") || t.substring(6,8).equals("PM"))
				{
					return Integer.parseInt(t.substring(0,2)) % 12 * 60 + Integer.parseInt(t.substring(3,5)) + 720;
				}
				// otherwise it is am
				else
				{
					return Integer.parseInt(t.substring(0,2)) % 12 * 60 + Integer.parseInt(t.substring(3,5));
				}
			}

		}
		return -1;
	}

	void createTransientTask(Scheduler scheduler)
	{
		Scanner scanner = new Scanner(System.in);

		Driver driver = new Driver();

		String dateString = "";

		// for user to input the times
		String startTimeString = "";

		String endTimeString = "";
		// for parsing the time strings to an integer
		int startTime = 0;

		int endTime = 0;

		Date tempStartDate = new Date(01,01,01);

		// boolean for if a task is successfully created
		boolean creationSuccessful = false;

		String name = "";

		String type = "";

		System.out.println("Enter transient task date in format: MM/dd/yyyy");
		dateString = scanner.nextLine();

		// if the date is not valid, read it from the user until it's correct
		while (!driver.dateIsValid(dateString))
		{
			System.out.println("Incorrect format, enter date in format: MM/dd/yyyy");
			dateString = scanner.nextLine();
		}

		tempStartDate.setDay(driver.getDay(dateString));
		tempStartDate.setMonth(driver.getMonth(dateString));
		tempStartDate.setYear(driver.getYear(dateString));

		System.out.println("Enter transient task start time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
		startTimeString = scanner.nextLine();

		// if the time is not valid, read it from the user until it's correct
		while (!driver.timeIsValid(startTimeString))
		{
			System.out.println("Incorrect format, enter start time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
			startTimeString = scanner.nextLine();
		}

		startTime = driver.getTime(startTimeString);

		// same thing here but for end time instead of start time
		System.out.println("Enter transient task end time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
		endTimeString = scanner.nextLine();

		while (!driver.timeIsValid(endTimeString))
		{
			System.out.println("Incorrect format, enter end time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
			endTimeString = scanner.nextLine();
		}

		endTime = driver.getTime(endTimeString);

		System.out.println("Enter the name of the transient task:");
		name = scanner.nextLine();

		System.out.println("Enter the type of the transient task (Course, Study, Sleep, Exercise, Work, Meal, Visit, Shopping, and Appointment):");
		type = scanner.nextLine();

		while (!driver.transientTypeIsValid(type))
		{
			System.out.println("Incorrect type, enter type as (Course, Study, Sleep, Exercise, Work, Meal, Visit, Shopping, and Appointment):");
			type = scanner.nextLine();
		}

		TransientTask transientTask = new TransientTask(tempStartDate, startTime, endTime, name, type);
		// the scheduler returns true if the task was successfully created, false otherwise
		creationSuccessful = scheduler.createTransientTask(transientTask);

		if (creationSuccessful)
		{
			System.out.println("Successfully created transient task");
		}
		else
		{
			System.out.println("Failed to create transient task");
		}
	}
	// overloaded createTransientTask() function
	void createTransientTask(Scheduler scheduler, TransientTask transientTask)
	{
		boolean creationSuccessful = false;

		creationSuccessful = scheduler.createTransientTask(transientTask);

		if (creationSuccessful)
		{
			System.out.println("Successfully created transient task");
		}
		else
		{
			System.out.println("Failed to create transient task");
		}
	}

	void createRecursiveTask(Scheduler scheduler)
	{
		Scanner scanner = new Scanner(System.in);

		Driver driver = new Driver();

		String dateString = "";

		// for user to input the times
		String startTimeString = "";

		String endTimeString = "";
		// for parsing the time strings to an integer
		int startTime = 0;

		int endTime = 0;

		Date tempStartDate = new Date(01,01,01);

		Date tempEndDate = new Date(01,01,01);

		// boolean for if a task is successfully created
		boolean creationSuccessful = false;

		String name = "";

		String type = "";

		String frequency = "";

		System.out.println("Enter recursive task start date in format: MM/dd/yyyy");
		dateString = scanner.nextLine();

		// if the date is not valid, read it from the user until it's correct
		while (!driver.dateIsValid(dateString))
		{
			System.out.println("Incorrect format, enter date in format: MM/dd/yyyy");
			dateString = scanner.nextLine();
		}

		tempStartDate.setDay(driver.getDay(dateString));
		tempStartDate.setMonth(driver.getMonth(dateString));
		tempStartDate.setYear(driver.getYear(dateString));

		System.out.println("Enter recursive task end date in format: MM/dd/yyyy");
		dateString = scanner.nextLine();

		// if the date is not valid, read it from the user until it's correct
		while (!driver.dateIsValid(dateString))
		{
			System.out.println("Incorrect format, enter date in format: MM/dd/yyyy");
			dateString = scanner.nextLine();
		}

		tempEndDate.setDay(driver.getDay(dateString));
		tempEndDate.setMonth(driver.getMonth(dateString));
		tempEndDate.setYear(driver.getYear(dateString));

		System.out.println("Enter recursive task start time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
		startTimeString = scanner.nextLine();

		// if the time is not valid, read it from the user until it's correct
		while (!driver.timeIsValid(startTimeString))
		{
			System.out.println("Incorrect format, enter start time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
			startTimeString = scanner.nextLine();
		}

		startTime = driver.getTime(startTimeString);

		System.out.println("Enter recursive task end time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
		endTimeString = scanner.nextLine();

		// if the time is not valid, read it from the user until it's correct
		while (!driver.timeIsValid(endTimeString))
		{
			System.out.println("Incorrect format, enter end time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
			endTimeString = scanner.nextLine();
		}

		endTime = driver.getTime(endTimeString);

		System.out.println("Enter the frequency of the recursive task (D for daily, W for weekly, M for monthly):");
		frequency = scanner.nextLine();

		while (!driver.recursiveFrequencyIsValid(frequency))
		{
			System.out.println("Incorrect frequency, enter frequency as (D for daily, W for weekly, M for monthly):");
			frequency = scanner.nextLine();
		}

		System.out.println("Enter the name of the recursive task:");
		name = scanner.nextLine();

		System.out.println("Enter the type of the recursive task (Course, Study, Sleep, Exercise, Work, and Meal):");
		type = scanner.nextLine();

		while (!driver.recursiveTypeIsValid(type))
		{
			System.out.println("Incorrect type, enter type as (Course, Study, Sleep, Exercise, Work, and Meal):");
			type = scanner.nextLine();
		}

		RecursiveTask recursiveTask = new RecursiveTask(frequency, name, type, tempStartDate,tempEndDate, startTime, endTime);

		creationSuccessful = scheduler.createRecursiveTask(recursiveTask);

		if (creationSuccessful)
		{
			System.out.println("Successfully created recursive task");
		}
		else
		{
			System.out.println("Failed to create recursive task");
		}
	}
	// overloaded createRecursiveTask() function
	void createRecursiveTask(Scheduler scheduler, RecursiveTask recursiveTask)
	{
		boolean creationSuccessful = false;

		creationSuccessful = scheduler.createRecursiveTask(recursiveTask);

		if (creationSuccessful)
		{
			System.out.println("Successfully created recursive task");
		}
		else
		{
			System.out.println("Failed to create recursive task");
		}
	}

	void createAntiTask(Scheduler scheduler)
	{
		Scanner scanner = new Scanner(System.in);

		Driver driver = new Driver();

		String dateString = "";

		// for user to input the times
		String startTimeString = "";

		String endTimeString = "";
		// for parsing the time strings to an integer
		int startTime = 0;

		int endTime = 0;

		Date dateToRemove = new Date(01, 01,01);

		// boolean for if a task is successfully created
		boolean creationSuccessful = false;

		System.out.println("Enter anti task date in format: MM/dd/yyyy");
		dateString = scanner.nextLine();

		// if the date is not valid, read it from the user until it's correct
		while (!driver.dateIsValid(dateString))
		{
			System.out.println("Incorrect format, enter date in format: MM/dd/yyyy");
			dateString = scanner.nextLine();
		}

		dateToRemove.setDay(driver.getDay(dateString));
		dateToRemove.setMonth(driver.getMonth(dateString));
		dateToRemove.setYear(driver.getYear(dateString));

		System.out.println("Enter anti task start time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
		startTimeString = scanner.nextLine();

		// if the time is not valid, read it from the user until it's correct
		while (!driver.timeIsValid(startTimeString))
		{
			System.out.println("Incorrect format, enter start time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
			startTimeString = scanner.nextLine();
		}

		startTime = driver.getTime(startTimeString);

		// same thing here but for end time instead of start time
		System.out.println("Enter anti task end time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
		endTimeString = scanner.nextLine();

		while (!driver.timeIsValid(endTimeString))
		{
			System.out.println("Incorrect format, enter end time in format: 12:00 AM or 5:15AM or 2:30PM or 6:45 PM (only 00,15,30,45 for minutes)");
			endTimeString = scanner.nextLine();
		}

		endTime = driver.getTime(endTimeString);

		AntiTask antiTask = new AntiTask(dateToRemove,startTime,endTime);

		creationSuccessful = scheduler.removeTask(antiTask);

		if (creationSuccessful)
		{
			System.out.println("Successfully created anti task");
		}
		else
		{
			System.out.println("Failed to create anti task");
		}
	}

	// overloaded createAntiTask() function
	void createAntiTask(Scheduler scheduler, AntiTask antiTask)
	{
		boolean creationSuccessful = false;

		creationSuccessful = scheduler.removeTask(antiTask);

		if (creationSuccessful)
		{
			System.out.println("Successfully created anti task");
		}
		else
		{
			System.out.println("Failed to create anti task");
		}
	}
	public boolean transientTypeIsValid(String t)
	{
				switch (t.toUpperCase()) {
				case "COURSE": return true; 
				case "STUDY": return true; 
				case "SLEEP": return true; 
				case "EXERCISE": return true;  
				case "WORK": return true; 
				case "MEAL": return true; 
				case "VISIT": return true;  
				case "SHOPPING": return true; 
				case "APPOINTMENT": return true; 
				default: return false;
		}
	}

	public boolean recursiveTypeIsValid(String t)
	{
		switch (t.toUpperCase()) {
		case "COURSE": return true; 
		case "STUDY": return true; 
		case "SLEEP": return true; 
		case "EXERCISE": return true;  
		case "WORK": return true; 
		case "MEAL": return true;  
		default: return false;
		}
	}

	public boolean recursiveFrequencyIsValid(String f)
	{
		switch (f.toUpperCase()) {
			case "D": return true; 
			case "W": return true;
			case "M": return true;
			default: return false;
		}
	}

	void createTasksFromFile(Scheduler scheduler, FileHandler fileHandler, String filePath)
	{
		ArrayList<Task> tasks = new ArrayList<>();

		try {
			tasks = fileHandler.readFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println(tasks);

		for (int i = 0; i < tasks.size(); ++i)
		{
			if (tasks.get(i) instanceof TransientTask)
			{
				if (scheduler.createTransientTask((TransientTask) tasks.get(i)))
				{
					System.out.println("Successfully created TransientTask from file");
				}
				else
					System.out.println("Failed to create TransientTask from file");
			}
			else if (tasks.get(i) instanceof RecursiveTask)
			{
				if (scheduler.createRecursiveTask((RecursiveTask) tasks.get(i)))
				{
					System.out.println("Successfully created RecursiveTask from file");
				}
				else
					System.out.println("Failed to create RecursiveTask from file");
			}
			else
			{
				if (scheduler.removeTask((AntiTask) tasks.get(i)))
				{
					System.out.println("Successfully created AntiTask from file");
				}
				else
					System.out.println("Failed to create AntiTask from file");
			}

		}
		System.out.println("Finished adding tasks from the file");
	}

}