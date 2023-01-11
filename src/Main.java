import manager.Manager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task = new Task(
                "Task name",
                "Task description", Task.Status.NEW);
                manager.createTask(task);
        Task task1 = new Task(
                "Task1 name",
                "Task1 description", Task.Status.NEW);
                manager.createTask(task1);
        Epic epic = new Epic(
                "name Epic",
                "description Epic",
                Task.Status.NEW,new ArrayList<>());
                manager.createEpic(epic);
        SubTask subtask = new SubTask(
                "subtask name",
                "subtask description",
                Task.Status.NEW,3);
                manager.createSubTask(subtask);
        SubTask subtask1 = new SubTask(
                "subtask1 name",
                "subtask1 description",
                Task.Status.DONE,3);
                manager.createSubTask(subtask1);
        Epic epic1 = new Epic(
                "name Epic1",
                "description epic1",
                Task.Status.NEW,new ArrayList<>());
                manager.createEpic(epic1);
        SubTask subtask2 = new SubTask(
                "subtask2 name",
                "subtask2 description",
                Task.Status.DONE,6);
                manager.createSubTask(subtask2);
        System.out.println(manager.getAllEpics());//тут выводится с айди на первом месте, можно вставлять в методы ниже
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        /*System.out.println(epic);
        manager.displaySubtaskByEpicId(6);
        manager.deleteEpicById(6);
        System.out.println(manager.getEpicById(6));
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        manager.deleteAllTasks();
        manager.deleteTaskById(2);
        manager.deleteSubtaskById(4);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());*/
    }
}
