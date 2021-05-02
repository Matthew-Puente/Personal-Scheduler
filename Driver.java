package basePackage;

public class Driver {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Date tempStartDate = new Date(3,14); // March 14th
		scheduler.printSchedule();
		boolean wow = scheduler.createTransientTask(tempStartDate,720, 1080, "Working on Projects", "Study");
		scheduler.printSchedule();
		System.out.println(wow);
		Date tempEndDate = new Date(7,14);
		wow = scheduler.createRecursiveTask("M", "Napping", "Survival", tempStartDate,360, tempEndDate, 420);
		System.out.println(wow);
		Date dateToRemove = new Date(5,14);
		wow = scheduler.removeTask(tempStartDate, 720, 1080);
		System.out.println(wow);
	}

}
