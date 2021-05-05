package basePackage;

public class Driver {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Date tempStartDate = new Date(3,14); // March 14th
		TransientTask transientTask = new TransientTask(tempStartDate,720, 1080, "Working on Projects", "Study");
		boolean wow = scheduler.createTransientTask(transientTask);
		scheduler.printAllTask();
		Date tempEndDate = new Date(7,14);
		RecursiveTask recursiveTask = new RecursiveTask("M", "Napping", "Survival", tempStartDate,tempEndDate, 360, 420);
		wow = scheduler.createRecursiveTask(recursiveTask);
		scheduler.printAllTask();
		Date dateToRemove = new Date(5,14);
		AntiTask antiTask = new AntiTask(dateToRemove,360,420);
		wow = scheduler.removeTask(antiTask);
		scheduler.printAllTask();
	}

}
