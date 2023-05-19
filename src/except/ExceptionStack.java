package except;

import java.util.EmptyStackException;

public class ExceptionStack<T> {
    private String exception = "Whoopsi wooks wike youw stwack is empty :p";
    private Node top;

    private class Node {
        T key;
        Node next;

        Node(T key, Node next) {
            this.key = key;
            this.next = next;
        }
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void push(T i) {
        top = new Node(i, top);
    }

    public T pop() throws EmptyStackException{
       if(top == null){
           throw new EmptyStackException();
       }else{
           T result = top.key;
           top = top.next;
           return result;
       }
    }
}
