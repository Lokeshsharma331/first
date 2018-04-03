public class PROCESS {
    String id;
    Integer arrival_time;
    Integer Burst_time;
    PROCESS(  String id, Integer arrival_time,Integer Burst_time){
        this.arrival_time=arrival_time;
        this.id=id;
        this.Burst_time=Burst_time;
    }

    public Integer getArrival_time() {
        return arrival_time;
    }
}
