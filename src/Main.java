import manager.Manager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Epic epic = new Epic(
                "name",
                "description",
                manager.generateId(), Task.Status.NEW,new ArrayList<>());
                manager.createEpic(epic);
        SubTask subtask = new SubTask(
                "subtask name",
                "subtask description",
                manager.generateId(), Task.Status.NEW,1);
                manager.createSubTask(subtask);
        SubTask subtask1 = new SubTask(
                "subtask1 name",
                "subtask1 description",
                manager.generateId(), Task.Status.DONE,1);
                manager.createSubTask(subtask1);
        System.out.println(epic);
        manager.getAllTasks();
        manager.getSubtaskByEpicId(1);
        System.out.println(manager.getEpicById(1));

    }
}
