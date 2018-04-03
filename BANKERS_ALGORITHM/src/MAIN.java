import java.util.Scanner;
import java.util.Vector;

public class MAIN {
    private static int no_of_resource=0;
    private static int no_of_process=0;
    private static int [] available;
    private static int[][] max;
    private static int allocation[][],need[][];
    private static boolean finish[];
    public static void main(String[] args) {

        Scanner in=new Scanner(System.in);
        System.out.println("Enter no. of processes:");
        no_of_process=in.nextInt();
        System.out.println("Enter no. of resources:");
        no_of_resource=in.nextInt();
        available=new int[no_of_resource];
        finish=new boolean[no_of_process];
        max=new int[no_of_process][no_of_resource];
        allocation=new int[no_of_process][no_of_resource];
        need=new int[no_of_process][no_of_resource];
        for(int i=0;i<no_of_process;i++){
            finish[i]=false;
        }


        for (int i=0;i<no_of_process;i++){
            System.out.println("Enter no. of instance allocated for "+i +"th process");
            for (int j=0;j<no_of_resource;j++){
                allocation[i][j]=in.nextInt();
            }
        }
        for (int i=0;i<no_of_process;i++){
            System.out.println("Enter no. of instance may request at most for "+i +"th process");
            for (int j=0;j<no_of_resource;j++){
                max[i][j]=in.nextInt();
            }
        }
        for(int i=0;i<no_of_resource;i++){
            System.out.println("Enter no. of " + i +"th resource instances available:");
            available[i]=in.nextInt();
        }
        for (int i=0;i<no_of_process;i++){
            for (int j=0;j<no_of_resource;j++){
                need[i][j]=max[i][j]-allocation[i][j];
            }
        }
        for (int i=0;i<no_of_process;i++){
            for (int j=0;j<no_of_resource;j++){
                System.out.println(need[i][j]);
            }
        }
        while (!allfinished()){
            for(int i=0;i<no_of_process;i++){
                if(request_complete(i)&&!finish[i]){

                    for (int j=0;j<no_of_resource;j++){
                        available[j]+=allocation[i][j];

                        }
                    finish[i]=true;
                    System.out.println("completed   "+ i +"th process");
                }
            }
        }





    }
    private static boolean request_complete(int processno){
        for(int i=0;i<no_of_resource;i++){
            if(need[processno][i]>available[i]){
                return false;
            }
        }
        return true;

    }
    private static boolean allfinished(){

        for (int i=0;i<no_of_process;i++){
            if(!finish[i]){
                return false;
            }
        }
        return true;
    }
}
