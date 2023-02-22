package task;

public class Task {
    private int taskId;
    private Type type;
    private String name;
    private String description;
    private Status status;

    public enum Type {
        TASK,
        EPIC,
        SUBTASK
    }
    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    public Task(Type type, String name, String description,Status status) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
    }
    @Override
    public String toString() {
        return taskId +
                     "," +type +
                     "," + name +
                     "," + description +
                     "," + status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
