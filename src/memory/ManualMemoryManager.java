package memory;

public class ManualMemoryManager extends MemoryManager {
    public ManualMemoryManager(int capacity) {
        super(capacity);
    }

    public void release(int address) {
        set(address, getFree());
        setFree(address);
    }
    public void releaseList(int address){
        if(address==NONE){
            return;
        }
        releaseList(get(address));
        release(address);
    }

    public static void main(String[] args) {
        ManualMemoryManager m = new ManualMemoryManager(10);
        m.setRoot(m.allocate());
        int next = m.getRoot();
        m.setRoot(m.allocate());
        m.set(m.getRoot(),next);
        next = m.getRoot();
        m.setRoot(m.allocate());
        m.set(m.getRoot(),next);
        m.releaseList(m.getRoot());
        System.out.println(m.unusedCount());

    }
}
