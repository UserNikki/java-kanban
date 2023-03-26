package manager;
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
import task.Epic;
import task.SubTask;
import task.Task;

public class HttpTaskServer {
    private FileBackedTasksManager manager;
    private static final int PORT = 8080;

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public void launchServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        this.manager = Managers.getFileManager(new File("src/data.csv"));
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен.Порт " + PORT + " Добавь эндпойнт к http://localhost:8080/");
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка TaskHandler.");
            String requestMethod = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");

            if (requestMethod.equals("DELETE")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    manager.deleteSubtaskById(id);
                    String response = "task deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("task")) {
                    manager.deleteAllTasks();
                    String response = "all tasks deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }

            }

            if (requestMethod.equals("GET")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    Task task = manager.getTaskById(id);
                    String response = GSON.toJson(task);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("tasks")) {
                    List<Task> tasks = new ArrayList<>(manager.taskMap.values());
                    String response = GSON.toJson(tasks);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                if (path[path.length - 1].equals("task")) {
                    List<Task> tasks = new ArrayList<>(manager.taskMap.values());
                    String response = GSON.toJson(tasks);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }

            }
            if (requestMethod.equals("POST")) {//проверил, работает
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Task task = new Task(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.updateTask(id, task);
                    String response = "Task is updated: " + manager.getTaskById(id);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("task")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Task task = new Task(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.createTask(task);
                    String response = "Task is created: " + task.getTaskId() + task;
                    httpExchange.sendResponseHeaders(200, 0);
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
            System.out.println(path[path.length - 1]);

            if (requestMethod.equals("DELETE")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    manager.deleteEpicById(id);
                    String response = "Epic deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("epic")) {
                    manager.deleteAllEpics();
                    String response = "all epics deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }

            if (requestMethod.equals("GET")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    Epic epic = manager.getEpicById(id);
                    String response = GSON.toJson(epic);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("epic")) {
                    List<Epic> epics = new ArrayList<>(manager.epicMap.values());
                    String response = GSON.toJson(epics);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }

            }
            if (requestMethod.equals("POST")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Epic epic = new Epic(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.updateEpic(id, epic);
                    String response = "Epic is updated: " + manager.getEpicById(id);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("epic")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Epic epic = new Epic(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString());
                    manager.createEpic(epic);
                    String response = "Task is created: " + epic.getTaskId() + epic;
                    httpExchange.sendResponseHeaders(200, 0);
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
            System.out.println("Началась обработка EpicHandler.");
            String requestMethod = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            System.out.println(path[path.length - 1]);

            if (requestMethod.equals("DELETE")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    manager.deleteSubtaskById(id);
                    String response = "subtask deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("subtask")) {
                    manager.deleteAllSubtasks();
                    String response = "all subtasks deleted";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }

            }

            if (requestMethod.equals("GET")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    SubTask subtask = manager.getSubtaskById(id);
                    String response = GSON.toJson(subtask);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("subtask")) {
                    List<SubTask> subtasks = new ArrayList<>(manager.subTaskMap.values());
                    String response = GSON.toJson(subtasks);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }

            }
            if (requestMethod.equals("POST")) {
                if (httpExchange.getRequestURI().getQuery().contains("id")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println(query);
                    String[] split = query.split("=");
                    int id = Integer.parseInt(split[1]);
                    System.out.println(id);
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    SubTask subtask = new SubTask(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString(),jsonObject.get("epicId").getAsInt());
                    manager.updateSubtask(id, subtask);
                    String response = "Epic is updated: " + manager.getSubtaskById(id);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }
                if (path[path.length - 1].equals("subtask")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    SubTask subtask = new SubTask(Task.Type.valueOf(jsonObject.get("type").getAsString()),
                            jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(),
                            Task.Status.valueOf(jsonObject.get("status").getAsString()), jsonObject.get("duration").getAsLong(),
                            jsonObject.get("startTime").getAsString(),jsonObject.get("epicId").getAsInt());
                    manager.createSubTask(subtask);
                    String response = "Task is created: " + subtask.getTaskId() + subtask;
                    httpExchange.sendResponseHeaders(200, 0);
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