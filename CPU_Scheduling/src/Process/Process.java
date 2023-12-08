package Process;

public class Process {
    private String name;
    private float[] hsbColorArray = { 1.0f, 1.0f, 1.0f };
    private int arrivalTime;
    private int brustTime;
    private int priority;

    private int waitingTime;
    private int turnaroundTime;
    // ______________________________________________________________________

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBrustTime() {
        return brustTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = (priority >= 0 ? priority : 0);
    }

    public float[] hsbColorArray() {
        return hsbColorArray;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    // ______________________________________________________________________
    public Process(String name, float colorHue, int arrivalTime, int brustTime, int priority) {
        this.name = name;
        this.hsbColorArray[0] = colorHue;
        this.arrivalTime = arrivalTime;
        this.brustTime = brustTime;
        this.priority = priority;
    }
}