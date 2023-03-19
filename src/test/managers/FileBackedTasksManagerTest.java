package test.managers;

import manager.FileBackedTasksManager;
import manager.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.time.LocalDateTime;
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
        this.manager = new FileBackedTasksManager(new File(PATH));
        this.task = manager.createTask(new Task
                (Task.Type.TASK,"InMemoryTaskManagerTest","Task test",
                        Task.Status.NEW,25,"25/03/2023/12:00"));
        this.epic = manager.createEpic(new Epic
                (Task.Type.EPIC,"InMemoryTaskManagerTest", "test Epic",
                        Task.Status.NEW,90,"26/03/2023/20:00"));
        this.subtask = manager.createSubTask(new SubTask
                (Task.Type.SUBTASK,"InMemoryTaskManagerTest","test Subtask",
                        Task.Status.NEW,10,"27/03/2023/18:05",2));
        this.historyManager = manager.getHistory();

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
        assertEquals(task, historyManager.getHistory().get(0),"different tasks");
        historyManager.remove(task.getTaskId());
        manager.getEpicById(2);
        assertEquals(epic, historyManager.getHistory().get(0),"different tasks");
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
        assertEquals(historyManager.getHistory().size(),3);
        int id = subtask.getTaskId();
        int index = historyManager.getHistory().indexOf(subtask);
        historyManager.remove(id);
        assertEquals(historyManager.getHistory().size(),2);
        final IndexOutOfBoundsException exp = assertThrows(IndexOutOfBoundsException.class,
                () -> {Task testTask =  historyManager.getHistory().get(index);}
        );
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

    @Test
    void shouldCountEpicDurationAsSumOfSubtasksDuration() {
        SubTask subtaskForTest = manager.createSubTask(new SubTask
                (Task.Type.SUBTASK,"Count epic duration test","test Subtask",
                        Task.Status.NEW,15,"10/03/2023/18:10",2));
        assertEquals((subtask.getDuration() + subtaskForTest.getDuration()), epic.getDuration());
    }

    @Test
    void shouldCountEpicDurationAsSubtaskDurationWhenOnlyHasOneSubtask() {
        assertEquals(epic.getDuration(), subtask.getDuration());
    }
    @Test
    void shouldCountTaskEndTimeAsStartTimePlusDuration() {
        LocalDateTime localDateTime = LocalDateTime.parse("25/03/2023/12:25", Task.DATE_TIME_FORMATTER);
        LocalDateTime wrongLocalDateTime = LocalDateTime.parse("25/03/2023/12:30", Task.DATE_TIME_FORMATTER);
        assertEquals(task.getEndTime(), localDateTime);
        assertNotEquals(task.getEndTime(), wrongLocalDateTime);
    }

    @Test
    void shouldCountSubTaskEndTimeAsStartTimePlusDuration() {
        LocalDateTime localDateTime = LocalDateTime.parse("27/03/2023/18:15", Task.DATE_TIME_FORMATTER);
        LocalDateTime wrongLocalDateTime = LocalDateTime.parse("25/03/2023/12:30", Task.DATE_TIME_FORMATTER);
        assertEquals(subtask.getEndTime(), localDateTime);
        assertNotEquals(subtask.getEndTime(), wrongLocalDateTime);
    }

    @Test
    void shouldCountEpicStartTimeWhenOnlyHasOneSubtask() {
        assertEquals(subtask.getStartTime(),epic.getStartTime());
    }
    @Test
    void shouldCountEpicStartTimeAsEarliestOfTwoSubtasks() {
        SubTask subtaskForTest = manager.createSubTask(new SubTask
                (Task.Type.SUBTASK,"Count epic duration test","test Subtask",
                        Task.Status.NEW,15,"25/03/2023/17:59",2));
        assertTrue(subtaskForTest.getStartTime().isBefore(subtask.getStartTime()));
        assertEquals(epic.getStartTime(), subtaskForTest.getStartTime());
    }

    @Test
    void shouldCountEpicEndTimeAsStartTimePlusDurationWhenNoSubtasks() {
        Epic epicForTest = manager.createEpic(new Epic
                (Task.Type.EPIC,"InMemoryTaskManagerTest", "test Epic",
                        Task.Status.NEW,20,"26/03/2023/20:00"));
        assertEquals(epicForTest.getStartTime().plusMinutes(epicForTest.getDuration()),
                epicForTest.getEndTimeForEpic());
        assertEquals(epicForTest.getEndTimeForEpic(),
                LocalDateTime.parse("26/03/2023/20:20", Task.DATE_TIME_FORMATTER));
    }

    @Test
    void shouldCountEpicEndTimeAsSubtaskEndTimeWhenHasOnlyOneSubtask() {
        assertEquals(epic.getEndTimeForEpic(), subtask.getEndTime());
    }

    @Test
    void shouldCountEpicEndTimeAsLatestSubtaskEndTime() {
        SubTask subtaskForTest = manager.createSubTask(new SubTask
                (Task.Type.SUBTASK,"Count epic duration test","test Subtask",
                        Task.Status.NEW,15,"30/03/2023/17:10",2));
        assertTrue(subtaskForTest.getEndTime().isAfter(subtask.getEndTime()));
        assertEquals(epic.getEndTimeForEpic(), subtaskForTest.getEndTime());
    }

    @Test
    void shouldNotCreateAnyTaskIfStartTimeCrossesTimePeriodOfAnyExistingTasksBoundariesTest() {//ну как нейминг))?
        //усовершенствовал валидацию включив границы в недопустимые значения

        Task taskForTest = new Task
                (Task.Type.TASK,"InMemoryTaskManagerTest","Task test",
                        Task.Status.NEW,25,subtask.getStartTime().format(Task.DATE_TIME_FORMATTER));
        Epic epicForTest = new Epic
                (Task.Type.EPIC,"InMemoryTaskManagerTest", "test Epic",
                        Task.Status.NEW,90,task.getStartTime().format(Task.DATE_TIME_FORMATTER));
        SubTask subtaskForTest =new SubTask
                (Task.Type.SUBTASK,"InMemoryTaskManagerTest","test Subtask",
                        Task.Status.NEW,10,epic.getStartTime().format(Task.DATE_TIME_FORMATTER),2);
        taskForTest.setTaskId(111);
        epicForTest.setTaskId(222);
        subtaskForTest.setTaskId(333);
        manager.createTask(taskForTest);
        manager.createEpic(epicForTest);
        manager.createSubTask(subtaskForTest);
        assertNull(manager.getTaskById(111),"You screwed up again, task is created");
        assertNull(manager.getEpicById(222),"You screwed up again, epic is created");
        assertNull(manager.getSubtaskById(333),"You screwed up again, subtask is created");

    }

    @Test
    void shouldNotCreateAnyTaskIfStartTimeCrossesTimePeriodOfAnyExistingTasksInsideTheRange() {
        Task taskForTest = new Task
                (Task.Type.TASK,"InMemoryTaskManagerTest","Task test",
                        Task.Status.NEW,25,subtask.getStartTime().plusMinutes(1).format(Task.DATE_TIME_FORMATTER));
        Epic epicForTest = new Epic
                (Task.Type.EPIC,"InMemoryTaskManagerTest", "test Epic",
                        Task.Status.NEW,90,task.getStartTime().plusMinutes(1).format(Task.DATE_TIME_FORMATTER));
        SubTask subtaskForTest =new SubTask
                (Task.Type.SUBTASK,"InMemoryTaskManagerTest","test Subtask",
                        Task.Status.NEW,10,epic.getStartTime().plusMinutes(1).format(Task.DATE_TIME_FORMATTER),2);
        taskForTest.setTaskId(111);
        epicForTest.setTaskId(222);
        subtaskForTest.setTaskId(333);
        manager.createTask(taskForTest);
        manager.createEpic(epicForTest);
        manager.createSubTask(subtaskForTest);
        assertNull(manager.getTaskById(111),"You screwed up again, task is created");
        assertNull(manager.getEpicById(222),"You screwed up again, epic is created");
        assertNull(manager.getSubtaskById(333),"You screwed up again, subtask is created");
    }

}