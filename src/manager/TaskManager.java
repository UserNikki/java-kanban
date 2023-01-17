package manager;

import task.Epic;
import task.SubTask;
import task.Task;

public interface TaskManager {
//

    public Epic createEpic(Epic epic);

    public SubTask createSubTask(SubTask subtask);

    public void countEpicStatus(int epicId);

    public Task createTask(Task task);

    public void deleteAllTasks();

    public void deleteAllEpics();

    public void deleteAllSubtasks();

    public void deleteTaskById(int id);

    public void deleteEpicById(int id);

    public void deleteSubtaskById(int id);

    public String getAllTasks();

    public String getAllEpics();

    public String getAllSubtasks();

    public void displaySubtaskByEpicId(int epicId);

    public Epic getEpicById(int epicId);

    public Task getTaskById(int taskId);

    public SubTask getSubtaskById(int subtaskId);

    public Task updateTask(int taskId, Task task);

    public Epic updateEpic(int epicId, Epic epic);

    public SubTask updateSubtask(int subtaskId, SubTask subtask);


}

