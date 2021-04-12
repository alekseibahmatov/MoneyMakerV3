package models;

public class Timetable {
    private int maxTasksPerDay, minIntervalBetweenSameTasks, minTime, maxTime;

    public Timetable(int minTime, int maxTime, int maxTasksPerDay, int minIntervalBetweenSameTasks) {
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.maxTasksPerDay = maxTasksPerDay;
        this.minIntervalBetweenSameTasks = minIntervalBetweenSameTasks;
    }

    public int getMinTime() {
        return minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public int getMaxTasksPerDay() {
        return maxTasksPerDay;
    }

    public int getMinIntervalBetweenSameTasks() {
        return minIntervalBetweenSameTasks;
    }
}
