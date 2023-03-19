package test.managers;

import manager.HistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import task.Epic;
import task.SubTask;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

  private InMemoryTaskManager manager;
  protected HistoryManager historyManager;
  protected Task task;
  protected Epic epic;
  protected SubTask subtask;




  @Override
  @BeforeEach
  void createTestData() {
    this.manager = new InMemoryTaskManager();
    this.historyManager = manager.getHistory();
    this.task = manager.createTask(new Task
            (Task.Type.TASK,"InMemoryTaskManagerTest","Task test",
                    Task.Status.NEW,25,"25/03/2023/12:00"));
    this.epic = manager.createEpic(new Epic
            (Task.Type.EPIC,"InMemoryTaskManagerTest", "test Epic",
                    Task.Status.NEW,90,"26/03/2023/20:00"));
    this.subtask = manager.createSubTask(new SubTask
            (Task.Type.SUBTASK,"InMemoryTaskManagerTest","test Subtask",
                    Task.Status.NEW,10,"27/03/2023/18:05",2));
  }

  @Override
  @Test
  void createTaskTest() {
   int id = task.getTaskId();
   Task taskToCompareWith = manager.getTaskById(id);
   assertNotNull(taskToCompareWith, "Проверка на null провалена...");
   assertEquals(task, taskToCompareWith, "Задачи не совпадают.");
  }

  @Override
  @Test
  void createEpicTest() {
   int id = epic.getTaskId();
   Epic epicToCompareWith = manager.getEpicById(id);
   assertNotNull(epicToCompareWith, "Проверка на null провалена...");
   assertEquals(epic, epicToCompareWith, "Эпики не совпадают.");
  }

  @Override
  @Test
  void createSubTaskTest() {
   int id = subtask.getTaskId();
   SubTask subTaskToCompareWith = manager.getSubtaskById(id);
   assertNotNull(subTaskToCompareWith, "Проверка на null провалена...");
   assertEquals(subtask, subTaskToCompareWith, "Эпики не совпадают.");
   assertNotNull(subtask.getEpicId(),"отсутствует эпик айди");//Redundant null-check: a value of primitive type 'int' is never null
  }

  @Test
  void mustCountEpicStatusAsNewTest() {
      Epic epicForTest = manager.createEpic(new Epic
              (Task.Type.EPIC,"Epic for countStatusTest", "test Epic",
                      Task.Status.IN_PROGRESS,10,"10/03/2023/12:00"));
      epicForTest.setTaskId(666);
      assertEquals(Task.Status.NEW, epicForTest.getStatus(),"Статус не новый");
      assertTrue(epicForTest.getSubTaskIdList().isEmpty(),"Список сабтасок не пуст");
  }

  @Test
  void mustCountEpicStatusAsDoneTest() {
      Epic epicForTest = new Epic(Task.Type.EPIC,"Epic for countStatusTest", "test Epic",
              Task.Status.NEW,10,"18/03/2023/12:00");
      SubTask subTaskForTest = new SubTask(Task.Type.SUBTASK,"subtask for countStatusTest","test Subtask",
              Task.Status.DONE,20,"18/03/2023/18:05",4);
      manager.createEpic(epicForTest);
      manager.createSubTask(subTaskForTest);
      assertEquals(Task.Status.DONE, epicForTest.getStatus(),"Статус не done");
  }

  @Test
  void mustCountEpicStatusAsInProgressTest() {
      Epic epicForTest = new Epic(Task.Type.EPIC,"Epic for countStatusTest", "test Epic",
              Task.Status.NEW,10,"18/03/2023/12:00");
      SubTask subTaskForTest = new SubTask(Task.Type.SUBTASK,"subtask for countStatusTest","test Subtask",
              Task.Status.DONE,20,"18/03/2023/18:05",4);
      SubTask subTaskForTest1 = new SubTask
              (Task.Type.SUBTASK,"subtask for countStatusTest","test Subtask",
                      Task.Status.NEW,10,"10/03/2023/18:05",4);
      manager.createEpic(epicForTest);
      manager.createSubTask(subTaskForTest);
      manager.createSubTask(subTaskForTest1);
      assertEquals(Task.Status.IN_PROGRESS, epicForTest.getStatus(),"Статус не in progress");
      subTaskForTest.setStatus(Task.Status.IN_PROGRESS);
      subTaskForTest1.setStatus(Task.Status.IN_PROGRESS);
      assertEquals(Task.Status.IN_PROGRESS, epicForTest.getStatus(),"Статус не in progress");
  }

  @Override
  @Test
  void mustGetAllTasksTest () {
   String allTasks = manager.getAllTasks();
   assertNotNull(allTasks, "Задачи не возвращаются.");
   manager.deleteAllTasks();
   assertEquals("[]", manager.getAllTasks());
   assertNull(manager.getTaskById(11));
  }

  @Override
  @Test
  void mustGetAllEpicsTest() {
   String allEpics = manager.getAllEpics();
   assertNotNull(allEpics, "Задачи на возвращаются.");
   manager.deleteAllEpics();
   assertEquals("[]", manager.getAllEpics());
   assertNull(manager.getEpicById(11));
  }

  @Override
  @Test
  void mustGetAllSubtasksTest() {
   String allSubtasks = manager.getAllSubtasks();
   assertNotNull(allSubtasks, "Задачи на возвращаются.");
   manager.deleteAllSubtasks();
   assertEquals("[]", manager.getAllSubtasks());
   assertNull(manager.getSubtaskById(11));
  }

  @Override
  @Test
  void displaySubtaskByEpicIdTest() {
   String subtasks = manager.displaySubtaskByEpicId(epic.getTaskId());
   assertNotNull(subtasks,"Все кончено, ты проиграл Саске. Всё вокруг null...");
  }

  @Override
  @Test
  void mustDeleteAllTasksTest() {
   manager.deleteAllTasks();
   assertNull(manager.getTaskById(1), "Задачи не удалены.");
   assertEquals("[]",manager.getAllTasks());
  }

  @Override
  @Test
  void mustDeleteAllEpicsTest() {
    manager.deleteAllEpics();
    assertNull(manager.getEpicById(2));
    assertEquals("[]",manager.getAllEpics());
  }

  @Override
  @Test
  void mustDeleteAllSubtasksTest() {
   manager.deleteAllSubtasks();
   assertNull(manager.getSubtaskById(3));
   assertEquals("[]",manager.getAllSubtasks());
  }

  @Override
  @Test
  void mustDeleteTaskByIdTest() {
   manager.deleteTaskById(1);
   assertNull(manager.getTaskById(1),"Таска не удалена");
  }

  @Override
  @Test
  void mustDeleteEpicByIdTest() {
   manager.deleteEpicById(2);
   assertNull(manager.getEpicById(2),"epic не удалена");
  }

  @Override
  @Test
  void mustDeleteSubtaskByIdTest() {
   manager.deleteSubtaskById(3);
   assertNull(manager.getSubtaskById(3),"subtask не удалена");
  }

    @Override
    @Test
    void mustGetTaskByIdTest() {
       Task task =  manager.getTaskById(1);
        assertNotNull(task,"Все кончено, ты проиграл Саске. Всё вокруг null...");
        assertNull(manager.getTaskById(55), "Должен быть нал, но увы...");
        manager.deleteAllTasks();
        assertNull(manager.getTaskById(55), "Должен быть нал, но увы...");

    }

    @Override
    @Test
    void mustGetEpicByIdTest() {
        Epic epic = manager.getEpicById(2);
        assertNotNull(epic, "Задачи на возвращаются.");
        assertNull(manager.getEpicById(55), "Должен быть нал, но увы...");
        manager.deleteAllEpics();
        assertNull(manager.getEpicById(55), "Должен быть нал, но увы...");
    }

    @Override
    @Test
    void mustGetSubtaskByIdTest() {
        SubTask subTask = manager.getSubtaskById(3);
        assertNotNull(subTask, "Задачи на возвращаются.");
        assertNull(manager.getSubtaskById(55), "Должен быть нал, но увы...");
        manager.deleteAllSubtasks();
        assertNull(manager.getSubtaskById(55), "Должен быть нал, но увы...");
    }

    @Override
    @Test
    void mustUpdateTaskTest() {
        Task taskForTest = manager.createTask(new Task
            (Task.Type.TASK,"Updated task","Task test",
                Task.Status.NEW,15,"10/03/2023/20:05"));
        manager.updateTask(1,taskForTest);
        assertEquals(manager.getTaskById(1),taskForTest,"разные объекты");
    }

    @Override
    @Test
    void mustUpdateEpicTest() {
        Epic epicForTest = manager.createEpic(new Epic
                (Task.Type.EPIC,"Updated epic", "test Epic",
                        Task.Status.NEW,10,"10/03/2023/20:00"));
        manager.updateEpic(2,epicForTest);
        assertEquals(manager.getEpicById(2),epicForTest,"разные объекты");
    }

    @Override
    @Test
    void mustUpdateSubtaskTest() {
        SubTask subtaskForTest = manager.createSubTask(new SubTask
                (Task.Type.SUBTASK,"updated subtask","test Subtask",
                        Task.Status.NEW,15,"10/03/2023/18:10",2));
        manager.updateSubtask(3,subtaskForTest);
        assertEquals(manager.getSubtaskById(3),subtaskForTest,"разные объекты");
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

    //поля времени плюс пересечения
    //пропустил тесты полей которые просто заносятся, не знаю что там тестировать)
    //я учту с названиями, прсто все переписывать не могу, мне бы 8 спринт пройти и апи писать
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