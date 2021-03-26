package utils;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

public class HouseValidation extends MethodProvider {
    public boolean validate() {

        log("house validation");

        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        new ConditionalSleep(60000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                return !getBank().isOpen();
            }
        }.sleep();

        if (!getTabs().getOpen().equals(Tab.SETTINGS)) getTabs().open(Tab.SETTINGS);

        new ConditionalSleep(60000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                return getTabs().getOpen().equals(Tab.SETTINGS);
            }
        }.sleep();

        RS2Widget houseWidget = getWidgets().get(116, 28, 1);
        houseWidget.interact();

        new ConditionalSleep(10000, 1000) {
            @Override
            public boolean condition() throws InterruptedException {
                log("sleep until see house rooms widget");
                RS2Widget houseRoomsAmount = getWidgets().get(370, 20);
                return (houseRoomsAmount != null && houseRoomsAmount.isVisible());
            }
        }.sleep();

        if (Integer.parseInt(getWidgets().get(370, 20).getMessage().split(" ")[3]) > 0) {
            RS2Widget leaveHouseWidget = getWidgets().get(370, 21);
            leaveHouseWidget.interact();
//            new ConditionalSleep(5000, 1000) {
//                @Override
//                public boolean condition() throws InterruptedException {
//                    log("sleep until house widget disappear");
//                    RS2Widget leaveHouseWidget = getWidgets().get(370, 21);
//                    return (leaveHouseWidget == null && !leaveHouseWidget.isVisible());
//                }
//            }.sleep();
            if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
            return true;
        } else {
            RS2Widget leaveHouseWidget = getWidgets().get(370, 21);
            leaveHouseWidget.interact();
//            new ConditionalSleep(5000, 1000) {
//                @Override
//                public boolean condition() throws InterruptedException {
//                    log("sleep until house widget disappear");
//                    RS2Widget leaveHouseWidget = getWidgets().get(370, 21);
//                    return (leaveHouseWidget == null && !leaveHouseWidget.isVisible());
//                }
//            }.sleep();
            if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
            return false;
        }
    }
}
