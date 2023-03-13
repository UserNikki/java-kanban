package manager.tests;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

abstract class TaskManagerTest<T extends TaskManager> {



    abstract void createTestData();

    @Test
    void createTaskTest() {

    }

    @Test
    void createEpicTest() {

    }

    @Test
    void createSubTaskTest() {
    }

    @Test
    void mustDeleteAllTasksTest() {
    }

    @Test
    void mustDeleteAllEpicsTest() {
    }

    @Test
    void mustDeleteAllSubtasksTest() {
    }

    @Test
    void mustDeleteTaskByIdTest() {
    }

    @Test
    void mustDeleteEpicByIdTest() {
    }

    @Test
    void mustDeleteSubtaskByIdTest() {
    }

    @Test
    void mustGetAllTasksTest() {
    }

    @Test
    void mustGetAllEpicsTest() {
    }

    @Test
    void mustGetAllSubtasksTest() {
    }

    @Test
    void displaySubtaskByEpicIdTest() {
    }

    @Test
    void mustGetEpicByIdTest() {
    }

    @Test
    void mustGetTaskByIdTest() {
    }

    @Test
    void mustGetSubtaskByIdTest() {
    }

    @Test
    void mustUpdateTaskTest() {
    }

    @Test
    void mustUpdateEpicTest() {
    }

    @Test
    void mustUpdateSubtaskTest() {
    }

}
