package manager;

import task.Node;
import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    public Map<Integer, Node<Task>> history;

    private final List<Node<Task>> historyList = new LinkedList<>();

    private static final int MAX_HISTORY_SIZE = 10;

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;


    public InMemoryHistoryManager() {
        this.history = new HashMap<>();
    }

    public int getHistorySize() {
            return this.size;
        }

    public List<Node<Task>> getTasks(Map<Integer,Node<Task>> list) {
        return new ArrayList<>(list.values());
        }



    @Override
    public List<Task> getHistory() {
        List<Task> historyTaskList = new ArrayList<>();
        for (Node<Task> node : history.values()) {
            historyTaskList.add(node.getData());
        }
        return historyTaskList;
    }

    @Override
    public void add(Task task)  {
    try {
        removeNode(history.get(task.getTaskId()));
        if (history.size() >= MAX_HISTORY_SIZE) {
            removeNode(historyList.get(MAX_HISTORY_SIZE - 1));
            historyList.remove(MAX_HISTORY_SIZE - 1);
        }
        historyList.add(0, linkLast(task));
        history.put(task.getTaskId(), linkLast(task));
    }
        catch (NullPointerException o) {
            o.getStackTrace();
        }
    }
    @Override
    public void remove(int id) {
        history.remove(id);
    }


    void removeNode(Node<Task> node) {
        if (history.containsValue(node)) {
           int id = node.data.getTaskId();
           history.remove(id);
        }
    }

    private Node<Task> linkLast(Task element) {
        final Node<Task> oldTail =  this.tail;
        final Node<Task> newNode = new Node<>(oldTail, element,  null);
        this.tail = newNode;
        if (oldTail == null) {
            this.head = newNode;
        } else {
            oldTail.next = newNode;
        }
        ++this.size;

        return newNode;
    }

    public List<Task> getHead() {
        List<Task> list = new ArrayList<>();
        for (Node<Task> i : history.values()) {
           list.add(i.getData());
        }
        return list;
    }

    /*@Override
    public String toString() {
        //String str = String.format("%s,%s", history.keySet(),history.values());
        return "InMemoryHistoryManager" + history.values();
    }*/

    @Override
    public String toString() {
        return "InMemoryHistoryManager" + history.values() ;
    }
}
