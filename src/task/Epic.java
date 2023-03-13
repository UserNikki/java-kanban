package task;

import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {

    private List<Integer> subTaskIdList;
    private LocalDateTime endTimeForEpic;


    public Epic(Type type, String name, String description,Status status,long duration,String startTime) {
        super(type, name, description, status,duration,startTime);
        this.subTaskIdList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return super.toString() +
                "," + endTimeForEpic.format(Task.DATE_TIME_FORMATTER);
    }

    public void setEndTimeForEpic(LocalDateTime endTimeForEpic) {
        this.endTimeForEpic = endTimeForEpic;
    }

    public LocalDateTime getEndTimeForEpic() {
        return endTimeForEpic;
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

    public void setSubTaskIdList(int subTaskId) {
        this.subTaskIdList.add(subTaskId);
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
