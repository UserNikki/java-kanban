import manager.*;
import task.Epic;
import task.SubTask;
import task.Task;
import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        /*
        СНОВА ПРИВЕТ. ТОТ КТО ПРИДУМАЛ 7 И 8 СПРИНТ НАВЕРНОЕ НАС НЕНАВИДИТ ПОЧЕМУ-ТО))
        НЕВЕРОЯТНОЕ КОЛ-ВО ИНФЫ ЗА ТАКОЙ ПЕРИОД.
        НЕ СТАЛ НАКИДЫВАТЬ ВСЕ ТЕСТЫ ТАК КАК УВЕРЕН ЧТО НАЛАЖАЛ С РЕАЛИЗАЦИЕЙ, К ТОМУ ЖЕ ДЕДЛАЙН ЖЕСТКИЙ ПРИШЕЛ
        ПО ОТДЕЛЬНОСТИ KVСЕРВЕР И КЛИЕНТ РАБОТАЛИ, ЭНДПОЙНТЫ АПИ РАБОТАЛИ
        ВМЕСТЕ КАК ОНИ ДОЛЖНЫ РАБОТАТЬ Я ТАК И НЕ ПОНЯЛ(ЕСТЬ ДАЖЕ ПОДОЗРЕНИЕ ЧТО И НЕДОЛЖНЫ),
        СЛЕДОВАТЕЛЬНО НЕ ПОНИМАЮ КАК И ЧТО ТЕСТИРОВАТЬ.
        НЕ ПОНЯЛ КАК ОДНОВРЕМЕННО ТЕСТИРОВАТЬ HTTPTASKMANAGER И KVSERVER
        СЕРВЕР В НАШЕМ УЧЕБНОМ СЛУЧАЕ РЕЛЕВАНТЕН КОГДА ЗАПУЩЕН, А ОТКУДА ОН ДОЛЖЕН ЧТО ТО БРАТЬ ПРИ ЗАПУСКЕ Я НЕ ПОНЯЛ
        ИЗВИНИ ЗА СЫРУЮ РАБОТУ И НАПРАВЬ НА ПУТЬ ВЕРНЫЙ)) #спаситипамагити

         */
        new HttpTaskServer().launchServer();
        new KVServer().start();
        FileBackedTasksManager manager = new FileBackedTasksManager(new File("src/data.csv"));
        //HttpTaskManager httpTaskManager = new HttpTaskManager(URI.create("http://localhost:8078/"));

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

        manager.createTask(task);
        manager.createTask(task1);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        //manager.getTaskById(1);
        //System.out.println(httpTaskManager.getClientKV().load("subtask"));

        }

    }



