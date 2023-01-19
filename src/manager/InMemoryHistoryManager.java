package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List <Task> history;

    private static final int MAX_HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }


    @Override
    public List<Task> getHistory() {
        return history;
        }

    @Override
    public void add(Task task)  {
        history.add(task);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "history=" + history +
                '}';
    }
}
