package activities.setup;

import org.osbot.rs07.script.MethodProvider;

public abstract class Task extends MethodProvider {
    public abstract boolean isNeededToStartAtGE();
    public abstract boolean validate();
    public abstract void execute(int type);
}
