import java.util.*;

public class Manager {
    Map<Integer,Task> taskMap = new HashMap<>();
    Map<Integer,Epic> epicMap = new HashMap<>();
    Map<Integer,ArrayList<SubTask>> subTaskMap = new HashMap<>();
    int generateId = 1;


    public Epic createEpic(Epic epic) {
        epic.setEpicId(generateId++);
        epicMap.put(epic.getEpicId(),epic);
        return epic;
    }
    public Epic getEpicById(int epicId) {
        return epicMap.get(epicId);
    }
    public String getAllEpics() {
        return epicMap.entrySet().toString();
    }
    public Epic updateEpic(int epicId, Epic epic) {
        epicMap.put(epicId, epic);
        countEpicStatus(epicId);
        return epic;
    }
    public void deleteEpicById(int epicId) {
        epicMap.remove(epicId);
    }
    public void deleteAllEpics() {
        epicMap.clear();
    }

    public SubTask createSubTask(SubTask subtask, int epicId) {
        if (epicMap.containsKey(epicId) && subTaskMap.containsKey(epicId)) {
            subTaskMap.get(epicId).add(subtask);
        }
        else if (epicMap.containsKey(epicId) && !subTaskMap.containsKey(epicId)) {
            ArrayList<SubTask> subTaskList = new ArrayList<>();
            subTaskList.add(subtask);
            subTaskMap.put(epicId,subTaskList);
        }
        countEpicStatus(epicId);

        else {
            System.out.println("The ID of an existing EPIC is required!"
                    + "You can create a subtask only within the existing EPIC...");
        }
        return subtask;
    }


    public String getSubtasksList(int epicId) {
        try {
            return subTaskMap.get(epicId).toString();
        } catch (NullPointerException e) {
            System.out.println("Error...List is empty!");
            return Collections.emptyList().toString();
        }
    }

    public void countEpicStatus(int epicId) {

        boolean isDone = false;
        boolean isNew = false;

        if (epicMap.containsKey(epicId) && subTaskMap.containsKey(epicId)) {

            for (SubTask subtask : subTaskMap.get(epicId)) {
                if (subtask.epicStatus.equals(Status.DONE)) {
                    epicMap.get(epicId).setEpicStatus(Status.DONE);
                    isDone = true;
                }
                if (subtask.epicStatus.equals(Status.NEW)) {
                    epicMap.get(epicId).setEpicStatus(Status.NEW);
                    isNew = true;
                }
            }
            if (isNew == isDone) {
                epicMap.get(epicId).setEpicStatus(Status.IN_PROGRESS);
            }
        }
        else if (epicMap.containsKey(epicId) && !subTaskMap.containsKey(epicId)) {
            epicMap.get(epicId).setEpicStatus(Status.NEW);
        }
        else {
            System.out.println("This epic does not exist...");
        }
    }

    public String getAllTasks() {

        return taskMap.entrySet().toString();
    }

    public Task createTask(Task task) {
        task.setTaskID(generateId++);
        taskMap.put(task.getTaskID(), task);
        return task;
    }


    public String getAllSubTasks() {

        return subTaskMap.entrySet().toString();
    }

    public void deleteAllTasks() {
        taskMap.clear();
    }
    public Task getTaskById(int id) {
        Task task = null;
            if (taskMap.containsKey(id)) {
                task = taskMap.get(id);
            }
            return task;
    }



}

