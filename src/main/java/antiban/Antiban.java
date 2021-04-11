package antiban;

import org.osbot.rs07.api.NPCS;
import org.osbot.rs07.api.Players;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;

import java.util.Random;

public class Antiban extends MethodProvider {
    private Area areas[] = {
            new Area(3207, 3434, 3220, 3423),
            new Area(3254, 3454, 3266, 3448),
            new Area(3237,3223, 3231, 3212)
    };

    private Area banks[] = {
            Banks.VARROCK_EAST,
            Banks.VARROCK_WEST
    };

    public void randomMove() throws InterruptedException {

        Random rand = new Random();

        int random = rand.nextInt(10000);

        if(random >= 0 && random <= 100) {

            log("Making outstanding move");
            log("Moving mouse to some points");

            int moves = random(1, 4);

            for (int i = 0; i < moves; i++) {
                getMouse().move(random(26, 745), random(7, 492));

                sleep(random(500, 700));
            }
        }

        else if(random > 100 && random <= 110) {
            log("Making outstanding move");
            log("Moving mouse outside screen");

            getMouse().moveOutsideScreen();
            sleep(random(20000, 180000));
        }

        else if(random > 200 && random <= 210) {
            log("Making outstanding move");
            log("Sleeping");

            sleep(random(10000, 120000));
        }

        else if(random > 300 && random <= 302) {
            log("Making outstanding move");
            log("Running to some place");

            int move = random( 1, 2);
            if(move == 1) {
                int area = random(0, 2);
                while(!areas[area].contains(myPosition())) {
                    getWalking().webWalk(areas[area]);
                    sleep(random(500, 700));
                }
            }

            else if(move == 2) {
                int area = random(0, 1);
                while(!banks[area].contains(myPosition())) {
                    getWalking().webWalk(banks[area]);
                    sleep(random(500, 700));
                }
            }
        }

        else if(random > 400 && random <= 450) {
            log("Making outstanding move");
            log("Rotating camera by pitch");


            int move = random(1, 2);

            if(move == 1) getCamera().movePitch(getCamera().getPitchAngle() + random(10, 40));
            else getCamera().movePitch(getCamera().getPitchAngle() - random(10, 40));

            sleep(random(300, 500));
        }

        else if(random > 500 && random <= 550) {
            log("Making outstanding move");
            log("Rotating camera by yaw");

            int move = random(1, 2);

            if(move == 1) getCamera().moveYaw(getCamera().getYawAngle() + random(10, 40));
            else getCamera().moveYaw(getCamera().getYawAngle() - random(10, 40));

            sleep(random(300, 500));
        }

        else if(random > 600 && random <= 630) {
            log("Making outstanding move");
            log("Rotating camera by pitch and yaw");

            int move = random(1, 4);

            if(move == 1) {
                getCamera().moveYaw(getCamera().getYawAngle() + random(10, 20));
                getCamera().movePitch(getCamera().getPitchAngle() + random(10, 20));
            }

            else if(move == 2) {
                getCamera().moveYaw(getCamera().getYawAngle() + random(10, 20));
                getCamera().movePitch(getCamera().getPitchAngle() - random(10, 20));
            }

            else if(move == 3) {
                getCamera().moveYaw(getCamera().getYawAngle() - random(10, 20));
                getCamera().movePitch(getCamera().getPitchAngle() + random(10, 20));
            }

            else if(move == 4) {
                getCamera().moveYaw(getCamera().getYawAngle() - random(10, 20));
                getCamera().movePitch(getCamera().getPitchAngle() - random(10, 20));
            }
        }

        else if(random > 700 && random <= 708) {
            log("Making outstanding move");
            log("Interacting with random npc");

            NPCS npcs = getNpcs();

            int randomNPC = random(0, npcs.getAll().size());

            getCamera().toEntity(npcs.getAll().get(randomNPC));

            sleep(random(300, 500));

            npcs.getAll().get(randomNPC).interact();

            sleep(random(300, 500));
        }

        else if(random > 800 && random <= 815) {
            log("Making outstanding move");
            log("Opening skills tab");

            if(!getTabs().getOpen().equals(Tab.SKILLS)) getTabs().open(Tab.SKILLS);
            sleep(random(1500, 3000));
        }

        else if(random > 900 && random <= 915) {
            log("Making outstanding move");
            log("Opening attack tab");

            if(!getTabs().getOpen().equals(Tab.ATTACK)) getTabs().open(Tab.ATTACK);
            sleep(random(1500, 3000));
        }

        else if (random > 1000 && random <= 1005) {
            log("Making outstanding move");
            log("Adding someone to friends");

            Players players = getPlayers();

            int player = random(0, players.getAll().size());

            getCamera().toEntity(players.getAll().get(player));

            AddFriend(players.getAll().get(player).getName());
            sleep(random(300, 500));
        }
    }

    private void AddFriend(String name) throws InterruptedException
    {
        if(tabs.getOpen() != Tab.FRIENDS)
        {
            tabs.open(Tab.FRIENDS);
            sleep(random(500, 700));
        }

        if(IsFriend(name)) return;

        RS2Widget widget = widgets.get(429, 14);
        if(widget == null) return;

        if(widget.isVisible()) widget.interact();
        sleep(random(500, 700));

        widget = widgets.get(162, 45);
        if(widget != null && widget.isVisible())
        {
            keyboard.typeString(name);
            keyboard.pressKey(13);
        }
    }

    private boolean IsFriend(String name)
    {
        RS2Widget widget = widgets.get(429, 3);
        if(widget == null) return false;

        RS2Widget[] friends;
        if((friends =  widget.getChildWidgets()) != null)
        {
            for(RS2Widget w : friends)
            {
                if(w.getMessage().equals(name))
                    return true;
            }
        }

        return false;
    }
}
