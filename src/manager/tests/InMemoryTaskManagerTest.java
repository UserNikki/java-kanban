package manager.tests;

import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
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
    this.historyManager = Managers.getDefaultHistory();
    this.task = manager.createTask(new Task
            (Task.Type.TASK,"InMemoryTaskManagerTest","Task test",
                    Task.Status.NEW,100,"10/03/2023/18:59"));
    this.epic = manager.createEpic(new Epic
            (Task.Type.EPIC,"InMemoryTaskManagerTest", "test Epic",
                    Task.Status.NEW,90,"10/03/2023/20:00"));
    this.subtask = manager.createSubTask(new SubTask
            (Task.Type.SUBTASK,"InMemoryTaskManagerTest","test Subtask",
                    Task.Status.NEW,10,"10/03/2023/18:05",2));
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
  }

  @Test
  void mustCountEpicStatusAsNewTest() {
      Epic epicForTest = manager.createEpic(new Epic
              (Task.Type.EPIC,"Epic for countStatusTest", "test Epic",
                      Task.Status.IN_PROGRESS,10,"10/03/2023/12:00"));
      epicForTest.setTaskId(666);
      assertEquals(Task.Status.NEW, epicForTest.getStatus(),"Статус не новый");//проверка что независимо от указанного, при создании статус устанавливается как новый
      assertTrue(epicForTest.getSubTaskIdList().isEmpty(),"Список сабтасок не пуст");

  }

  @Test
  void mustCountEpicStatusAsDoneTest() {//не понимаю почему список сабтасок пуст внутри эпика
      Epic epicForTest = manager.createEpic(new Epic //из-за этого статус внутри теста не расчитывается
              (Task.Type.EPIC,"Epic for countStatusTest", "test Epic",//но в мейне все работает как надо,т.е методы в порядке
                      Task.Status.NEW,10,"10/03/2023/12:00"));//расчет статуса идет внутри метода createSubtask
      epicForTest.setTaskId(666);//айди сетаю чтобы не зависеть от внутренностей в @BeforeEach
      SubTask subTaskForTest = manager.createSubTask(new SubTask
              (Task.Type.SUBTASK,"subtask for countStatusTest","test Subtask",
                      Task.Status.DONE,10,"10/03/2023/18:05",666));
      subTaskForTest.setTaskId(10);
      assertEquals(Task.Status.DONE, epicForTest.getStatus(),"Статус не done");

  }

  @Test
  void mustCountEpicStatusAsInProgressTest() {
      Epic epicForTest = manager.createEpic(new Epic
              (Task.Type.EPIC,"Epic for countStatusTest", "test Epic",
                      Task.Status.NEW,10,"10/03/2023/12:00"));
      epicForTest.setTaskId(666);
      SubTask subTaskForTest = manager.createSubTask(new SubTask
              (Task.Type.SUBTASK,"subtask for countStatusTest","test Subtask",
                      Task.Status.DONE,10,"10/03/2023/18:05",666));
      subTaskForTest.setTaskId(10);
      SubTask subTaskForTest1 = manager.createSubTask(new SubTask
              (Task.Type.SUBTASK,"subtask for countStatusTest","test Subtask",
                      Task.Status.NEW,10,"10/03/2023/18:05",666));
      subTaskForTest.setTaskId(10);
      assertEquals(Task.Status.IN_PROGRESS, epicForTest.getStatus(),"Статус не in progress");
  }

  @Override
  @Test
  void mustGetAllTasksTest () {
   String allTasks = manager.getAllTasks();
   assertNotNull(allTasks, "Задачи на возвращаются.");
  }

  @Override
  @Test
  void mustGetAllEpicsTest() {
   String allEpics = manager.getAllEpics();
   assertNotNull(allEpics, "Задачи на возвращаются.");
  }

  @Override
  @Test
  void mustGetAllSubtasksTest() {
   String allSubtasks = manager.getAllSubtasks();
   assertNotNull(allSubtasks, "Задачи на возвращаются.");
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
   assertNull(manager.getTaskById(1), "Задачи на возвращаются.");
  }

  @Override
  @Test
  void mustDeleteAllEpicsTest() {
    manager.deleteAllEpics();
    assertNull(manager.getEpicById(2));
  }

  @Override
  @Test
  void mustDeleteAllSubtasksTest() {
   manager.deleteAllSubtasks();
   assertNull(manager.getSubtaskById(3));
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
    }

    @Override
    @Test
    void mustGetEpicByIdTest() {
        Epic epic = manager.getEpicById(2);
        assertNotNull(epic, "Задачи на возвращаются.");
    }

    @Override
    @Test
    void mustGetSubtaskByIdTest() {
        SubTask subTask = manager.getSubtaskById(3);
        assertNotNull(subTask, "Задачи на возвращаются.");
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
      List<Task> historyListForTest = historyManager.getHistory();
      assertEquals(historyListForTest.size(), historyManager.getHistory().size());
      assertArrayEquals(historyListForTest.toArray(),historyManager.getHistory().toArray());
      assertNotNull(historyListForTest);
    }
    @Test
    void mustAddElementToHistoryTest() {
      historyManager.add(task);
      assertEquals(task, historyManager.getHistory().get(0),"different tasks");
    }

    @Test
    void mustRemoveElementFromHistoryTest() {
      historyManager.add(task);
      historyManager.add(epic);
      int index = historyManager.getHistory().indexOf(task);
      historyManager.remove(task.getTaskId());
      assertNotEquals(historyManager.getHistory().get(index), task);
    }

}