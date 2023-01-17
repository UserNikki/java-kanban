package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List <Task> history;

    public InMemoryHistoryManager(List<Task> history) {
        this.history = history;
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
    @Override
    public void add(Task task)  {
        history.add(task);
    }

}
