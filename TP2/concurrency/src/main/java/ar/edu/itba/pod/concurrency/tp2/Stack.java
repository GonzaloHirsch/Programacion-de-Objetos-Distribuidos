package ar.edu.itba.pod.concurrency.tp2;

// Implementacion thread safe del stack
public class Stack {
    private static final int MAX_SIZE = 10;
    private int top = 0;
    private final int[] values = new int[MAX_SIZE];
    private final String LOCK = "LOCK";

    public void push(final int n) {
        synchronized (LOCK) {
            if (top == MAX_SIZE) {
                throw new IllegalStateException("stack full");
            }
            values[top++] = n;
        }
    }

    public int pop() {
        synchronized (LOCK) {
            if (top == 0) {
                throw new IllegalStateException("stack empty");
            }
            return values[--top];
        }
    }

//    public void push(final int n) {
//        if (top == MAX_SIZE) {
//            throw new IllegalStateException("stack full");
//        }
//        values[top++] = n;
//    }
//
//    public int pop() {
//        if (top == 0) {
//            throw new IllegalStateException("stack empty");
//        }
//        return values[--top];
//    }

    public int[] getItems(){
        return values;
    }

    public int getTop(){
        return this.top;
    }
}
