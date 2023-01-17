package manager;

import task.Task;
import task.SubTask;
import task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
