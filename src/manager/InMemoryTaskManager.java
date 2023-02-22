package manager;
import task.Epic;
import task.Node;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer,Task> taskMap = new HashMap<>();
    protected Map<Integer,Epic> epicMap = new HashMap<>();
    protected Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private int id = 1;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    public HistoryManager getHistory() {
         return historyManager;
    }



    @Override
    public Epic createEpic(Epic epic) {
        int epicId = generateId();
        epic.setTaskId(epicId);
        epic.setStatus(Task.Status.NEW);
        epicMap.put(epicId, epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subtask) {
        int subtaskId = generateId();
        subtask.setTaskId(subtaskId);
        subTaskMap.put(subtaskId, subtask);
        if (epicMap.containsKey(subtask.getEpicId())) {
            for (Integer id : epicMap.keySet()) {
                epicMap.get(id).setSubTaskIdList(subtask.getTaskId());
            }
        }
        else {
            System.out.println("Epic does not exist...");
        }
        countEpicStatus(subtask.getEpicId());

        return subtask;
    }

    public void countEpicStatus(int epicId) {
        boolean isDone = false;
        boolean isNew = false;
        for (SubTask subtask : subTaskMap.values()) {
            if (subtask.getEpicId() == epicId) {
                if (epicMap.containsKey(epicId)) {
                    if (subtask.getStatus().equals(Task.Status.DONE)) {
                        epicMap.get(epicId).setStatus(Task.Status.DONE);
                        isDone = true;
                    }
                    if (subtask.getStatus().equals(Task.Status.NEW)) {
                        epicMap.get(epicId).setStatus(Task.Status.NEW);
                        isNew = true;
                    }
                    if (isNew == isDone) {
                        epicMap.get(epicId).setStatus(Task.Status.IN_PROGRESS);
                    }
                }
                else {
                    System.out.println("Epic does not exist...");
                }
            }
        }
    }

    @Override
    public Task createTask(Task task) {
        int taskId = generateId();
        task.setTaskId(taskId);
        taskMap.put(taskId, task);
        return task;
    }

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subTaskMap.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        taskMap.remove(id);
        getHistory().remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (SubTask subtask : subTaskMap.values()) {
            if (subtask.getEpicId() == id) {
                getHistory().remove(subtask.getTaskId());
            }
        }
        epicMap.remove(id);
        subTaskMap.values().removeIf(value -> value.getEpicId() == id);
        getHistory().remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        subTaskMap.remove(id);
        getHistory().remove(id);
    }

    @Override
    public String getAllTasks() {
        return taskMap.values().toString();
    }

    @Override
    public String getAllEpics() {
        return epicMap.values().toString();
    }

    @Override
    public String getAllSubtasks() {
        return subTaskMap.values().toString();
    }

    @Override
    public void displaySubtaskByEpicId(int epicId) {
        ArrayList <SubTask> subtasksList = new ArrayList<>();
        for (SubTask subtask : subTaskMap.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasksList.add(subtask);
            }
        }
        for (SubTask subtask : subtasksList) {
            System.out.println(subtask);
        }
        if (subtasksList.isEmpty()) {
            System.out.println("Epic does not exist or contain any subtasks...");
        }
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epicMap.get(epicId));
        return epicMap.get(epicId);
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(taskMap.get(taskId));
        return taskMap.get(taskId);
    }

    @Override
    public SubTask getSubtaskById(int subtaskId) {
        historyManager.add(subTaskMap.get(subtaskId));
        return subTaskMap.get(subtaskId);
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        taskMap.put(taskId, task);
        return task;
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        epicMap.put(epicId, epic);
        countEpicStatus(epicId);
        return epic;
    }

    @Override
    public SubTask updateSubtask(int subtaskId, SubTask subtask) {
        subTaskMap.put(subtaskId, subtask);
        countEpicStatus(subtask.getEpicId());
        return subtask;
    }
    private int generateId () {
        return id++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
