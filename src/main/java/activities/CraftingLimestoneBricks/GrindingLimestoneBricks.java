package activities.CraftingLimestoneBricks;

import activities.setup.Task;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class GrindingLimestoneBricks extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return getInventory().contains("Chisel") && getInventory().contains("Limestone");
    }

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        new ConditionalSleep(60000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                return !getBank().isOpen();
            }
        }.sleep();

        log("bank closed");


        switch (type) {
            case 1:
                if (getInventory().contains("Chisel")) getInventory().getItem("Chisel").interact();
                Sleep.sleepUntil(random(100, 300));
                if (getInventory().contains("Limestone")) getInventory().getItem("Limestone").interact();
                break;
            case 2:
                if (getInventory().getAmount("Limestone") +
                        getInventory().getAmount("Rock") +
                        getInventory().getAmount("Limestone brick") == 27) {
                    mouse.move(random(689, 720), random(429, 460));

                    new ConditionalSleep(300, 150) {
                        @Override
                        public boolean condition() {
                            return mouse.getPosition().getX() >= 689 &&
                                    mouse.getPosition().getX() <= 720 &&
                                    mouse.getPosition().getY() >= 429 &&
                                    mouse.getPosition().getY() <= 460;
                        }
                    };

                    mouse.click(false);

                    mouse.move(random(647, 678), random(429, 460));

                    new ConditionalSleep(300, 150) {
                        @Override
                        public boolean condition() {
                            return mouse.getPosition().getX() >= 647 &&
                                    mouse.getPosition().getX() <= 678 &&
                                    mouse.getPosition().getY() >= 429 &&
                                    mouse.getPosition().getY() <= 460;
                        }
                    };

                    mouse.click(false);
                } else {
                    if (getInventory().contains("Chisel")) getInventory().getItem("Chisel").interact();
                    Sleep.sleepUntil(random(100, 300));
                    if (getInventory().contains("Limestone")) getInventory().getItem("Limestone").interact();
                }
                break;

        }
    }
}
