public class PROCESS {
    String id;
    Integer priority;
    Integer Burst_time;
    PROCESS(  String id, Integer priority,Integer Burst_time){
        this.priority=priority;
        this.id=id;
        this.Burst_time=Burst_time;
    }

    public Integer getPriority() {
        return priority;
    }
}
