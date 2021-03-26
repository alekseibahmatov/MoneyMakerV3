package bank.utils;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.ArrayList;
import java.util.Random;

public class BankManager extends MethodProvider {

    public void openBank() {

        Area[] banks = {
                Banks.GRAND_EXCHANGE,
                Banks.LUMBRIDGE_UPPER,
                Banks.DRAYNOR,
                Banks.AL_KHARID,
                Banks.FALADOR_EAST,
                Banks.FALADOR_WEST,
                Banks.VARROCK_WEST,
                Banks.VARROCK_EAST,
                Banks.CAMELOT,
                Banks.CATHERBY,
                Banks.EDGEVILLE,
                Banks.YANILLE,
                Banks.GNOME_STRONGHOLD,
                Banks.CASTLE_WARS,
                Banks.ARDOUGNE_NORTH,
                Banks.ARDOUGNE_SOUTH,
                Banks.DUEL_ARENA,
                Banks.PEST_CONTROL,
                Banks.CANIFIS,
                Banks.TZHAAR,
                new Area(2536, 3573, 2536, 3573),
        };


        if (getWalking().webWalk(banks)) {

            log("FUCK123");

            if (getBank().isOpen()) return;

            if (getInventory().isItemSelected()) getInventory().deselectItem();

            Filter<RS2Object> geBoothFilter = rs2Object -> rs2Object.hasAction("Bank");

            RS2Object geBooth = getObjects().closest(geBoothFilter);
            NPC banker = getNpcs().closest("Banker");

            int random = new Random().nextInt(10);
            if (geBooth != null && random < 5) {
                if (!geBooth.isVisible()) getCamera().toEntity(geBooth);
                log("Opening bank through geBooth");
                geBooth.interact("Bank");
                new ConditionalSleep(2500, 3000) {
                    @Override
                    public boolean condition() {
                        return getBank().isOpen();
                    }
                }.sleep();
            }

            if (banker != null && random >= 5) {
                if (!banker.isVisible()) getCamera().toEntity(banker);
                log("Opening bank through banker");
                banker.interact("Bank");
                new ConditionalSleep(2500, 3000) {
                    @Override
                    public boolean condition() {
                        return getBank().isOpen();
                    }
                }.sleep();
            }
            try {
                if (!getBank().open()) openBank();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            openBank();
        }
    }
}
