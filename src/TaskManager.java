import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class TaskManager {

  String FILE_NAME = "tasks.json";
  Path file = Path.of(FILE_NAME);
  Set<Task> tasks;
  TaskManager() {
    this.tasks = loadTasks();
  }

  Set<Task> loadTasks() {

    if (!Files.exists(file)) {
      try {
        Files.createFile(file);
        return new HashSet<>();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    } else {
      try {
        String json = Files.readString(file);
        if (json.isEmpty()) return  new HashSet<>();
        String[] jsonarr = json.substring(2, json.length() - 2).split("},\\{");
        return Arrays.stream(jsonarr).map(Task::new).collect(Collectors.toSet());

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void saveTask() {
    if (tasks.isEmpty()) {
      try {
        Files.writeString(file, "");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      String json = tasks.stream()
          .map(Task::toJson)
          .collect(joining(",", "[", "]"));

      try {
        Files.writeString(file, json);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  int addTask(String task) {
    int id = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    tasks.add( new Task(id, task));
    saveTask();
    return  id;
  }

  int deleteTask(int id) {
    tasks.removeIf(task -> task.getId() == id);
    return  id;
  }

  void markTask(int id, TaskStatus newStatus) {
    try {
      Task orignalTask = findTaskById(id);
      Task updatedTask = new Task(id, orignalTask.description);
      updatedTask.setCreatedAt(orignalTask.createdAt);
      updatedTask.setUpdatedAt(LocalDateTime.now());
      updatedTask.setStatus(newStatus);

      tasks.remove(orignalTask);
      tasks.add(updatedTask);

      saveTask();
    } catch ( Exception e ) {
      System.err.println(e.getMessage());
    }
  }

  void listByStatus(TaskStatus status) {
    System.out.println("Task List That are " + status.getStatus());

    filterTasks(task -> task.status == status).forEach(System.out::println );
  }



  void updateTask(int id, String description) {

    try {
      Task orignalTask = findTaskById(id);
      Task updatedTask = new Task(id, description);
      updatedTask.setCreatedAt(orignalTask.createdAt);
      updatedTask.setUpdatedAt(LocalDateTime.now());

      tasks.remove(orignalTask);
      tasks.add(updatedTask);

      saveTask();
    } catch ( Exception e ) {
      throw  e;
    }

  }

  private Task findTaskById(int id) {
    return findTask(task -> task.getId() == id);
  }

  private Set<Task> filterTasks(Predicate<Task> filter) {
    return tasks.stream().filter(filter).collect(Collectors.toSet());
  }

  private Task findTask(Predicate<Task> predicate) {
    return  tasks.stream().filter(predicate).findFirst().orElseThrow( () -> new NoSuchElementException("No task found") );
  }

  public List<Task> getAllTasks() {
    return tasks.stream().toList();
  }

  public List<Task> getAllTasksByStatus(TaskStatus status) {
    return filterTasks(task -> task.status == status).stream().toList();
  }


}
