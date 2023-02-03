import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        InMemoryHistoryManager managerHistory = new InMemoryHistoryManager();
        Task task = new Task("Task name", "Task description", Task.Status.NEW);
        manager.createTask(task);
        Task task1 = new Task("Task1 name", "Task1 description", Task.Status.NEW);
        manager.createTask(task1);
        Epic epic = new Epic("name Epic", "description Epic", Task.Status.NEW, new ArrayList<>());
        manager.createEpic(epic);
        Epic epic1 = new Epic("name Epic1", "description epic1", Task.Status.NEW,new ArrayList<>());
        manager.createEpic(epic1);
        SubTask subTask = new SubTask("Subtask","description", Task.Status.NEW,3);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Subtask1","description", Task.Status.NEW,3);
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Subtask2","description", Task.Status.NEW,3);
        manager.createSubTask(subTask2);


        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        System.out.println(manager.getHistory());
        manager.getEpicById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        System.out.println(manager.getHistory());
        manager.getSubtaskById(7);
        manager.getTaskById(1);
        System.out.println(manager.getHistory());

        manager.deleteTaskById(1);
        System.out.println(manager.getHistory());
        manager.deleteEpicById(3);
        System.out.println(manager.getHistory());








    }
    }

