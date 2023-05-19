package memory;

public class MarkAndSweepGarbageCollector extends MemoryManager {
    private boolean[] marked;
    public MarkAndSweepGarbageCollector(int i) {
        super(i);
        marked = new boolean[i];
    }

    @Override
    public int allocate() {
        if (super.getFree() == NONE) {
            mark(getRoot());
            if (markedTrue(marked)){
                return NONE;
            }else{
                sweep();
            }
        }
        return super.allocate();
    }

    private void mark(int i) {
        if (i<0) {
            return;
        }
        marked[i]=true;
        if (get(i)!=i && get(i+1)!=i) {
            mark(get(i));
            mark(get(i + 1));
        }
    }

    private boolean markedTrue(boolean[] a){
        for (int i =0; i<a.length;i++){
            if (a[i]==false){
                return false;
            }
        }
        return true;
    }

    private void sweep(){
        for (int i =0; i<getCapacity()-1; i+=2){
            if (marked[i]==true){
                marked[i]=false;
            }else{
                int x = getFree();
                setFree(i);
                set(i,x);
            }
        }
    }
}
