package activities.GrindingDesertGoatHorns;

import activities.setup.Task;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class GrindingDesertGoatHorns extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if(!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return getInventory().contains("Desert goat horn") && getInventory().contains("Pestle and mortar");
    }

    @Override
    public void execute(int type) {
        if(getBank().isOpen()) getBank().close();
        else if(getGrandExchange().isOpen()) getGrandExchange().close();

        switch (type) {
            case 1:
                if (getInventory().contains("Pestle and mortar"))
                    getInventory().getItem("Pestle and mortar").interact();
                Sleep.sleepUntil(random(100, 300));
                if (getInventory().contains("Desert goat horn")) getInventory().getItem("Desert goat horn").interact();
                break;
            case 2:
                if (getInventory().getAmount("Desert goat horn") + getInventory().getAmount("Goat horn dust") == 27) {
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
                    if (getInventory().contains("Pestle and mortar"))
                        getInventory().getItem("Pestle and mortar").interact();
                    Sleep.sleepUntil(random(100, 300));
                    if (getInventory().contains("Desert goat horn"))
                        getInventory().getItem("Desert goat horn").interact();
                }
                break;
        }
    }
}
