package test.managers;

import manager.FileBackedTasksManager;
import manager.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManager manager;
    protected HistoryManager historyManager;
    private static final String PATH = "src/data.csv" ;
    protected Task task;
    protected Epic epic;
    protected SubTask subtask;

    @BeforeEach
    @Override
    void createTestData() {
        this.historyManager = manager.getHistory();
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
        //если я просто удалю задачи, негде будет срабатывать методу save(). Пустым файл никак не станет,
        //останется лишь состояние на момент последнего срабатывания save() поэтому тест с пустой записью опускаю
    }

    @Test
    void mustNotSaveToFileTest() {


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
    @Test
    void mustUpdateTaskTest() {
        super.mustUpdateTaskTest();
    }

    @Override
    @Test
    void mustUpdateEpicTest() {
        super.mustUpdateEpicTest();
    }

    @Override
    @Test
    void mustUpdateSubtaskTest() {
        super.mustUpdateSubtaskTest();
    }

    @Override
    @Test
    void mustGetTaskByIdTest() {
        super.mustGetTaskByIdTest();
    }

    @Override
    @Test
    void mustGetEpicByIdTest() {
        super.mustGetEpicByIdTest();
    }

    @Override
    @Test
    void mustGetSubtaskByIdTest() {
        super.mustGetSubtaskByIdTest();
    }

    @Test
    void mustReturnHistoryAsListTest() {
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        List<Task> historyListForTest = manager.getHistory().getHistory();
        assertEquals(historyListForTest.size(), historyManager.getHistory().size());
        assertArrayEquals(historyListForTest.toArray(),historyManager.getHistory().toArray());
        assertNotNull(historyListForTest);
    }

    @Test
    void mustReturnEmptyHistoryListTest() {
        List<Task> historyListForTest = manager.getHistory().getHistory();
        assertEquals(historyListForTest.size(), historyManager.getHistory().size());
        assertArrayEquals(historyListForTest.toArray(),historyManager.getHistory().toArray());
    }
    @Test
    void mustAddElementToHistoryTest() {
        historyManager.add(task);
        assertEquals(task, historyManager.getHistory().get(0),"different Test.Test.test.tasks");
    }

    @Test
    void mustRemove1stElementFromHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        int index = historyManager.getHistory().indexOf(task);
        historyManager.remove(task.getTaskId());
        assertNotEquals(historyManager.getHistory().get(index), task);
        assertNotNull(historyManager.getHistory().get(index));
        index = historyManager.getHistory().indexOf(epic);
        historyManager.remove(task.getTaskId());
        assertNotEquals(historyManager.getHistory().get(index), task);

    }
    @Test
    void mustRemove2ndElementFromHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        int index = historyManager.getHistory().indexOf(epic);
        historyManager.remove(epic.getTaskId());
        assertNotEquals(historyManager.getHistory().get(index), task);
        assertNotNull(historyManager.getHistory().get(index));
    }
    @Test
    void mustRemoveThirdElementFromHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        int index = historyManager.getHistory().indexOf(subtask);
        historyManager.remove(subtask.getTaskId());
        assertNotEquals(historyManager.getHistory().get(index), task);
        assertNotNull(historyManager.getHistory().get(index));
    }

    @Test
    void shouldNotHaveCreatedDuplicateHistoryTest() {
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        int size = historyManager.getHistory().size();
        assertEquals(historyManager.getHistory().size(), size);
        manager.getTaskById(1);
        assertEquals(historyManager.getHistory().size(), size);
    }

}