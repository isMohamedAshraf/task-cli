import java.time.LocalDateTime;

public class Task{

  int id;
  String description;
  TaskStatus status;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;

  public Task(int id, String description) {
    this.id = id;
    this.description = description;
    this.status = TaskStatus.TODO;
    this.createdAt = LocalDateTime.now();
  }

  public Task(String json) {
      String[] jsonValues = json
          .replaceAll("\"", "")
          .split(",")
          ;


      for (String jsonValue : jsonValues) {
        String keyValue = jsonValue.substring(0, jsonValue.indexOf(":"));
        String value = jsonValue.substring(jsonValue.indexOf(":") + 1);
        switch (keyValue) {
          case "id" -> id = Integer.parseInt(value);
          case "description" -> description = value;
          case "status" -> status = TaskStatus.valueOf(value);
          case "createdAt" -> createdAt = LocalDateTime.parse(value);
          case "updatedAt" -> {
            if (value.equalsIgnoreCase("null")) {
              updatedAt = null;
            } else  {
              updatedAt = LocalDateTime.parse(value);
            }
          }
        }
      }


  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String toJson() {
  return "{" +
        "\"id\":" + id + "," +
        "\"description\":\"" + getDescription() + "\"," +
        "\"status\":\"" + getStatus() + "\"," +
        "\"createdAt\":\"" + createdAt + "\"," +
        "\"updatedAt\":\"" + updatedAt + "\"" +
        "}";
  }

  @Override
  public String toString() {
    return String.format("ID:%s Description: %s Status: %s Created At: %s, Updated At: %s", id, description, status.getStatus(), createdAt, updatedAt);
  }


}
