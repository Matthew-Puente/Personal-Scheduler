
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Driver
{

	public static void main(String[] args)
	{
		Scheduler scheduler = new Scheduler();

		FileHandler fileHandler;

		Scanner scanner = new Scanner(System.in);

		// boolean for if a task is successfully created
		boolean creationSuccessful = false;

		// using java's Date class with the full path, different from our custom Date class
		java.util.Date date;

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
		// for checking if the user's date has this given date format
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");

		System.out.println("Would you like to import tasks from a file? (y/n)");
		choice = scanner.next().charAt(0);

		if (choice == 'y')
		{
			// open the users file
		}

		Date tempStartDate = new Date(01,01);

		Date tempEndDate = new Date(01,01);

		Date dateToRemove = new Date(01, 01);

		while (choice != '5')
		{
			// print the options
			System.out.println("1. Display current schedule");
			System.out.println("2. Create a transient task");
			System.out.println("3. Create a recursive task");
			System.out.println("4. Create an anti task");
			System.out.println("5. quit");

			choice = scanner.next().charAt(0);
			scanner.nextLine();

			switch (choice)
			{	// 1. print the schedule
				case '1':
					scheduler.printAllTask();

					break;

				// 2. create a transient task
				case '2':
					System.out.println("Enter the date for a transient task in the format: MM-dd-yyyy");
					dateString = scanner.nextLine();

					try
					{
						// check if the date format given by user is correct and print the unix timestamp
						date = simpleDateFormat.parse(dateString);
						System.out.println("Given time in ms (unix time): " + date.getTime());

					}
					catch(ParseException e)
					{
						e.printStackTrace();
					}

					/*
					take substrings of the user's dateString to get month and day
					.substring method takes arguments (startIndex, endIndex + 1)
					 */
					tempStartDate.setMonth(Integer.parseInt(dateString.substring(0,2)));
					tempStartDate.setDay(Integer.parseInt(dateString.substring(3,5)));
					// MUST INCLUDE 0 FOR SINGLE DIGIT HOURS e.g. 01:00 AM, 03:15 PM
					System.out.println("Enter the start time of the transient task in format: hh:mm [AP]M e.g. 12:15 AM or 02:15 PM (INTERVALS OF 15 FOR MINUTES):");
					startTimeString = scanner.nextLine();
					// this takes the hours of the time given mod 12 multiplies that number by 60 then adds the minutes, lastly if the time has PM add 720 to convert to PM
					startTime = Integer.parseInt(startTimeString.substring(0,2)) % 12 * 60 + Integer.parseInt(startTimeString.substring(3,5));

					if (startTimeString.substring(6,8).equals("PM"))
					{
						startTime += 720;
					}
					// same thing here but for end time instead of start time
					System.out.println("Enter the end time of the transient task in format: hh:mm [AP]M e.g. 12:15 AM or 02:15 PM (INTERVALS OF 15 FOR MINUTES):");
					endTimeString = scanner.nextLine();
					endTime = Integer.parseInt(endTimeString.substring(0,2)) % 12 * 60 + Integer.parseInt(endTimeString.substring(3,5));

					if (endTimeString.substring(6,8).equals("PM"))
					{
						endTime += 720;
					}

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
					System.out.println("Enter the start date for a recursive task in the format: MM-dd-yyyy");
					dateString = scanner.nextLine();

					try
					{
						date = simpleDateFormat.parse(dateString);
						System.out.println("Given time in ms (unix time): " + date.getTime());

					}
					catch(ParseException e)
					{
						e.printStackTrace();
					}

					/*
					take substrings of the user's dateString to get month and day
					.substring method takes arguments (startIndex, endIndex + 1)
					 */
					tempStartDate.setMonth(Integer.parseInt(dateString.substring(0,2)));
					tempStartDate.setDay(Integer.parseInt(dateString.substring(3,5)));

					System.out.println("Enter the end date for the recursive task in the format: MM-dd-yyyy");
					dateString = scanner.nextLine();

					try
					{
						date = simpleDateFormat.parse(dateString);
						System.out.println("Given time in ms (unix time): " + date.getTime());

					}
					catch(ParseException e)
					{
						e.printStackTrace();
					}

					/*
					take substrings of the user's dateString to get month and day
					.substring method takes arguments (startIndex, endIndex + 1)
					 */
					tempEndDate.setMonth(Integer.parseInt(dateString.substring(0,2)));
					tempEndDate.setDay(Integer.parseInt(dateString.substring(3,5)));

					System.out.println("Enter the start time of the recursive task in format: hh:mm [AP]M e.g. 12:15 AM or 02:15 PM (INTERVALS OF 15 FOR MINUTES):");
					startTimeString = scanner.nextLine();
					startTime = Integer.parseInt(startTimeString.substring(0,2)) % 12 * 60 + Integer.parseInt(startTimeString.substring(3,5));

					if (startTimeString.substring(6,8).equals("PM"))
					{
						startTime += 720;
					}

					System.out.println("Enter the end time of the recursive task in format: hh:mm [AP]M e.g. 12:15 AM or 02:15 PM (INTERVALS OF 15 FOR MINUTES):");
					endTimeString = scanner.nextLine();
					endTime = Integer.parseInt(endTimeString.substring(0,2)) % 12 * 60 + Integer.parseInt(endTimeString.substring(3,5));

					if (endTimeString.substring(6,8).equals("PM"))
					{
						endTime += 720;
					}

					System.out.println("Enter the frequency of the recursive task (D for daily, W for weekly, M for monthly) CAPS SENSITIVE:");
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
					System.out.println("Enter the date for an anti task in the format: MM-dd-yyyy");
					dateString = scanner.nextLine();

					try
					{
						date = simpleDateFormat.parse(dateString);
						System.out.println("Given time in ms (unix time): " + date.getTime());

					}
					catch(ParseException e)
					{
						e.printStackTrace();
					}

					/*
					take substrings of the user's dateString to get month and day
					.substring method takes arguments (startIndex, endIndex + 1)
					 */
					dateToRemove.setMonth(Integer.parseInt(dateString.substring(0,2)));
					dateToRemove.setDay(Integer.parseInt(dateString.substring(3,5)));

					System.out.println("Enter the start time of the anti task in format: hh:mm [AP]M e.g. 12:15 AM or 02:15 PM (INTERVALS OF 15 FOR MINUTES):");
					startTimeString = scanner.nextLine();
					startTime = Integer.parseInt(startTimeString.substring(0,2)) % 12 * 60 + Integer.parseInt(startTimeString.substring(3,5));

					if (startTimeString.substring(6,8).equals("PM"))
					{
						startTime += 720;
					}

					System.out.println("Enter the end time of the anti task in format: hh:mm [AP]M e.g. 12:15 AM or 02:15 PM (INTERVALS OF 15 FOR MINUTES):");
					endTimeString = scanner.nextLine();
					endTime = Integer.parseInt(endTimeString.substring(0,2)) % 12 * 60 + Integer.parseInt(endTimeString.substring(3,5));

					if (endTimeString.substring(6,8).equals("PM"))
					{
						endTime += 720;
					}

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

}

