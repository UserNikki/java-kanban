import manager.InMemoryTaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();
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
                Task.Status.NEW, new ArrayList<>());
        manager.createEpic(epic);
        Epic epic1 = new Epic(
                "name Epic1",
                "description epic1",
                Task.Status.NEW,new ArrayList<>());
        manager.createEpic(epic1);
        System.out.println(manager.getHistory());
        Epic epic2 = new Epic(
                "name Epic2",
                "description epic1",
                Task.Status.NEW,new ArrayList<>());
                manager.createEpic(epic2);
        Epic epic3 = new Epic(
                "name Epic3",
                "description epic1",
                Task.Status.NEW,new ArrayList<>());
        manager.createEpic(epic3);
        Epic epic4 = new Epic(
                "name Epic4",
                "description epic1",
                Task.Status.NEW,new ArrayList<>());
        manager.createEpic(epic4);
        Epic epic5 = new Epic(
                "name Epic5",
                "description epic1",
                Task.Status.NEW,new ArrayList<>());
        manager.createEpic(epic5);

        Task task3 = new Task(
                "Task3 name",
                "Task description", Task.Status.NEW);
        manager.createTask(task3);
        Task task4 = new Task(
                "Task name",
                "Task description", Task.Status.NEW);
        manager.createTask(task4);
        Task task5 = new Task(
                "Проверка удаления 11 элемента",
                "Должен встать в 0 индекс", Task.Status.NEW);
        manager.createTask(task5);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(4);
        manager.getEpicById(5);
        manager.getEpicById(6);
        manager.getEpicById(7);
        manager.getEpicById(8);
        manager.getTaskById(9);
        manager.getTaskById(10);
        manager.getTaskById(11);
        System.out.println(manager.getHistory());
    }
    }

