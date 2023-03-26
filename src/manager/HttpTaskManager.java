package manager;

import com.google.gson.*;
import task.Epic;
import task.SubTask;
import task.Task;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {
    protected String taskKey;
    protected String epicKey;
    protected String subtaskKey;
    protected String historyKey;
    protected KVTaskClient clientKV;
    private static Gson gson;

    public HttpTaskManager(URI uri) {
        super();
        taskKey = "task";
        epicKey = "epic";
        subtaskKey = "subtask";
        historyKey = "history";
        clientKV = new KVTaskClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .serializeNulls()
                .create();

    }

    @Override
    public void save() {//робит
        if (!taskMap.isEmpty()) {
            List<Task> tasks = new ArrayList<>(taskMap.values());
            String valueTasks = gson.toJson(tasks);
            clientKV.putData(taskKey,valueTasks);
        }
        if (!epicMap.isEmpty()) {
            List<Epic> epics = new ArrayList<>(epicMap.values());
            String valueEpics = gson.toJson(epics);
            clientKV.putData(epicKey,valueEpics);
        }
        if (!subTaskMap.isEmpty()) {
            List<SubTask> subtasks = new ArrayList<>(subTaskMap.values());
            String valueSubtasks = gson.toJson(subtasks);
            clientKV.putData(subtaskKey,valueSubtasks);
        }
        clientKV.putData(historyKey, historyToString(historyManager));
    }




    public KVTaskClient getClientKV() {
        return clientKV;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public String getEpicKey() {
        return epicKey;
    }

    public String getSubtaskKey() {
        return subtaskKey;
    }

    public String getHistoryKey() {
        return historyKey;
    }

    @Override
    public Task createTask(Task task) {
        if (timeValidator(task)) {
            super.createTask(task);
            save();
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (timeValidator(epic)) {
            super.createEpic(epic);
            save();
        }
        return epic;
    }


    @Override
    public SubTask createSubTask(SubTask subtask) {
        if (timeValidator(subtask)) {
            super.createSubTask(subtask);
            save();
        }
        return subtask;
    }

    @Override
    public String getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public String getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public String getAllSubtasks() {
        return super.getAllSubtasks();
    }


}