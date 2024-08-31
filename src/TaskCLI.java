import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.System.out;

public class TaskCLI {

  private final Scanner scanner;
  private final TaskManager manager;
  private final TasksPrinter printer;

  public TaskCLI() {
    this.scanner = new Scanner(System.in);
    this.manager = new TaskManager();
    this.printer = new TasksPrinter();
  }

  public void start() throws IOException {
    out.println(Files.readString(Path.of("banner.txt")));
    printHelp();

    String command;
    while (!(command = scanner.next().toLowerCase()).equals("exit")) {
      switch (command) {
        case "add" -> add();
        case "update" -> update();
        case "delete" -> delete();
        case "mark-todo" -> markTodo();
        case "mark-done" -> markDone();
        case "mark-in-progress" -> markInProgress();
        case "list" -> list();
        case "list-todo" -> listTodo();
        case "list-in-progress" -> listInProgress();
        case "list-done" -> listDone();
        case "help" -> printHelp();
        default -> out.println("Unknown command. Type 'help' to see the list of commands or 'exit' to exit.");
      }
    }

    System.exit(0);

  }

  private void add() {
    String description = scanner.nextLine().trim();
    int id = manager.addTask(description);
    out.printf("Task added successfully (ID: %d)\n", id);
  }

  private void update() {
    try {
      int id = scanner.nextInt();
      String description = scanner.nextLine().trim();
      manager.updateTask(id, description);
      out.println("Task updated successfully");
    } catch (NoSuchElementException e) {
      out.println(e.getMessage());
    }
  }

  private void delete() {
    try {
      int id = scanner.nextInt();
      manager.deleteTask(id);
      out.println("Task deleted successfully");
      scanner.nextLine();
    } catch (NoSuchElementException e) {
      out.println(e.getMessage());
    }
  }

  private void markTodo() {
    try {
      int id = scanner.nextInt();
      manager.markTask(id, TaskStatus.TODO);
      out.println("Task marked as Todo successfully");
      scanner.nextLine();
    } catch (NoSuchElementException e) {
      out.println(e.getMessage());
    }
  }

  private void markInProgress() {
    try {
      int id = scanner.nextInt();
      manager.markTask(id, TaskStatus.IN_PROGRESS);
      out.println("Task marked as In-Progress successfully");
      scanner.nextLine();
    } catch (NoSuchElementException e) {
      out.println(e.getMessage());
    }
  }

  private void markDone() {
    try {
      int id = scanner.nextInt();
      manager.markTask(id, TaskStatus.DONE);
      out.println("Task marked as Done successfully");
      scanner.nextLine();
    } catch (NoSuchElementException e) {
      out.println(e.getMessage());
    }
  }

  private void list() {
    printer.printTaskList(manager.getAllTasks());
    scanner.nextLine();
  }

  private void listTodo() {
    printer.printTaskList(manager.getAllTasksByStatus(TaskStatus.TODO));
    scanner.nextLine();
  }

  private void listInProgress() {
    printer.printTaskList(manager.getAllTasksByStatus(TaskStatus.IN_PROGRESS));
    scanner.nextLine();
  }

  private void listDone() {
    printer.printTaskList(manager.getAllTasksByStatus(TaskStatus.DONE));
    scanner.nextLine();
  }


  private void printHelp() {
    String help = """
                - add [description] : Add a new task
                - update [id] [description] : Update a task
                - delete [id] : Delete a task
                - mark-todo [id] : Mark a task as Todo
                - mark-in-progress [id] : Mark a task as In-Progress
                - mark-done [id] : Mark a task as Done
                - list : List all tasks
                - list-todo : List all Todo tasks
                - list-in-progress : List all In-Progress tasks
                - list-done : List all Done tasks
                - exit : Exit the program
                """;
    out.println(help);
  }







}
