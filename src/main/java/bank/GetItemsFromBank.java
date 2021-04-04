package bank;

import bank.utils.BankManager;
import models.TaskItem;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;
import utils.Sleep;

public class GetItemsFromBank extends MethodProvider {
    TaskItem[][][] taskItems = {
            {
                    {
                            new TaskItem("Unicorn horn", 27, true, true),
                            new TaskItem("Unicorn horn dust",true, false),
                            new TaskItem("Pestle and mortar", 1, false, true)
                    }
            },
            {
                    {
                            new TaskItem("Desert goat horn", 27, true, true),
                            new TaskItem("Goat horn dust", true, false),
                            new TaskItem("Pestle and mortar", 1, false, true)
                    }
            },
            {
                    {
                            new TaskItem("Uncut opal",27, true, true),
                            new TaskItem("Opal", true, false),
                            new TaskItem("Crushed gem", true, false),
                            new TaskItem("Chisel", 1, false, true)
                    },
                    {
                            new TaskItem("Limestone",27, true, true),
                            new TaskItem("Limestone brick", true, false),
                            new TaskItem("Chisel", 1, false, true)
                    }
            },
            {
                    {
                            new TaskItem("Supercompost", 27, true, true),
                            new TaskItem("Ultracompost", true, false),
                            new TaskItem("Volcanic ash", 54, true, true),
                    }
            },
//            {
//                    {
//                            new TaskItem("Bullseye lantern (empty)", 25, true, true),
//                            new TaskItem("Swamp tar", 25, true, true),
//                            new TaskItem("Bullseye lantern", true, false),
//                            new TaskItem("Ring of wealth", true, false),
//                            new TaskItem("Teleport to house", 1, true, true),
//                            new TaskItem("Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)", 1, false, true),
//                    },
//                    {
//                            new TaskItem("Falador teleport", 1, true, true),
//                            new TaskItem("Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)", 1, false, true),
//
//                    }
//            },
            {
                    {
                            new TaskItem("Games necklace(1),Games necklace(2),Games necklace(3),Games necklace(4),Games necklace(5),Games necklace(6),Games necklace(7),Games necklace(8)", 1, true, true),
                            new TaskItem("Plank", true, false),
                    }
            },
            {
                    {
                            new TaskItem("Guam leaf", 14, true, true),
                            new TaskItem("Vial of water", 14, 50, true, true),
                            new TaskItem("Guam potion (unf)", true, false),

                    },
                    {
                            new TaskItem("Raw bear meat", 1, true, true),
                            new TaskItem("Raw rat meat", 1, true, true),
                            new TaskItem("Raw beef", 1, true, true),
                            new TaskItem("Raw chicken", 1, true, true),

                    },
            },
            {
                    {
                            new TaskItem("Tarromin", 14, true, true),
                            new TaskItem("Vial of water", 14, 50, true, true),
                            new TaskItem("Tarromin potion (unf)", true, false),
                    },
                    {
                            new TaskItem("Guam potion (unf)", 14, true, true),
                            new TaskItem("Eye of newt",14, true, true),
                            new TaskItem("Attack potion(3)", true, false),
                    }
            },
            {
                    {
                            new TaskItem("Flax",28, true, true),
                            new TaskItem("Bow string", true, false),
                    },
                    {
                            new TaskItem("Uncut opal", 27, true, true),
                            new TaskItem("Opal", true, false),
                            new TaskItem("Crushed gem", true, false),
                            new TaskItem("Chisel", 1, false, true)
                    },
            },
            {
                    {
                            new TaskItem("Mind rune", random(422, 666), 41, true, true),
                            new TaskItem("Water rune", random(422, 666), 23, true, true),
                            new TaskItem("Earth rune", random(422, 666), 61, true, true),
                            new TaskItem("Fire rune", random(422, 666), 44, true, true),
                            new TaskItem("Chaos rune", random(422, 666), 81, true, true),
                            new TaskItem("Tuna", random(18, 21), 3, true, true),
                            new TaskItem("Wooden shield", 1, false, true),
                            new TaskItem("Staff of air", 1, false, true),
                    },
                    {
                            new TaskItem("Law rune", 500, 50, true, true),
                            new TaskItem("Restore potion(1),Restore potion(2),Restore potion(3),Restore potion(4)", 3, 15, true, true),
                            new TaskItem("Zamorak monk top", 1, false, true),
                            new TaskItem("Zamorak monk bottom", 1, false, true),
                            new TaskItem("Staff of air", 1, false, true),
                            new TaskItem("Wine of zamorak", true, false),
                            new TaskItem("Tuna", 10, 3 , true, true),
                    }
            }
    };

    public int getItems(int taskID, int step, BankManager bankManager, boolean depositAll) throws InterruptedException {

        log("Opening bank in GetItemsFromBank");
        bankManager.openBank();

        if (depositAll && (!getInventory().isEmpty() || !getEquipment().isEmpty())) {
            getBank().depositAll();
            Sleep.sleepUntil(random(700, 1200));
            getBank().depositWornItems();
        }

        for (int i = 0; i < taskItems[taskID][step].length; i++) {
            if (getInventory().contains(taskItems[taskID][step][i].getName()) && taskItems[taskID][step][i].isNeededToBeDeposited())
                getBank().depositAll(taskItems[taskID][step][i].getName());
        }

        for (int i = 0; i < taskItems[taskID][step].length; i++) {
            //log(String.format("%s : %b", taskItems[taskID][step][i].getName(), getBank().getItem(taskItems[taskID][step][i].getName()).getAmount() < taskItems[taskID][step][i].getMinAmount()));
            String[] splittedItems = taskItems[taskID][step][i].getName().split(",");
            if (splittedItems.length == 1 &&
                    !(getBank().contains(taskItems[taskID][step][i].getName()) && taskItems[taskID][step][i].getMinAmount() == -1) &&
                    !getEquipment().contains(taskItems[taskID][step][i].getName()) &&
                    !getInventory().contains(taskItems[taskID][step][i].getName()) &&
                    taskItems[taskID][step][i].isNeededToBeTaken() &&
                    (((getBank().getAmount(taskItems[taskID][step][i].getName()) < taskItems[taskID][step][i].getMinAmount()) &&
                            (taskItems[taskID][step][i].getMinAmount() != -1)) || getBank().getAmount(taskItems[taskID][step][i].getName()) == 0)
            ) return -1;
            else if (splittedItems.length > 1) {
                boolean hasAtLeastOneOfListedItems = false;
                for (String item : splittedItems) {
                    if (getBank().contains(item) || getInventory().contains(item)) {
                        hasAtLeastOneOfListedItems = true;
                        break;
                    }
                }

                if (!hasAtLeastOneOfListedItems) return -1;
            }
        }

        for (int i = 0; i < taskItems[taskID][step].length; i++) {
            String[] splittedItems = taskItems[taskID][step][i].getName().split(",");
            if (splittedItems.length == 1 && (!getInventory().contains(taskItems[taskID][step][i].getName()) && !getEquipment().contains(taskItems[taskID][step][i].getName())) && taskItems[taskID][step][i].isNeededToBeTaken())
                getBank().withdraw(taskItems[taskID][step][i].getName(), taskItems[taskID][step][i].getAmount());
            else if (splittedItems.length > 1) {
                for (String item : splittedItems) {
                    if (!getInventory().contains(item) && getBank().contains(item) && taskItems[taskID][step][i].isNeededToBeTaken()) {
                        getBank().withdraw(item, taskItems[taskID][step][i].getAmount());
                        break;
                    }
                }
            }

            sleep(random(300, 500));
        }

        log("Closing bank in GetItemsFromBank");
        getBank().close();

        return 0;
    }
}
