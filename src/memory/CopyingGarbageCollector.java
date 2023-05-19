package memory;

public class CopyingGarbageCollector extends MemoryManager {
    private int toSpace;
    private int fromSpace;
    public CopyingGarbageCollector(int capacity) {
        //memory should be twice capitiy, so we have a space to copy into
        super(capacity*2);
        //these deliniate the begining of reachable and unreachable memory
        toSpace=capacity;
        fromSpace=0;
        //free starst at the begining of the memory
        setFree(fromSpace);


    }

    @Override
    public int allocate(){
        if (0==unusedCount()){
            GarbageCollect();
            if(0==unusedCount()){
                return NONE;
            }
        }
        int res = getFree();
        setFree(res+2);
        set(res+1,NONE);
        set(res,NONE);
        return res;
    }

    public void GarbageCollect(){
        setFree(toSpace);
        if (getRoot()!=NONE){
            copy(getRoot(),toSpace);
            sweep(toSpace, getRoot());
            setRoot(toSpace);
        }
        //swapping to space and from space so that next garbage collection process we copy memory to the right place
        int x=fromSpace;
        fromSpace=toSpace;
        toSpace=x;
    }

    public void sweep(int adress,int content){
        int current=get(content);
        while(current<getFree()) {//allready have forwaded the root
            //check base case when we reach end of usefull memory
            if (content != NONE) {  //&& adress< getCapacity()/2+toSpace
                //need to make sure that we are not copying somthing allready copied
                if ((get(content) < toSpace && toSpace != 0) || (get(content) >= fromSpace && toSpace == 0)) {
                    copy(content, adress);
                    forward(adress, content);
                    adress+=2;
                }else{
                    //some kind of set

                    //copy(get(content),adress+2);
                    //set(content,adress);
                }
                current+=1;
                //forward(adress, content);

            }
        }
    }

    public void forward(int adress,int content){
         set(content,adress);
    }

    public void copy( int content,int adress){
        //copies into new adress what is stored at content
        set(adress, get(content));
        set(adress + 1, get(content + 1));
        setFree(getFree()+2);
        forward(adress,content);

    }
    @Override
    public int unusedCount() {
        //might not need to be here
        if ((getFree()==getCapacity()&&fromSpace!=0) || (getFree()==(getCapacity()/2)&&fromSpace==0)){
            return 0;
        }
        return (getCapacity()/2)-(getFree()-fromSpace);
    }


}
