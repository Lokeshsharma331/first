import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MAIN {
    private static List<PROCESS> l=new ArrayList<>();
    public static void main(String[] args) {

            l.add(new PROCESS("P1",4,5));
            l.add(new PROCESS("P2",2,7));
            l.add(new PROCESS("P3",1,4));
            l.add(new PROCESS("P4",3,2));
            l.sort(Comparator.comparing(PROCESS::getPriority));
            int completion_time[]=new int[l.size()],waiting_time[]=new int[l.size()],i=0,average_waiting_time=0;
            waiting_time[0]=0;
            for (PROCESS p:l){
                waiting_time[i]=completion_time[i-1==-1?0:i-1];
                completion_time[i]=completion_time[i-1==-1?0:i-1]+p.Burst_time;

                System.out.println(p.id+"   "+"  "+completion_time[i]);
                i++;

            }
            for(Integer wait:waiting_time){
                System.out.println(wait);
                average_waiting_time+=wait;
            }
            System.out.println("AVERAGE WAITING TIME "+average_waiting_time/l.size());
        }
    }
