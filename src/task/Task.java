package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int taskId;
    private Type type;
    private String name;
    private String description;
    private Status status;
    private long duration;
    private LocalDateTime startTime;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy/HH:mm");

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

    public Task(Type type, String name, String description,Status status,long duration,String startTime) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime,DATE_TIME_FORMATTER);
    }
    @Override
    public String toString() {
        return taskId +
                     "," +type +
                     "," + name +
                     "," + description +
                     "," + status +
                     "," + duration +
                     "," + startTime.format(DATE_TIME_FORMATTER);
    }

    public LocalDateTime getEndTime() {
        return getStartTime().plusMinutes(getDuration());
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
