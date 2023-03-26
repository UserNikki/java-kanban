package test.managers;

import manager.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    FileBackedTasksManager manager;
    protected HistoryManager historyManager;
    protected HttpTaskManager httpTaskManager ;
    private static final String PATH = "src/data.csv" ;



    @BeforeEach
     void createTestData() throws IOException {//правильно ли я настраиваю окружение?
        new HttpTaskServer().launchServer();
        new KVServer().start();
        this.manager = new FileBackedTasksManager(new File(PATH));
        this.httpTaskManager = new HttpTaskManager(URI.create("http://localhost:8078/"));
        httpTaskManager.getClientKV().load("task");
        httpTaskManager.getClientKV().load("epic");
        httpTaskManager.getClientKV().load("subtask");
    }

    @Test
    void shouldGetAllTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        httpTaskManager.getAllTasks();
        assertEquals(httpTaskManager.getTaskById(1), response.body());
    }
    /*@Test
    void shouldGetTask() {
        assertEquals(httpTaskManager.getAllTasks(),httpTaskManager.getClientKV().load("task"));
    }*/





}