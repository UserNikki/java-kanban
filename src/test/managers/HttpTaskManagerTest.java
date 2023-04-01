package test.managers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import task.Epic;
import task.SubTask;
import task.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    protected HttpTaskManager manager;
    private HttpTaskServer httpTaskServer = new HttpTaskServer();
    private KVServer kvServer;
    Gson gson = HttpTaskServer.GSON;

    /* ТО ПО ОТДЕЛЬНОСТИ ЗАПУСКАЙ
    И СМОТРИ ЧТОБЫ ДАННЫЕ В ФАЙЛЕ "src/data.csv" БЫЛИ КОРРЕКТНЫМИ, ЕСЛИ ТЕСТ НЕ РАБОТАЕТ ОЧИСТИ ФАЙЛ И
     ЗАПУСТИ МЕЙН В HttpTaskServer КЛАССЕ ЧТОБЫ ЗАГРУЗИЛОСЬ. ЕСТЬ БЛУЖДАЮЩИЙ БАГ, Я ВООБЩЕ НЕ ПОНИМАЮ ГДЕ ЕГО ИСКАТЬ
     Т.К. С ВИДУ В КОДЕ ВСЕ НОРМ И ИСПОЛЬЗУЮТСЯ ОДНИ И ТЕ ЖЕ ЦЕПОЧКИ МЕТОДОВ.

            для инсомния если что:
    все вообще http://localhost:8080/tasks
    все задачи http://localhost:8080/tasks/task
    все эпики  http://localhost:8080/tasks/epic
    все сабы   http://localhost:8080/tasks/subtask
    история http://localhost:8080/history
    все что по айди :
    http://localhost:8080/tasks/task/?id=1
    http://localhost:8080/tasks/epic/?id=3
    http://localhost:8080/tasks/subtask/?id=4
     */

    private void createTestData(HttpTaskManager httpTaskManager) {//все айди с 1 по 6 в порядке создания
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

        httpTaskManager.createTask(task);
        httpTaskManager.createTask(task1);
        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubTask(subTask);
        httpTaskManager.createSubTask(subTask1);
        httpTaskManager.createSubTask(subTask2);
        httpTaskManager.getTaskById(1);
        httpTaskManager.getSubtaskById(4);
    }

    @BeforeEach
     void createDataBeforeEach() throws IOException {
        this.httpTaskServer = new HttpTaskServer();
        this.kvServer = new KVServer();
        this.kvServer.start();
        this.httpTaskServer.launchServer();
        createTestData(new HttpTaskManager("http://localhost:8078/"));
        this.manager = HttpTaskManager.loadFromServer("http://localhost:8078/");
    }

    @AfterEach
    void serverStop() {
        this.kvServer.stop();
        this.httpTaskServer.stop();
    }


    @Test
    void shouldGetAllTasksTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("KURWA" + gson.toJson(manager.taskMap.values()));
            System.out.println("KURWA" + response.body());//совпадают
            assertEquals(gson.toJson(manager.taskMap.values()), response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldGetAllSubTasksTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.subTaskMap.values()), response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }
    @Test
    void shouldGetTaskByIdCorrectIdTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getTaskById(1)), response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldGetTaskByIdWrongIdTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=777");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getTaskById(777)),response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldGetEpicByIdWrongIdTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=777");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getEpicById(777)),response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldGetSubtaskByIdCorrectIdTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getSubtaskById(4)), response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldGetSubTaskByIdWrongIdTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=777");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getSubtaskById(777)),response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldCreateTaskTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskForTest = new Task
                (Task.Type.TASK,"POST method test","description", Task.Status.NEW,10,"30/03/2023/11:11");
        String json = gson.toJson(taskForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Task is created", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldCreateSubtaskTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subtaskForTest = new SubTask(Task.Type.SUBTASK,"Post method test","description",
                Task.Status.NEW,15,"30/03/2023/22:22",3);
        String json = gson.toJson(subtaskForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("SubTask is created", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void shouldUpdateTaskTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        Task taskForTest = new Task
                (Task.Type.TASK,"POST method test","description",
                        Task.Status.NEW,10,"31/03/2023/11:11");
        String json = gson.toJson(taskForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Task is updated", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldUpdateSubtaskTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        SubTask subtaskForTest = new SubTask(Task.Type.TASK,"POST method test","description",
                        Task.Status.NEW,10,"31/03/2023/03:11",3);
        String json = gson.toJson(subtaskForTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Subtask is updated", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void shouldDeleteAllTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("all tasks deleted", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void shouldDeleteAllEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("all epics deleted", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldDeleteAllSubtask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("all subtasks deleted", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldDeleteTaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("task deleted", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldDeleteEpicById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Epic deleted", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldDeleteSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("subtask deleted", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnMessageIfHistoryIsEmptyTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("history is empty", response.body());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldThrowIOExceptionAndNotCreateSubtaskWithWrongJsonBodyTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subtaskForTest = new SubTask(Task.Type.SUBTASK,"Post method test","description",
                Task.Status.NEW,15,"30/03/2023/22:22",3);
        String json = gson.toJson(manager.getAllSubtasks());
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        final IOException exp = assertThrows(IOException.class,
                () -> {HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());}
        );
    }

    @Test
    void shouldThrowIOExceptionAndNotCreateEpicWithWrongJsonBodyTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(manager.getAllEpics());
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        final IOException exp = assertThrows(IOException.class,
                () -> {HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());}
        );
    }

    @Test
    void shouldThrowIOExceptionAndNotCreateTaskWithWrongJsonBodyTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(manager.getAllTasks());
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        final IOException exp = assertThrows(IOException.class,
                () -> {HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());}
        );
    }

    @Test
    void shouldReturnMessageIfTaskEndpointDoesNotExistTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/gettask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Error", response.body());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnMessageIfEpicEndpointDoesNotExistTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/getepic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Ошибка в обработке запроса", response.body());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnMessageIfSubtaskEndpointDoesNotExistTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/getsubtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Ошибка в обработке запроса", response.body());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnMessageIfHistoryEndpointDoesNotExistTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Ошибка в обработке запроса", response.body());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnMessageIfAllTasksEndpointDoesNotExistTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/getall");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Ошибка в обработке запроса", response.body());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}