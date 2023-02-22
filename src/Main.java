import manager.*;
import task.Epic;
import task.Node;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        //ПРИВЕТ. НЕ СОВСЕМ ПОНЯЛ ПРО СОЗДАТЬ МЭЙН В ДРУГОМ КЛАССЕ.IDE ГОВОРИТ МЭЙН ДОЛЖЕН БЫТЬ В КЛАССЕ МЭЙН
        //ЗАПИСАТЬ С ОТСТУПОМ 2 СТРОКИ НЕ ПРОБЛЕМА. А ВОТ СЧИТАТЬ ОКАЗАЛОСЬ ПРОБЛЕМА))
        //ПОДСКАЖИ ПОЖАЛУЙСТА КАК ЭТО СДЕЛАТЬ. НАТЫКАЯСЬ НА ПРОПУСК ВЫХОДИТ ЭТО:
        //Exception in thread "main" java.lang.NumberFormatException: For input string: ""
        //ВЕСЬ ДЕНЬ ПОЧТИ ПОТРАТИЛ НА ПОПЫТКИ ЕГО ИГНОРИРОВАТЬ И ПРОПУСКАТЬ
        //НАУЧИ КАК ЭТО СДЕЛАТЬ. ОБРАБОТКА NumberFormatException НЕ ПОМОГЛА
        //ДА И БУФЕРЕДРИДЕР НАВЕРНОЕ ПЕРЕСТАЕТ БЫТЬ ISrEADY() И СТРОКУ ЧИТАТЬ КАК НА ПРОПУСК НАТЫКАЕТСЯ

        FileBackedTasksManager fileman =new FileBackedTasksManager(new File("C:\\Users\\Пользователь\\dev\\java-kanban\\alltasks.csv"));
        InMemoryTaskManager manager = new InMemoryTaskManager();
        TaskManager taskManager = new FileBackedTasksManager(new File("C:\\Users\\Пользователь\\dev\\java-kanban\\alltasks.csv"));

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

        /*fileman.loadFromFile(new File("C:\\Users\\Пользователь\\dev\\java-kanban\\alltasks.csv"));
        System.out.println(fileman.getHistory());
        System.out.println(fileman.getAllTasks());
        System.out.println(fileman.getAllEpics());
        System.out.println(fileman.getAllSubtasks());*/

    }

}

