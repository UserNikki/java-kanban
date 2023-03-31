package server;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import manager.FileBackedTasksManager;
import manager.Managers;
import task.Epic;
import task.SubTask;
import task.Task;

public class HttpTaskServer {
    private FileBackedTasksManager manager;
    private static final int PORT = 8080;

    HttpServer httpServer;

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private int getIdFromQuery(String query) {
        String[] split = query.split("=");
        return Integer.parseInt(split[1]);
    }

    public void launchServer() throws IOException {
        this.httpServer = HttpServer.create();
        this.manager = Managers.getFileManager(new File("src/data.csv"));
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new AllTasksHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен.Порт " + PORT + " Добавь эндпойнт к http://localhost:8080/");
    }
    public void stop() {
        httpServer.stop(1);
    }

    public FileBackedTasksManager getManager() {
        return manager;
    }

    public static void main(String[] args) throws IOException {
        //отдельный запуск апи для проверки
        //в классе мейн отдельный мейн для проверки клиент-валью


        HttpTaskServer server = new HttpTaskServer();
        server.launchServer();
        FileBackedTasksManager manager = server.getManager();

        Task task = new Task
                (Task.Type.TASK,"name","description", Task.Status.NEW,100,"26/03/2023/11:59");
        Task task1 = new Task
                (Task.Type.TASK,"name1","description1", Task.Status.NEW,55,"25/03/2023/18:59");
        Epic epic = new Epic
                (Task.Type.EPIC,"name Epic", "description Epic", Task.Status.NEW,10,"20/03/2023/07:00");
        SubTask subTask = new SubTask
                (Task.Type.SUBTASK,"Subtask","description", Task.Status.NEW,15,"20/03/2023/11:00",3);
        SubTask subTask1 = new SubTask
                (Task.Type.SUBTASK,"Subtask1","description1", Task.Status.NEW,20,"20/03/2023/12:00",3);
        SubTask subTask2 = new SubTask
                (Task.Type.SUBTASK,"Subtask2","description2", Task.Status.NEW,30,"20/03/2023/18:00",3);

        manager.createTask(task);
        manager.createTask(task1);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.getTaskById(1);
        manager.getSubtaskById(4);

    }

    class AllTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка AllTasksHandler.");
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String endPoint = path[path.length - 1];
            if (endPoint.equals("tasks")) {
                List<Task> tasks = new ArrayList<>(manager.taskMap.values());
                tasks.addAll(manager.epicMap.values());
                tasks.addAll(manager.subTaskMap.values());
                String response = GSON.toJson(tasks);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                String response = "Ошибка в обработке запроса";
                httpExchange.sendResponseHeaders(400, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка TaskHandler.");
            String requestMethod = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String query = httpExchange.getRequestURI().getQuery();
            System.out.println(query);
            System.out.println(path[path.length - 1]);
            String endPoint = path[path.length - 1];
            if (requestMethod.equals("DELETE")) {
                if (query != null) {
                    System.out.println(getIdFromQuery(query));
                    manager.deleteTaskById(getIdFromQuery(query));
                    String response = "task deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("task")) {
                    manager.deleteAllTasks();
                    String response = "all tasks deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }

            if (requestMethod.equals("GET")) {
                if (query != null) {
                    Task task = manager.getTaskById(getIdFromQuery(query));

                    String response = GSON.toJson(task);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                 if (endPoint.equals("task")) {
                     List<Task> tasks = new ArrayList<>(manager.taskMap.values());
                         if (tasks.isEmpty()) {
                             System.out.println("tasks is empty");
                             httpExchange.sendResponseHeaders(200, 0);
                             try (OutputStream os = httpExchange.getResponseBody()) {
                                 os.write("there are no tasks".getBytes());
                             }
                             return;
                         }
                         String response = GSON.toJson(tasks);
                         httpExchange.sendResponseHeaders(200, 0);
                         try (OutputStream os = httpExchange.getResponseBody()) {
                             os.write(response.getBytes());
                         }
                    return;
                }
                else {
                    String response = "Error";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }


            }
            if (requestMethod.equals("POST")) {
                if (query != null)  {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Task task = new Task(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.updateTask(getIdFromQuery(query), task);
                    String response = "Task is updated";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("task")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Task task = new Task(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.createTask(task);
                    String response = "Task is created";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }

            }



        }
    }

    class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка EpicHandler.");
            String requestMethod = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String query = httpExchange.getRequestURI().getQuery();
            String endPoint = path[path.length - 1];
            System.out.println(query);
            System.out.println(endPoint);
            if (requestMethod.equals("DELETE")) {
                if (query != null)  {
                    manager.deleteEpicById(getIdFromQuery(query));
                    String response = "Epic deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("epic")) {
                    manager.deleteAllEpics();
                    String response = "all epics deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }

            if (requestMethod.equals("GET")) {
                if (query != null) {
                    Epic epic = manager.getEpicById(getIdFromQuery(query));
                    String response = GSON.toJson(epic);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("epic")) {
                    List<Epic> epics = new ArrayList<>(manager.epicMap.values());
                    if (epics.isEmpty()) {
                        System.out.println("epics is empty");
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write("there are no epics".getBytes());
                        }
                        return;
                    }
                    String response = GSON.toJson(epics);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }

            }
            if (requestMethod.equals("POST")) {
                if (query != null)  {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Epic epic = new Epic(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.updateEpic(getIdFromQuery(query), epic);
                    String response = "Epic is updated";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("epic")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Epic epic = new Epic(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.createEpic(epic);
                    String response = "Epic is created";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        }
    }

    class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка SubtaskHandler.");
            String requestMethod = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String endPoint = path[path.length - 1];
            String query = httpExchange.getRequestURI().getQuery();
            System.out.println(query);
            System.out.println(endPoint);

            if (requestMethod.equals("DELETE")) {
                if (query != null)  {
                    manager.deleteSubtaskById(getIdFromQuery(query));
                    String response = "subtask deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("subtask")) {
                    manager.deleteAllSubtasks();
                    String response = "all subtasks deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }

            if (requestMethod.equals("GET")) {
                if (query != null)  {
                    SubTask subtask = manager.getSubtaskById(getIdFromQuery(query));
                    String response = GSON.toJson(subtask);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("subtask")) {
                    List<SubTask> subtasks = new ArrayList<>(manager.subTaskMap.values());
                    if (subtasks.isEmpty()) {
                        System.out.println("tasks is empty");
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write("there are no subtasks".getBytes());
                        }
                        return;
                    }
                    String response = GSON.toJson(subtasks);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
            if (requestMethod.equals("POST")) {
                if (query != null)  {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    SubTask subtask = new SubTask(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString(),jsonObject.get("epicId").getAsInt());
                    manager.updateSubtask(getIdFromQuery(query), subtask);
                    String response = "Subtask is updated";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (endPoint.equals("subtask")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    SubTask subtask = new SubTask(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString(),jsonObject.get("epicId").getAsInt());
                    manager.createSubTask(subtask);
                    String response = "SubTask is created";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        }
    }

    class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка HistoryHandler.");
            String requestMethod = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String endPoint = path[path.length - 1];
            if (requestMethod.equals("GET")) {
                if (endPoint.equals("history")) {
                    List<Task> historyList = manager.getHistory().getHistory();
                    if (historyList.isEmpty()) {
                        String response = "history is empty";
                        httpExchange.sendResponseHeaders(400, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        return;
                    }
                    String response = GSON.toJson(historyList);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }

                }
                else {
                    String response = "Ошибка в обработке запроса";
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        }
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime dateTime) throws IOException {
            jsonWriter.value(dateTime.format(Task.DATE_TIME_FORMATTER));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), Task.DATE_TIME_FORMATTER);
        }
    }

}