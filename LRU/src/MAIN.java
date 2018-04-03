import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MAIN {
    private static List<Integer> set=new ArrayList<>();
    private static Map<Integer,Integer> indexes=new HashMap<>();
    private static int capacity=4,INT_MAX=999;
    public static void main(String[] args) {
        int pages[]={7,0,6,1,0,3,0,4,2,0,3,2};
        int page_faults = 0;
        for (int i=0; i<pages.length; i++) {
            if (set.size() < capacity) {
                if (!set.contains(pages[i])) {
                    set.add(pages[i]);
                    page_faults++;
                    for (Integer s:set){
                        System.out.print(s);
                    }
                    System.out.println();

                }
                indexes.put(pages[i], i);
            } else {
                if (!set.contains(pages[i])) {
                    int lru = INT_MAX, val = 0;
                    for (int j:set) {
                        if (indexes.get(j) < lru) {
                            lru = indexes.get(j);
                            val = j;
                        }
                    }
                    set.remove(set.indexOf(val));
                    set.add(pages[i]);
                    for (Integer s:set){
                        System.out.print(s);
                    }
                    page_faults++;
                    System.out.println();

                }
                indexes.put(pages[i], i);

            }
        }


    }
}
