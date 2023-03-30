package server;

import com.google.gson.*;
import manager.FileBackedTasksManager;
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

    public HttpTaskManager(String url) {
        super();
        taskKey = "task";
        epicKey = "epic";
        subtaskKey = "subtask";
        historyKey = "history";
        clientKV = new KVTaskClient(URI.create(url));
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .serializeNulls()
                .create();
    }

    @Override
    public void save() {
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

    public static HttpTaskManager loadFromServer(String url) {
        HttpTaskManager manager = new HttpTaskManager(url);
        JsonArray taskArray = JsonParser.parseString(manager.getClientKV()
                .load(manager.getTaskKey())).getAsJsonArray();
        if (taskArray != null) {
            for (JsonElement task : taskArray) {
                Task taskFromJson = gson.fromJson(task, Task.class);
                int id = taskFromJson.getTaskId();
                manager.taskMap.put(id, taskFromJson);
            }
        }
            JsonArray epicArray = JsonParser.parseString(manager.getClientKV()
                    .load(manager.getEpicKey())).getAsJsonArray();
            if (epicArray != null) {
                for (JsonElement epic : epicArray) {
                    Epic epicFromJson = gson.fromJson(epic, Epic.class);
                    int id = epicFromJson.getTaskId();
                    manager.epicMap.put(id, epicFromJson);
                }
            }
        JsonArray subtaskArray = JsonParser.parseString(manager.getClientKV()
                .load(manager.getSubtaskKey())).getAsJsonArray();
        if (subtaskArray != null) {
            for (JsonElement subtask : subtaskArray) {
                SubTask subtaskFromJson = gson.fromJson(subtask, SubTask.class);
                int id = subtaskFromJson.getTaskId();
                manager.subTaskMap.put(id, subtaskFromJson);
            }
        }
        List<Integer> list = new ArrayList<>(historyFromString(manager.getClientKV()
                .load(manager.getHistoryKey())));
        for (Integer id : list) {
            if (manager.taskMap.containsKey(id)) {
                manager.historyManager.add(manager.taskMap.get(id));
            }
            if (manager.epicMap.containsKey(id)) {
                manager.historyManager.add(manager.epicMap.get(id));
            }
            if (manager.subTaskMap.containsKey(id)) {
                manager.historyManager.add(manager.subTaskMap.get(id));
            }
        }

        return manager;
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