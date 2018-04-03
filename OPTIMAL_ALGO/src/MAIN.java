import java.util.ArrayList;
import java.util.List;

public class MAIN {
    private static int pages[]= { 7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2 };
    private static int frame_size=3,current_page_index=0;
    private static List<Integer> frame=new ArrayList<>();
    public static void main(String[] args) {
        optimalalgo();
    }
    private static int pagetoreplace(){
        int index_to_use[]=new int[frame_size],k=0,flag;
        for(int s:frame){
            index_to_use[k]=14;
            flag=0;
            for(int i=current_page_index;i<pages.length;i++){

                if(s==pages[i]&&flag==0){
                    index_to_use[k]=i;

                    flag=1;

                }
            }
            k++;

        }

        int max=0,frame_index_to_replace=0;
        for (int i=0;i<index_to_use.length;i++){

            if(index_to_use[i]>max){
                max=index_to_use[i];
                frame_index_to_replace=i;
            }
        }
        return frame_index_to_replace;
    }
    private static void optimalalgo(){
        for(int i=0;i<pages.length;i++){
            if(frame.contains(pages[i])){
                continue;
            }
            if(frame.size()<frame_size){
                frame.add(pages[i]);
            }
            else{
                current_page_index=i;
                frame.set(pagetoreplace(),pages[i]);

            }
            for(int s:frame){
                System.out.print(s);
            }
            System.out.println();
        }
    }
}
