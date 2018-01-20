import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class ProjectPlanner {
  private TaskGraph tasks = new TaskGraph();

  // reads data from file, stores it in a graph
  // returns a list of tasks sorted by id (for convenient printing later)
  public List<Task> readFile (String fileName) {
    File taskFile = new File(fileName);
    Scanner fileReader = null;
    try {
      fileReader = new Scanner(taskFile);
    } catch (FileNotFoundException e) {
      System.out.println("FILE NOT FOUND");
      System.exit(1);
    }
    System.out.println("#----------------------" + fileName + "----------------------#");
    System.out.println("FILE SUCCESSFULLY FOUND. LOADING PROJECT.");

    int numTasks = 0;
    if (fileReader.hasNextLine())
      numTasks = Integer.parseInt(fileReader.nextLine()); // get number of tasks
    System.out.println("DETECTED " + numTasks + " TASKS.\n");
    if (fileReader.hasNextLine()) fileReader.nextLine(); // move past the empty line

    this.tasks.setNumVertices(numTasks);
    List<Task> taskList = new ArrayList<Task>();
    for (int i = 0; i < numTasks; i++)
      taskList.add(new Task());

    int taskIndex = 0;
    while(fileReader.hasNextLine()) {
      String line = fileReader.nextLine();
      String[] tokens = line.split("[ \t]{1,}");

      if (tokens.length > 1) {
        int id = Integer.parseInt(tokens[0]);
        String name = tokens[1];
        int time = Integer.parseInt(tokens[2]);
        int staff = Integer.parseInt(tokens[3]);
        taskList.get(taskIndex).setProperties(id, time, staff, name);

        int[] preReqs = new int[(tokens.length-1) - 4];
        int inDegree = preReqs.length;
        taskList.get(taskIndex).setInDegree(inDegree);
        if (inDegree > 0) {
          for (int i = 0; i < inDegree; i++) {
            preReqs[i] = Integer.parseInt(tokens[i+4]);
            taskList.get(taskIndex).getInEdges().add(taskList.get(preReqs[i]-1));
          }
        }
      }
      taskIndex++; // advance to set up the next task
    }

    for (Task t : taskList)
      tasks.getAdjMap().put(t, new ArrayList<Task>());
    tasks.initAdjMap();
    return taskList;
  }

  // given a list with tasks, prints the optimal completion schedule
  public void printProject(List<Task> taskList) {

    // checks if the project is unrealizable
    List<Task> taskCycle = tasks.detectCycle();
    if (taskCycle.size() > 0) {
      System.out.println("The project is not realizable.");
      System.out.println("It runs into the following cycle:");
      for (Task task : taskCycle)
        System.out.println(task.getId() + ") " + task);
      System.out.println();
      return;
    }

    // if the projects is realizable, uses Dijkstra's algorithm to optimize schedule
    System.out.println("The project is realizable.");
    System.out.println("The optimal schedule is as follows:\n");
    tasks.setTimes();
    int staff = 0;
    int minTime = tasks.minTime(); // minimal completion time

    // stores all important time points to avoid unnecessary checks later
    List<Integer> timePoints = new ArrayList<Integer>();
    for (Task task : taskList) {
      timePoints.add(task.earliestStart);
      timePoints.add(task.earliestStart + task.getTime());
    }

    // advances time unit by unit, prints occuring events
    for (int t = 0; t <= minTime; t++) {
      if (timePoints.contains(t)) {
        List<Task> startedTasks = new ArrayList<Task>();
        List<Task> finishedTasks = new ArrayList<Task>();
        for (Task task : taskList) {
          if (t == task.earliestStart) {
            startedTasks.add(task);
            staff += task.getStaff();
          } else if (t == (task.earliestStart + task.getTime())) {
            finishedTasks.add(task);
            staff -= task.getStaff();
          }
        }

        if (startedTasks.size() > 0 || finishedTasks.size() > 0) {
          System.out.println("Time: " + t);
          for (Task started : startedTasks)
            System.out.println("Started: " + "\"" + started + "\"" + " (ID: " + started.getId() + ")");
          for (Task finished : finishedTasks)
            System.out.println("Finished: " + "\"" + finished + "\"" + " (ID: " + finished.getId() + ")");
          System.out.println("Current Staff: " + staff + "\n");
        }
      }
    }
    System.out.println("-------- Minimal completion time: " + minTime + " --------\n\n");
  }

  // given a list with tasks, prints detailed information about each of them
  public void printAllTasks(List<Task> taskList) {
    System.out.println("Printing all task data:\n");
    for (Task t : taskList) {
      System.out.println("Task ID: " + t.getId());
      System.out.println("Task name: " + t.getName());
      System.out.println("Task time cost: " + t.getTime());
      System.out.println("Task manpower: " + t.getStaff());
      if (!tasks.foundCycle) { // start/slack time only printed for acyclic graphs
        System.out.println("Earliest start: " + t.earliestStart);
        System.out.println("Latest start: " + t.latestStart);
        System.out.print("Task slack: " + t.slack);
        if (t.slack == 0)
          System.out.print(" (CRITICAL TASK)\n");
        else
          System.out.print("\n");
      }
      if (tasks.getAdjMap().get(t).size() > 0)
        System.out.println("Dependent tasks:");
      for (Task adjacent : tasks.getAdjMap().get(t)) {
        System.out.println(adjacent + " (ID: " + adjacent.getId() + ")");
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    ProjectPlanner buildProject = new ProjectPlanner();
    List<Task> taskList = buildProject.readFile(args[0]);
    buildProject.printProject(taskList);
    buildProject.printAllTasks(taskList);
  }
}
