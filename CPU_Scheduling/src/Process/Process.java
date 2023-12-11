package Process;

public class Process {
    private String name;
    private float color;
    private int arrivalTime;
    private int brustTime;
    private int remainingTime;
    private int priority;

    private int waitingTime;
    private int turnaroundTime;
    private int completionTime;
    boolean completed = false;
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

    public float getColor() {
        return color;
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

    public void setRemainingTime(int time) {
        this.remainingTime = time;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public void setCompletionTime(int time) {
        this.completionTime = time;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public boolean getCompleted()
    {
        return this.completed;
    }

    // ______________________________________________________________________
    public Process(String name, float color, int arrivalTime, int brustTime, int priority) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.brustTime = brustTime;
        this.remainingTime = brustTime;
        this.priority = priority;
    }
}