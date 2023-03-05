import manager.*;
import task.Epic;
import task.Node;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static manager.FileBackedTasksManager.loadFromFile;

public class Main {

    public static void main(String[] args) throws IOException {

        //FileBackedTasksManager fileman =new FileBackedTasksManager(new File("C:\\Users\\Пользователь\\dev\\java-kanban\\src\\data.csv"));
        InMemoryTaskManager manager = new InMemoryTaskManager();
        TaskManager taskManager = new FileBackedTasksManager(new File("C:\\Users\\Пользователь\\dev\\java-kanban\\src\\data.csv"));

        /*Task task = new Task(Task.Type.TASK,"Task name", "Task description", Task.Status.NEW);
        taskManager.createTask(task);
        Task task1 = new Task(Task.Type.TASK,"Task1 name", "Task1 description", Task.Status.NEW);
        taskManager.createTask(task1);
        Epic epic = new Epic(Task.Type.EPIC,"name Epic", "description Epic", Task.Status.NEW, new ArrayList<>());
        taskManager.createEpic(epic);
        Epic epic1 = new Epic(Task.Type.EPIC,"name Epic1", "description epic1", Task.Status.NEW,new ArrayList<>());
        taskManager.createEpic(epic1);
        SubTask subTask = new SubTask(Task.Type.SUBTASK,"Subtask","description", Task.Status.NEW,3);
        taskManager.createSubTask(subTask);
        SubTask subTask1 = new SubTask(Task.Type.SUBTASK,"Subtask1","description", Task.Status.NEW,3);
        taskManager.createSubTask(subTask1);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        System.out.println(taskManager.getHistory());*/

        /*FileBackedTasksManager fileman = loadFromFile(new File("C:\\Users\\Пользователь\\dev\\java-kanban\\src\\data.csv"));

        System.out.println(fileman.getHistory());
        System.out.println(fileman.getAllTasks());
        System.out.println(fileman.getAllEpics());
        System.out.println(fileman.getAllSubtasks());*/
        }

    }



