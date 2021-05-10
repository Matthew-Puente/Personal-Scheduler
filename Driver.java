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

		String dateString = "";

		char choice = 'n';

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
			System.out.println("1. Display current schedule");
			System.out.println("2. Create a transient task");
			System.out.println("3. Create a recursive task");
			System.out.println("4. Create an anti task");
			System.out.println("5. quit");

			choice = scanner.next().charAt(0);
			scanner.nextLine();
			// need way to convert from real time to 15 min increments, each time interval is for the real day
			// tempStartDate and tempEndDate is the time interval for a recursive task from one day to another
			// dateToRemove is the day to cancel out the given startTime and endTime for to add the anti task
			switch (choice)
			{
				case '1':
					scheduler.printAllTask();

					break;

				case '2':
					System.out.println("Enter the date for a transient task in the format: MM-dd-yyyy");
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

					System.out.println("Enter the name of the transient task:");
					name = scanner.nextLine();

					System.out.println("Enter the type of the transient task:");
					type = scanner.nextLine();

					TransientTask transientTask = new TransientTask(tempStartDate,720, 1080, name, type);

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

					System.out.println("Enter the frequency of the recursive task (D for daily, W for weekly, M for monthly) CAPS SENSITIVE:");
					frequency = scanner.nextLine();

					System.out.println("Enter the name of the recursive task:");
					name = scanner.nextLine();

					System.out.println("Enter the type of the recursive task:");
					type = scanner.nextLine();

					System.out.println("tempStartDate.getMonth(): " + tempStartDate.getMonth());
					System.out.println("tempStartDate.getDay(): " + tempStartDate.getDay());
					System.out.println("tempEndDate.getMonth(): " + tempEndDate.getMonth());
					System.out.println("tempEndDate.getDay(): " + tempEndDate.getDay());

					RecursiveTask recursiveTask = new RecursiveTask(frequency, name, type, tempStartDate,tempEndDate, 360, 420);

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

					AntiTask antiTask = new AntiTask(dateToRemove,360,420);

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

				case '5':
					// close file

					break;

			}
		}

	}

}

