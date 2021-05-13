import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.*;

public class Driver
{

	public static void main(String[] args)
	{

		Driver driver = new Driver();

		Scheduler scheduler = new Scheduler();

		FileHandler fileHandler;

		Scanner scanner = new Scanner(System.in);

		// boolean for if a task is successfully created
		boolean creationSuccessful = false;

		String name, type, frequency;

		// for user to input the date
		String dateString = "";
		// for user to input the times
		String startTimeString = "";

		String endTimeString = "";
		// for parsing the time strings to an integer
		int startTime = 0;

		int endTime = 0;

		char choice = 'n';

		/*
		System.out.println("Would you like to import tasks from a file? (y/n)");
		choice = scanner.next().charAt(0);
		scanner.nextLine();

		if (choice == 'y')
		{
			// open the users file
		}
		*/

		Date tempStartDate = new Date(01,01,01);

		Date tempEndDate = new Date(01,01,01);

		Date dateToRemove = new Date(01, 01,01);

		while (choice != '5')
		{
			driver.printOptions();

			choice = scanner.next().charAt(0);
			scanner.nextLine();

			switch (choice)
			{	// 1. print the schedule
				case '1':
					scheduler.printAllTask();

					break;

				// 2. create a transient task
				case '2':
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

					System.out.println("Enter the type of the transient task:");
					type = scanner.nextLine();

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

					break;

				// 3. create a recursive task
				case '3':
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

					System.out.println("Enter the name of the recursive task:");
					name = scanner.nextLine();

					System.out.println("Enter the type of the recursive task:");
					type = scanner.nextLine();

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

					break;

				// 4. create an anti task
				case '4':
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

					break;

				// close the file and end the program
				case '5':

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
		System.out.println("5. quit");
	}

	public boolean dateIsValid(String d)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		simpleDateFormat.setLenient(false);

		// using java's Date class with the full path, different from our custom Date class
		java.util.Date date = null;

		try
		{
			date = simpleDateFormat.parse(d);
			if(date.getYear() < 1000 || date.getYear() > 9999)
				return false;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public int getDay(String d)
	{
		if (d.charAt(0) != '0')
		{
			if (d.charAt(2) != '0' && d.charAt(3) == '/')
			{
				return Integer.parseInt(d.substring(2,3));
			}
			else
			{
				return Integer.parseInt(d.substring(2,4));
			}
		}
		else
		{
			if (d.charAt(3) != '0' && d.charAt(4) == '/')
			{
				return Integer.parseInt(d.substring(3,4));
			}
			else
			{
				return Integer.parseInt(d.substring(3,5));
			}
		}

	}

	public int getMonth(String d)
	{
		if (d.charAt(0) != '0')
		{
			return Integer.parseInt(d.substring(0,1));
		}
		else
		{
			return Integer.parseInt(d.substring(0,2));
		}

	}

	public int getYear(String d)
	{
		if (d.length() == 10)
		{
			return Integer.parseInt(d.substring(6,10));
		}
		else if (d.length() == 9)
		{
			return Integer.parseInt(d.substring(5,9));
		}
		else if (d.length() == 8)
		{
			return Integer.parseInt(d.substring(4,8));
		}
		else
		{
			return Integer.parseInt(d.substring(6,10));
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

}

