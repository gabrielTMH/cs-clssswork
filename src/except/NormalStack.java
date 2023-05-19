package except;

public class NormalStack<T> {
    private int size;
    private Node top;
    private class Node{
        T key;
        Node next;
        Node(T key, Node next){
            this.key=key;
            this.next=next;
        }
    }
    public NormalStack(){
        size=0;
        top=null;
    }
    public boolean isEmpty() { return top==null;    }

    public void push(T i) {
        top = new Node(i, top);
    }

    public T pop() {
        if (!this.isEmpty()) {
            T result = top.key;
            top = top.next;
            return result;
        }
        return null;
    }
}
