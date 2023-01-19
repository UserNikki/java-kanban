package manager;

import task.Epic;
import task.SubTask;
import task.Task;

public interface TaskManager {

    HistoryManager getHistory();

     Epic createEpic(Epic epic);

     SubTask createSubTask(SubTask subtask);

     Task createTask(Task task);

     void deleteAllTasks();

     void deleteAllEpics();

     void deleteAllSubtasks();

     void deleteTaskById(int id);

     void deleteEpicById(int id);

     void deleteSubtaskById(int id);

     String getAllTasks();

     String getAllEpics();

     String getAllSubtasks();

     void displaySubtaskByEpicId(int epicId);

     Epic getEpicById(int epicId);

     Task getTaskById(int taskId);

     SubTask getSubtaskById(int subtaskId);

     Task updateTask(int taskId, Task task);

     Epic updateEpic(int epicId, Epic epic);

     SubTask updateSubtask(int subtaskId, SubTask subtask);


}

