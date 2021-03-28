package models;

public class TaskData {
    private int taskID, taskParam;
    private long taskDuration;

    public TaskData(int taskID, int taskParam, long taskDuration) {
        this.taskID = taskID;
        this.taskParam = taskParam;
        this.taskDuration = taskDuration;
    }

    public int getTaskID() {
        return taskID;
    }

    public int getTaskParam() {
        return taskParam;
    }

    public long getTaskDuration() {
        return taskDuration;
    }
}
