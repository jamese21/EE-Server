public class Packet
{
//    Flow qosClass;

    //bits per second
//    double maxSusRate, minResRate;

    //milliseconds

    String maxJitter;
    String maxLatency;
    String data;
    long startTime, endTime;

    public Packet(String data, String maxJitter, String maxLatency)
    {
        this.maxJitter = maxJitter;
        this.data = data;
        this.maxLatency = maxLatency;
    }

    public String getMaxJitter() {
        return maxJitter;
    }

    public String getData() {
        return data;
    }

    public String getMaxLatency() {
        return maxLatency;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        endTime = System.currentTimeMillis();
    }

    public long getLatency()
    {
        return endTime-startTime;
    }
}
