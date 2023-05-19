package memory;

public class ReferenceCounter extends MemoryManager {
    private int[] counter;

    public ReferenceCounter(int capacity) {
        super(capacity);
        counter = new int [capacity];
    }

    public void decrementCount(int address){
        if(address >= 0) {
            counter[address] -= 1;
            if (counter[address] == 0) {
                decrementCount(get(address));
                decrementCount(get(address+1));
                super.set(address, getFree());
                setFree(address);
            }
        }
    }

    @Override
    public void setRoot(int address){
        int oldrt = getRoot();
        super.setRoot(address);
        if(address >= 0){
            counter[getRoot()] += 1;
        }
        decrementCount(oldrt);
    }

    @Override
    public void set(int address, int val){
        int old = get(address);
        super.set(address, val);
        if(val >= 0){
            counter[val] += 1;
        }
        decrementCount(old);
    }
}
