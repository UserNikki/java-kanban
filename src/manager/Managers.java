package manager;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import task.Task;

import java.util.List;

public class Managers  {

    public TaskManager getDefault() {
        return new TaskManager;
    }

    public static  HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager(name);
    }
}
