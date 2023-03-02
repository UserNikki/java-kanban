package task;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTaskIdList;


    public Epic(Type type, String name, String description,Status status, List<Integer> subTaskIdList) {
        super(type, name, description, status);
        this.subTaskIdList = subTaskIdList;
    }

    @Override
    public int getTaskId() {
        return super.getTaskId();
    }

    @Override
    public void setTaskId(int taskId) {
        super.setTaskId(taskId);
    }

    public List<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void setSubTaskIdList(int subTaskId) {//немного модифицировал сеттер
        this.subTaskIdList.add(subTaskId);
    }


    @Override
    public String toString() {
        return super.toString() +
                ",";
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
