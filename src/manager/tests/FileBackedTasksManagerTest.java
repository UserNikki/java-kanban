package manager.tests;

import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
//Привет. Не понял про граничные условия для записи в файл
    //у нас заданиями и реализацией не предусмотрена какая-либо разница в записи в файл для эпика с задачами и без задач
    FileBackedTasksManager manager;
    private static final String PATH = "src/data.csv" ;
    protected Task task;
    protected Epic epic;
    protected SubTask subtask;

    @BeforeEach
    @Override
    void createTestData() {
        this.manager = new FileBackedTasksManager(new File(PATH));
        this.task = manager.createTask(new Task
                (Task.Type.TASK,"FileBackedTasksManager","Task test",
                        Task.Status.NEW,100,"10/03/2023/18:59"));
        this.epic = manager.createEpic(new Epic
                (Task.Type.EPIC,"FileBackedTasksManager", "test Epic",
                        Task.Status.NEW,90,"10/03/2023/20:00"));
        this.subtask = manager.createSubTask(new SubTask
                (Task.Type.SUBTASK,"FileBackedTasksManager","test Subtask",
                        Task.Status.NEW,10,"10/03/2023/18:05",2));

    }

    @Test
    void mustSaveToFileTest() {
        FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(new File(PATH));
        assertEquals(testManager.getTaskById(1).toString(), task.toString(), "таски не равны");
        assertEquals(testManager.getEpicById(2).toString(), epic.toString(), "эпики не равны");
        assertEquals(testManager.getSubtaskById(3).toString(), subtask.toString(), "сабтаски не равны");
//я так понимаю, что в данном случае проверяется одновременно и сохранение и загрузка обратно
    }

    @Override
    @Test
    void createTaskTest() {
        super.createTaskTest();
    }

    @Override
    @Test
    void createSubTaskTest() {
        super.createSubTaskTest();
    }

    @Override
    @Test
    void createEpicTest() {
        super.createEpicTest();
    }

    @Override
    @Test
    void mustGetAllTasksTest() {
        super.mustGetAllTasksTest();
    }

    @Override
    @Test
    void mustGetAllEpicsTest() {
        super.mustGetAllEpicsTest();
    }

    @Override
    @Test
    void mustGetAllSubtasksTest() {
        super.mustGetAllSubtasksTest();
    }

    @Override
    @Test
    void displaySubtaskByEpicIdTest() {
        super.displaySubtaskByEpicIdTest();
    }

    @Override
    @Test
    void mustDeleteTaskByIdTest() {
        super.mustDeleteTaskByIdTest();
    }

    @Override
    @Test
    void mustDeleteEpicByIdTest() {
        super.mustDeleteEpicByIdTest();
    }

    @Override
    @Test
    void mustDeleteSubtaskByIdTest() {
        super.mustDeleteSubtaskByIdTest();
    }

    @Override
    @Test
    void mustDeleteAllTasksTest() {
        super.mustDeleteAllTasksTest();
    }

    @Override
    @Test
    void mustDeleteAllEpicsTest() {
        super.mustDeleteAllEpicsTest();
    }

    @Override
    @Test
    void mustDeleteAllSubtasksTest() {
        super.mustDeleteAllSubtasksTest();
    }

    @Override
    void mustUpdateTaskTest() {
        super.mustUpdateTaskTest();
    }

    @Override
    void mustUpdateEpicTest() {
        super.mustUpdateEpicTest();
    }

    @Override
    void mustUpdateSubtaskTest() {
        super.mustUpdateSubtaskTest();
    }

    @Override
    void mustGetTaskByIdTest() {
        super.mustGetTaskByIdTest();
    }

    @Override
    void mustGetEpicByIdTest() {
        super.mustGetEpicByIdTest();
    }

    @Override
    void mustGetSubtaskByIdTest() {
        super.mustGetSubtaskByIdTest();
    }



}