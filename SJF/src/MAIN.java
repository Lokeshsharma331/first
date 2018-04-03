import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MAIN {
    private static List<PROCESS> l=new ArrayList<>();
    private static List<PROCESS> waiting_process=new ArrayList<>();
    private static int time=0;
    public static void main(String[] args) {
        l.add(new PROCESS("P1",0,6));
        l.add(new PROCESS("P2",3,1));
        l.add(new PROCESS("P3",2,2));
        l.add(new PROCESS("P4",2,1));
        l.sort(Comparator.comparing(PROCESS::getArrival_time));
        int completion_time[]=new int[l.size()];
        System.out.println(l.get(l.size() - 1).arrival_time);
        while (!has_all_completed()) {
            if(l.get(l.size() - 1).arrival_time >= time) {
                addprocess();
            }
                waiting_process.sort(Comparator.comparing(PROCESS::getBurst_time));
                execute();

        }


    }
    private  static void addprocess(){
        for (PROCESS p:l){
            if(time==p.arrival_time) {
                waiting_process.add(p);

            }
        }

    }
    private static void execute(){
        waiting_process.get(0).Burst_time-=1;
        System.out.println(waiting_process.get(0).id+"  "+waiting_process.get(0).Burst_time);
        time++;
        if(waiting_process.get(0).Burst_time==0){
            waiting_process.get(0).has_completed=true;
            System.out.println("COMPLETED"+"    "+waiting_process.get(0).id+"   at   "+time);
            waiting_process.remove(0);

        }

    }
    private static boolean has_all_completed(){
        boolean flag=true;
        for (PROCESS p:l){
            if(!p.has_completed){
                flag=false;
            }
        }
        return flag;
    }
}
