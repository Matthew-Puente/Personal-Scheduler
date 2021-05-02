package basePackage;

public class Driver {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Date tempStartDate = new Date(3,14); // March 14th
		scheduler.printDay(tempStartDate);
		boolean wow = scheduler.createTransientTask(tempStartDate,720, 1080, "Working on Projects", "Study");
		scheduler.printDay(tempStartDate);
		System.out.println(wow);
		Date tempEndDate = new Date(7,14);
		wow = scheduler.createRecursiveTask("M", "Napping", "Survival", tempStartDate,360, tempEndDate, 420);
		System.out.println(wow);
		Date dateToRemove = new Date(5,14);
		scheduler.printDay(dateToRemove);
		wow = scheduler.removeTask(dateToRemove, 360, 420);
		System.out.println(wow);
		scheduler.printDay(dateToRemove);
	}

}
