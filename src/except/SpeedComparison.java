package except;

import except.ExceptionStack;
import except.NormalStack;

public class SpeedComparison {
    //well the first tests should never reach null or an exception so they are usually similar in time somtimes one is slighty faster sometimes the other is
    //For the test when we pop an empty stack 1000 times it is way faster to return a special character than an exception
    public static void main(String[] args) {
        ExceptionStack<Integer> s = new ExceptionStack<>();
        NormalStack<Integer> t = new NormalStack<>();
        long timeStart= System.nanoTime();
        for (int i=0;i<10000;i++){t.push(1);        }
        for (int i=0;i<10000;i++){t.pop();        }
        long timeFinish= System.nanoTime();
        System.out.println("normal stack test one " );
        System.out.println(timeFinish-timeStart);
        t = new NormalStack<>();
        timeStart= System.nanoTime();
        for (int i=0;i<10000;i++){s.push(1);         }
        for (int i=0;i<10000;i++){s.pop();}
        timeFinish= System.nanoTime();
        System.out.println("exception stack test one " );
        System.out.println(timeFinish-timeStart);
        s = new ExceptionStack<>();
        timeStart= System.nanoTime();
        for (int i=0;i<10000;i++){
            try {
                s.pop();
            }
            catch(Exception e) {
                //  Block of code to handle errors
            }

        }
        timeFinish= System.nanoTime();
        System.out.println("exception stack test two");
        System.out.println(timeFinish-timeStart);



        timeStart= System.nanoTime();
        for (int i=0;i<10000;i++){
           try {
               t.pop();
           }
           catch(Exception e){

           }
        }
        timeFinish= System.nanoTime();
        System.out.println("normal stack test two");
        System.out.println(timeFinish-timeStart);
    }

}
