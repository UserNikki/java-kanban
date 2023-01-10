package task;

import task.Task;

import java.util.List;

public class Epic extends Task {

    private List<Integer> subTaskIdList;//инициализировать здесь как ты советовал не выходит
                                        //буду благодарен, если подскажешь на будущее как инициализировать
                                        //чтобы в конструкторе при создании инициализировался новый список


    public Epic(String name, String description, int taskId, Status status, List<Integer> subTaskIdList) {
        super(name, description, taskId, status);
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
                "subTaskIdList=" + subTaskIdList.size() +
                '}';
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
