package test.managers;

import com.google.gson.Gson;
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

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    protected HttpTaskManager manager;
    private static final String PATH = "src/data.csv" ;
    private HttpTaskServer httpTaskServer = new HttpTaskServer();
    private KVServer kvServer;
    Gson gson = HttpTaskServer.GSON;

    /*  ПООТДЕЛЬНОСТИ ВСЕ РАБОТАЮТ. ВМЕСТЕ Я НЕ ПОНИМАЮ ЧТО ИМ НУЖНО.
    В ИНСОМНИИ ВСЕ РАБОТАЕТ, ЗДЕСЬ НЕ МОГУ НАСТРОИТЬ НОРМАЛЬНО, ЕСЛИ НЕ СЛОЖНО ТО ПО ОТДЕЛЬНОСТИ ЗАПУСКАЙ
    И СМОТРИ ЧТОБЫ ДАННЫЕ В ФАЙЛЕ "src/data.csv" БЫЛИ КОРРЕКТНЫМИ, ЕСЛИ ТЕСТ НЕ РАБОТАЕТ ОЧИСТИ ФАЙЛ И
     ЗАПУСТИ МЕЙН В HttpTaskServer КЛАССЕ ЧТОБЫ ЗАГРУЗИЛОСЬ. ЕСТЬ БЛУЖДАЮЩИЙ БАГ, Я ВООБЩЕ НЕ ПОНИМАЮ ГДЕ ЕГО ИСКАТЬ
     А ПЕРЕПИСЫВАТЬ ВСЕ ЗАНОВО УЖЕ ВСЕ, ВРЕМЕНИ НЕТ. СДАЮСЬ.ВСЕ ЧТО МОГ СДЕЛАЛ. СЛИШКОМ МНОГО ВСЕГО С ДЖСОН, АДАПТЕРАМИ
     И Т.Д. ТОЛКОМ НЕ ИЗУЧАЛИ И СРАЗУ ВОТ В ЭТО ВСЕ ЗА 2 НЕДЕЛИ ПОПРОБУЙ ПЕРЕВАРИ ГОВОРЯТ.
     ЕСЛИ ЧТО ПРИДУМАЮ ДОЗАКИНУ, ПУШУ ПОТОМУ ЧТО ВРЕМЯ ВСЕ
     */

    private void createTestData(HttpTaskManager httpTaskManager) {
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
        createTestData(new HttpTaskManager("http://localhost:8078"));
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
    void shouldGetAllEpicsTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("KURWA" + gson.toJson(manager.epicMap.values()));
            System.out.println("KURWA" + response.body());
            assertEquals(gson.toJson(manager.epicMap.values()), response.body());
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
            System.out.println("KURWA" + gson.toJson(manager.subTaskMap.values()));
            System.out.println("KURWA" + response.body());
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
    void shouldGetEpicByIdCorrectIdTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getEpicById(3)), response.body());
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
    void shouldCreateEpicTest() {//вот понятия не имею откуда тут наллпойнтерэксепшн
        HttpClient client = HttpClient.newHttpClient();//это выше моего понимания. Не должно его тут быть
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicTest = new Epic
                (Task.Type.EPIC,"POST TEST", "Epic",
                        Task.Status.NEW,15,"31/03/2023/07:59");
        String json = gson.toJson(epicTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Epic is created", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e ) {
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
    void shouldUpdateEpicTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        Epic epicTest = new Epic
                (Task.Type.EPIC,"POST TEST", "Epic",
                        Task.Status.NEW,15,"31/03/2023/01:59");
        String json = gson.toJson(epicTest);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Epic is updated", response.body());
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
    void shouldGetHistoryTest() {//из инсомнии, постмана работает, здесь ошибок не вижу, код правильный
        HttpClient client = HttpClient.newHttpClient();//хэндлер истории рабочий
        URI url = URI.create("http://localhost:8080/history");//понятия не имею в чем дело, что мог выжал из себя
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();//слишком много неизвестного под капотом теперь
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getHistory().getHistory()), response.body());
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

}