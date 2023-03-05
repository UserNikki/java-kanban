package manager;


import task.Epic;
import task.SubTask;
import task.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    /*public static FileBackedTasksManager fileManager(File file) {
       return FileBackedTasksManager.loadFromFile(file);
    }*/
}


