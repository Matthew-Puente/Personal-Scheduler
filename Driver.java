import java.util.Scanner;

public class Driver
{

	public static void main(String[] args)
	{
		Scheduler scheduler = new Scheduler();

		FileHandler fileHandler;

		Scanner scanner = new Scanner(System.in);

		String input = "";

		char choice = 'n';

		System.out.println("Would you like to import tasks from a file? (y/n)");
		choice = scanner.next().charAt(0);

		if (choice == 'y')
		{
			// open the users file
		}

		Date tempStartDate = new Date(3,14); // March 14th

		Date tempEndDate = new Date(7,14);

		Date dateToRemove = new Date(5, 14);

		while (choice != '5')
		{
			System.out.println("1. Display current schedule");
			System.out.println("2. Create a transient task");
			System.out.println("3. Create a recursive task");
			System.out.println("4. Create an anti task");
			System.out.println("5. quit");

			choice = scanner.next().charAt(0);

			switch (choice)
			{
				case '1':
					scheduler.printAllTask();

					break;

				case '2':
					TransientTask transientTask = new TransientTask(tempStartDate,720, 1080, "Working on Projects", "Study");

					boolean wow = scheduler.createTransientTask(transientTask);

					break;

				case '3':
					RecursiveTask recursiveTask = new RecursiveTask("M", "Napping", "Survival", tempStartDate,tempEndDate, 360, 420);

					wow = scheduler.createRecursiveTask(recursiveTask);

					break;

				case '4':
					AntiTask antiTask = new AntiTask(dateToRemove,360,420);

					wow = scheduler.removeTask(antiTask);

					break;

				case '5':
					// close file

					break;

			}
		}

	}

}


