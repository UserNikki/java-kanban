package manager;
import server.HttpTaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8080/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static FileBackedTasksManager getFileManager(File file) {
        return FileBackedTasksManager.loadFromFile(file);
    }

}


