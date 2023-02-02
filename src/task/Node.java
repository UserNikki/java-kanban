package task;

import java.util.Objects;

public class Node<T> {

    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T data,Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {//если не затруднит, объясни почему при реализации этого метода
        return "\nNode{" + //со всеми полями класса вылетает steakoverflowerror
                "data=" + data +
                '}';
    }
}
