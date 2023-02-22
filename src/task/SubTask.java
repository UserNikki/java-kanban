package task;

import task.Task;

public class SubTask extends Task {

private int epicId;

    public SubTask(Type type, String name, String description, Status status, int epicId) {
        super(type, name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return super.toString() +
                "," + epicId;
    }

    @Override
    public int getTaskId() {
        return super.getTaskId();
    }

    @Override
    public void setTaskId(int taskId) {
        super.setTaskId(taskId);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }
}
