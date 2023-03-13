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

        Task task = new Task
                (Task.Type.TASK,"name","description", Task.Status.NEW,100,"20/03/2023/18:59");
        Epic epic = new Epic
                (Task.Type.EPIC,"name Epic", "description Epic", Task.Status.NEW,90,"20/03/2023/07:00");
        SubTask subTask = new SubTask
                (Task.Type.SUBTASK,"Subtask","description", Task.Status.NEW,10,"20/03/2023/11:00",2);
        SubTask subTask1 = new SubTask
                (Task.Type.SUBTASK,"Subtask1","description1", Task.Status.NEW,20,"20/03/2023/12:00",2);
        SubTask subTask2 = new SubTask
                (Task.Type.SUBTASK,"Subtask2","description2", Task.Status.NEW,30,"20/03/2023/18:00",2);
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        System.out.println(manager.getPrioritizedTasks());

        /*FileBackedTasksManager fileman = loadFromFile(new File("C:\\Users\\Пользователь\\dev\\java-kanban\\src\\data.csv"));

        System.out.println(fileman.getHistory());
        System.out.println(fileman.getAllTasks());
        System.out.println(fileman.getAllEpics());
        System.out.println(fileman.getAllSubtasks());*/
        }

    }



