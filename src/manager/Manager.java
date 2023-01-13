package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.*;

public class Manager {
    private Map<Integer,Task> taskMap = new HashMap<>();
    private Map<Integer,Epic> epicMap = new HashMap<>();
    private Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private int id = 1;

    public Epic createEpic(Epic epic) {
        int epicId = generateId();
        epic.setTaskId(epicId);
        epic.setStatus(Task.Status.NEW);
        epicMap.put(epicId, epic);
        return epic;
    }

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
                System.out.println("task.Epic does not exist...");
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
                    System.out.println("task.Epic does not exist...");
                }
            }
        }
    }

    public Task createTask(Task task) {
        int taskId = generateId();
        task.setTaskId(taskId);
        taskMap.put(taskId, task);
        return task;
    }

    public void deleteAllTasks() {
        taskMap.clear();
    }

    public void deleteAllEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }

    public void deleteAllSubtasks() {
        subTaskMap.clear();
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    public void deleteEpicById(int id) {
        epicMap.remove(id);
        subTaskMap.values().removeIf(value -> value.getEpicId() == id);
    }

    public void deleteSubtaskById(int id) {
        subTaskMap.remove(id);
    }

    public String getAllTasks() {
        return taskMap.values().toString();
    }

    public String getAllEpics() {
        return epicMap.values().toString();
    }

    public String getAllSubtasks() {
        return subTaskMap.values().toString();
    }

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
            System.out.println("task.Epic does not exist or contain any subtasks...");
        }
    }

    public Epic getEpicById(int epicId) {
        return epicMap.get(epicId);
    }

    public Task getTaskById(int taskId) {
        return taskMap.get(taskId);
    }

    public SubTask getSubtaskById(int subtaskId) {
        return subTaskMap.get(subtaskId);
    }

    public Task updateTask(int taskId, Task task) {
        taskMap.put(taskId, task);
        return task;
    }

    public Epic updateEpic(int epicId, Epic epic) {
        epicMap.put(epicId, epic);
        countEpicStatus(epicId);
        return epic;
    }

    public SubTask updateSubtask(int subtaskId, SubTask subtask) {
        subTaskMap.put(subtaskId, subtask);
        countEpicStatus(subtask.getEpicId());
        return subtask;
    }
    private int generateId () {
        return id++;
    }

}

