package utils;

import org.osbot.rs07.api.Client;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.RandomSolver;

public class CustomBreakManager extends RandomSolver {

    private long breakDuration = -1;
    private long breakStartTime = -1;
    private boolean shouldLogout;

    public CustomBreakManager() {
        super(RandomEvent.BREAK_MANAGER);
    }

    public void startBreaking(final long breakDuration, final boolean shouldLogout) {
        this.breakDuration = breakDuration;
        this.breakStartTime = System.currentTimeMillis();
        this.shouldLogout = shouldLogout;
    }

    public long getBreakStartTime() {
        return breakStartTime;
    }

    @Override
    public boolean shouldActivate() {
        return breakDuration > 0 && !finishedBreaking();
    }

    public boolean finishedBreaking() {
        log(System.currentTimeMillis() - breakStartTime + " >= " + breakDuration);
        return System.currentTimeMillis() - breakStartTime >= breakDuration;
    }

    @Override
    public int onLoop() throws InterruptedException {

        if (shouldLogout && getClient().getLoginState() == Client.LoginState.LOGIN_SUCCESSFUL) {
            if (getWidgets().closeOpenInterface() && getLogoutTab().logOut()) {
                return 1000;
            }
        }

        if (getMouse().isOnScreen()) {
            getMouse().moveOutsideScreen();
        }

        return 3000;
    }
}
