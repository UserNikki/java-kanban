import manager.*;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import task.Epic;
import task.SubTask;
import task.Task;
import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        //ДО ВОСКРЕСЕНИЯ 2 АПРЕЛЯ НУЖНО ПОЛУЧИТЬ ЗАЧЕТ, ИНАЧЕ ВЫЛЕТ. ПОЖАЛУЙСТА, НЕ БУДЬ СЛИШКОМ СТРОГ))
        //УЖЕ САМ ВИЖУ, МНОГОЕ НЕ ОЧЕНЬ, НО ВСЕ ПЕРЕПИСЫВАТЬ НЕ УСПЕВАЮ
        //общий тест сохранения/загрузки обратно по kv, спойлер - работает)
        //сделал отельный мейн для проверки апи в классе хттпТаскСервер
        //мне уже сдавший чел объяснил, что это две не связанные штуки в контексте тз
        //просто чтобы научиться и так и так делать
        //т.е. я сам того не понимая, интуитивно сдела вроде бы правильно, но сомневался т.к. искал общую связь
        new KVServer().start();
        HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078/");

        Task task = new Task
                (Task.Type.TASK,"name","description", Task.Status.NEW,100,"26/03/2023/11:59");
        Task task1 = new Task
                (Task.Type.TASK,"name1","description1", Task.Status.NEW,55,"25/03/2023/18:59");
        Epic epic = new Epic
                (Task.Type.EPIC,"name Epic", "description Epic", Task.Status.NEW,10,"20/03/2023/07:00");
        SubTask subTask = new SubTask
                (Task.Type.SUBTASK,"Subtask","description", Task.Status.NEW,15,"20/03/2023/11:00",2);
        SubTask subTask1 = new SubTask
                (Task.Type.SUBTASK,"Subtask1","description1", Task.Status.NEW,20,"20/03/2023/12:00",2);
        SubTask subTask2 = new SubTask
                (Task.Type.SUBTASK,"Subtask2","description2", Task.Status.NEW,30,"20/03/2023/18:00",2);

        httpTaskManager.createTask(task);
        httpTaskManager.createTask(task1);
        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);
        httpTaskManager.createSubTask(subTask1);
        httpTaskManager.createSubTask(subTask2);
        httpTaskManager.getTaskById(1);//просмотр для записи айди истории и вывода в консоли
        System.out.println(httpTaskManager.getClientKV().load("subtask"));
        System.out.println(httpTaskManager.getClientKV().load("epic"));
        System.out.println(httpTaskManager.getClientKV().load("task"));
        System.out.println(httpTaskManager.getClientKV().load("history"));
        //System.out.println(HttpTaskManager.loadFromServer("http://localhost:8078/"));
        }

    }



