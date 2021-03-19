package activities.CollectionPlanks;

import activities.setup.Task;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class CollectingPlanks extends Task {

    private Area planksSpawningArea = new Area(2552, 3577, 2555, 3574);
    private Area planksBank = new Area(2536, 3575, 2533, 3571);

    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        Area checkArea = new Area(2515, 3590, 2557, 3534);

        return checkArea.contains(myPosition());
    }

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();

        if (getGrandExchange().isOpen()) getGrandExchange().close();

        if ((getInventory().contains("Ring of wealth (5)") ||
                getInventory().contains("Ring of wealth (4)") ||
                getInventory().contains("Ring of wealth (3)") ||
                getInventory().contains("Ring of wealth (2)") ||
                getInventory().contains("Ring of wealth (1)")) &&
                (getInventory().contains("Games necklace(8)") ||
                        getInventory().contains("Games necklace(7)") ||
                        getInventory().contains("Games necklace(6)") ||
                        getInventory().contains("Games necklace(5)") ||
                        getInventory().contains("Games necklace(4)") ||
                        getInventory().contains("Games necklace(3)") ||
                        getInventory().contains("Games necklace(2)") ||
                        getInventory().contains("Games necklace(1)"))) {
            try {
                depositAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            if (!getInventory().isFull()) collectPlanks();
            else depositPlanks();
        }
    }

    private void depositAll() throws InterruptedException {
        if (!planksBank.contains(myPosition())) getWalking().webWalk(planksBank);
        else {
            RS2Object bankChest = objects.closest("Bank chest");

            getCamera().toEntity(bankChest);
            sleep(random(300, 500));

            // depositing all items to bank
            if (bankChest != null && bankChest.isVisible()) {
                bankChest.interact();
                new ConditionalSleep(10000, 1000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        log("sleep until see depositAll widget");
                        RS2Widget depositAllWidget = getWidgets().get(12, 41);
                        return (depositAllWidget != null && depositAllWidget.isVisible());
                    }
                }.sleep();
                RS2Widget depositAllWidget = getWidgets().get(12, 41);
                if (depositAllWidget != null && depositAllWidget.isVisible()) {
                    depositAllWidget.interact();
                }
            }
        }
    }

    private void collectPlanks() {

        if (!planksSpawningArea.contains(myPosition())) getWalking().webWalk(planksSpawningArea);
        else {
            log("arrived to the plank spawning area");

            new ConditionalSleep(10000, 1000) {
                @Override
                public boolean condition() throws InterruptedException {
                    log("sleep until see planks");
                    GroundItem plank = getGroundItems().closest("Plank");
                    getCamera().toEntity(plank);
                    return (plank != null && plank.isVisible());
                }
            }.sleep();
            log("planks targeted");

            // collecting planks untill inventory is full
            while (!getInventory().isFull()) {
                GroundItem plank = getGroundItems().closest("Plank");
                // targeting dialog with pidaras kotorii herit vsju malinu
                RS2Widget pidarasDialog = getWidgets().get(231, 3);
                if (pidarasDialog != null && pidarasDialog.isVisible()) {
                    log("targeted dialog with pidaras kotorii herit vsju malinu");
                    break;
                }
                if (plank != null) {
                    if (!plank.isVisible()) {
                        getCamera().toEntity(plank);
                    }
                    Sleep.sleepUntil(random(500, 750));
                    plank.interact();
                    Sleep.sleepUntil(random(500, 750));
                    getCamera().movePitch(random(44, 67));
                    getCamera().moveYaw(random(154, 200));
                } else getWorlds().hopToP2PWorld();

                Sleep.sleepUntil(random(500, 750));
            }
            log("inventory is full of planks");
        }
    }

    private void depositPlanks() {
        if (!planksBank.contains(myPosition())) getWalking().webWalk(planksBank);
        else {
            RS2Object chestBank1 = objects.closest("Bank chest");

            getCamera().toEntity(chestBank1);

            Sleep.sleepUntil(random(300, 500));

            //depositing all inventory to bank

            chestBank1.interact();

            new ConditionalSleep(10000, 1000) {
                @Override
                public boolean condition() throws InterruptedException {
                    log("sleep until see depositAll widget");
                    RS2Widget depositAllWidget = getWidgets().get(12, 41);
                    return (depositAllWidget != null && depositAllWidget.isVisible());
                }
            }.sleep();

            RS2Widget depositAllWidget = getWidgets().get(12, 41);
            if (depositAllWidget != null && depositAllWidget.isVisible()) {
                depositAllWidget.interact();
            }


            log("deposited all inventory");

            Sleep.sleepUntil(random(1000, 1250));
        }
    }
}
