import activities.CollectionPlanks.CollectingPlanks;
import activities.CraftingLimestoneBricks.CuttingOpals;
import activities.CraftingLimestoneBricks.GrindingLimestoneBricks;
import activities.FillingBullseyeLantern.PlayerHasHouse;
import activities.FillingBullseyeLantern.PlayerNoHouse;
import activities.GrindingDesertGoatHorns.GrindingDesertGoatHorns;
import activities.MakingGuamPotions.MakingGuamPotions;
import activities.MakingTarrominPotions.HerbloreBelow12Lvl;
import activities.MakingTarrominPotions.MakingTarrominPotions;
import activities.MakingUltracompost.MakingUltracompost;
import activities.SpinningFlax.SpinningFlax;
import activities.UncornHornGrinder.UnicornHornGrinder;
import activities.setup.Task;
import bank.GetItemsFromBank;
import bank.utils.BankManager;
import grandExchange.GrandExchangeBuy;
import grandExchange.GrandExchangeSell;
import grandExchange.buy.GetBuyItems;
import grandExchange.sell.GetSellItems;
import grandExchange.utils.GrandExchangeManager;
import org.osbot.Constants;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Config;
import utils.CustomBreakManager;
import utils.HouseValidation;
import utils.Sleep;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;

@ScriptManifest(version = 0.1, author = "Z3Die", name = "Ultimate money maker V3", info = "Does a lot of activities to earn gold", logo = "")
public class main extends Script {

    // Activities initialization

    private UnicornHornGrinder unicornHornGrinder = new UnicornHornGrinder();
    private GrindingDesertGoatHorns grindingDesertGoatHorns = new GrindingDesertGoatHorns();

    private CuttingOpals cuttingOpals = new CuttingOpals();
    private GrindingLimestoneBricks limestoneBricks = new GrindingLimestoneBricks();

    private MakingUltracompost ultracompost = new MakingUltracompost();

    private PlayerHasHouse hasHouse = new PlayerHasHouse();
    private PlayerNoHouse noHouse = new PlayerNoHouse();

    private CollectingPlanks collectingPlanks = new CollectingPlanks();

    private MakingGuamPotions guamPotions = new MakingGuamPotions();

    private HerbloreBelow12Lvl herbloreBelow12Lvl = new HerbloreBelow12Lvl();
    private MakingTarrominPotions tarrominPotions = new MakingTarrominPotions();

    private SpinningFlax spinningFlax = new SpinningFlax();

    // Utilities initialization

    private CustomBreakManager customBreakManager = new CustomBreakManager();
    private Config config = new Config();
    private HouseValidation houseValidation = new HouseValidation();

    // Banking initialization

    private GetItemsFromBank getItemsFromBank = new GetItemsFromBank();
    private BankManager bankManager = new BankManager();

    // GrandExchange initialization

    private GrandExchangeManager grandExchangeManager = new GrandExchangeManager();
    private GrandExchangeBuy grandExchangeBuy = new GrandExchangeBuy();
    private GrandExchangeSell grandExchangeSell = new GrandExchangeSell();
    private GetBuyItems getBuyItems = new GetBuyItems();
    private GetSellItems getSellItems = new GetSellItems();


    private String timeOffset;


    private int taskID = -1, taskParam, prevTaskID;
    private boolean firstLoop;

    private boolean isBreaking;
    private long breakingUntil, untilBreak;

    private long startTime;

    private long goldAmount;

    private boolean isHouse;

    private List<Task> tasks = new ArrayList<>();

    @Override
    public void onStart() {

        // Activities initialization

        unicornHornGrinder.exchangeContext(getBot());
        grindingDesertGoatHorns.exchangeContext(getBot());
        cuttingOpals.exchangeContext(getBot());
        limestoneBricks.exchangeContext(getBot());
        ultracompost.exchangeContext(getBot());
        hasHouse.exchangeContext(getBot());
        noHouse.exchangeContext(getBot());
        collectingPlanks.exchangeContext(getBot());
        guamPotions.exchangeContext(getBot());
        herbloreBelow12Lvl.exchangeContext(getBot());
        tarrominPotions.exchangeContext(getBot());
        spinningFlax.exchangeContext(getBot());

        tasks.add(unicornHornGrinder);
        tasks.add(grindingDesertGoatHorns);
        tasks.add(cuttingOpals);
        tasks.add(ultracompost);
        tasks.add(hasHouse);
        tasks.add(collectingPlanks);
        tasks.add(guamPotions);
        tasks.add(herbloreBelow12Lvl);
        tasks.add(spinningFlax);

        // Utilities initialization

        customBreakManager.exchangeContext(getBot());
        getBot().getRandomExecutor().overrideOSBotRandom(customBreakManager);
        config.exchangeContext(getBot());
        houseValidation.exchangeContext(getBot());

        // Banking initialization

        getItemsFromBank.exchangeContext(getBot());
        bankManager.exchangeContext(getBot());

        // GrandExchange initialization

        grandExchangeManager.exchangeContext(getBot());
        grandExchangeBuy.exchangeContext(getBot());
        grandExchangeSell.exchangeContext(getBot());
        getBuyItems.exchangeContext(getBot());
        getSellItems.exchangeContext(getBot());

        startTime = System.currentTimeMillis();

        // Startup setups

        List<String> configData = config.getConfig();

        timeOffset = configData.get(0);

        isHouse = houseValidation.validate();

    }

    @Override
    public int onLoop() throws InterruptedException {

        if(!isBreaking) {
            if (taskID == -1) selectTask();

            if(firstLoop) {
                if(tasks.get(taskID).isNeededToStartAtGE()) goToGrandExchange();
                firstLoop = false;
            } else {
                switch (taskID) {
                    case 0:
                        if (unicornHornGrinder.validate()) unicornHornGrinder.execute(taskParam);
                        else {
                            taskID = -2;
                            prevTaskID = 0;
                            taskParam = 0;
                        }
                        break;
                    case 1:
                        if (grindingDesertGoatHorns.validate()) grindingDesertGoatHorns.execute(taskParam);
                        else {
                            taskID = -2;
                            prevTaskID = 1;
                            taskParam = 0;
                        }
                        break;
                    case 2:
                        if (getSkills().getDynamic(Skill.CRAFTING) >= 40) {
                            if (limestoneBricks.validate()) limestoneBricks.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 2;
                                taskParam = 1;
                            }
                        } else {
                            if (cuttingOpals.validate()) cuttingOpals.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 2;
                                taskParam = 0;
                            }
                        }
                        break;
                    case 3:
                        if (ultracompost.validate()) ultracompost.execute(taskParam);
                        else {
                            taskID = -2;
                            prevTaskID = 3;
                            taskParam = 0;
                        }
                        break;
                    case 4:
                        if (isHouse && getSkills().getDynamic(Skill.CRAFTING) >= 49) {
                            if (hasHouse.validate()) hasHouse.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 4;
                                taskParam = 0;
                            }
                        } else {
                            if(noHouse.validate()) noHouse.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 4;
                                taskParam = 0;
                            }
                        }
                        break;
                    case 5:
                        if (collectingPlanks.validate()) collectingPlanks.execute(taskParam);
                        else {
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

                                String[] gamesNecklaces = {"Games necklace(1)", "Games necklace(2)", "Games necklace(3)", "Games necklace(4)", "Games necklace(5)", "Games necklace(6)", "Games necklace(7)", "Games necklace(8)"};

                                // perebiraem i uzaem necklace
                                for (int i = 0; i < gamesNecklaces.length; i++) {
                                    if (getInventory().contains(gamesNecklaces[i]))
                                        getInventory().getItem(gamesNecklaces[i]).interact("Rub");
                                }

                                // son do pojavlenija widgeta teleporta na island
                                new ConditionalSleep(60000, 3000) {
                                    @Override
                                    public boolean condition() {
                                        RS2Widget teleportToBarbarianOutpost = getWidgets().get(219, 1, 2);
                                        return (teleportToBarbarianOutpost != null && teleportToBarbarianOutpost.isVisible());
                                    }
                                }.sleep();

                                // vzaimodeistvie s widgetom
                                RS2Widget teleportToBarbarianOutpost = getWidgets().get(219, 1, 2);
                                teleportToBarbarianOutpost.interact();
                                log("Teleporting to Barbarian Outpost");

                                // sleep until my position contains in island area
                                new ConditionalSleep(60000, 2000) {
                                    @Override
                                    public boolean condition() throws InterruptedException {
                                        log("sleep until teleport finish");
                                        Area islandArea = new Area(2515, 3590, 2557, 3534);
                                        return (islandArea.contains(myPosition()));
                                    }
                                }.sleep();
                            } else {
                                taskID = -2;
                                prevTaskID = 5;
                                taskParam = 0;
                            }
                        }
                        break;
                    case 6:
                        if(guamPotions.validate()) guamPotions.execute(taskParam);
                        else {
                            taskID = -2;
                            prevTaskID = 6;
                            taskParam = 0;
                        }
                        break;
                    case 7:
                        if (getSkills().getDynamic(Skill.HERBLORE) >= 12) {
                            if(tarrominPotions.validate()) tarrominPotions.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 7;
                                taskParam = 0;
                            }
                        } else {
                            if(herbloreBelow12Lvl.validate()) herbloreBelow12Lvl.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 7;
                                taskParam = 1;
                            }
                        }
                        break;
                    case 8:
                        if (getSkills().getDynamic(Skill.CRAFTING) >= 10) {
                            if (spinningFlax.validate()) spinningFlax.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 8;
                                taskParam = 0;
                            }
                        } else {
                            if (cuttingOpals.validate()) cuttingOpals.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 8;
                                taskParam = 0;
                            }
                        }
                        break;
                    case -3:
                        try {
                            goldAmount = grandExchangeSell.sellItems(2, bankManager, grandExchangeManager);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (goldAmount == -999) {
                            customBreakManager.startBreaking(breakingUntil, true);

                            isBreaking = true;
                        }
                        break;
                    case -2:
                        log("Case -2 started");
                        log("Getting items");

                        int state = getItemsFromBank.getItems(prevTaskID, taskParam, bankManager);

                        if (state == -1) {
                            try {
                                goldAmount = grandExchangeBuy.buyItems(prevTaskID, taskParam, bankManager, grandExchangeManager);

                                if (goldAmount == -1) {
                                    goldAmount = grandExchangeSell.sellItems(1, bankManager, grandExchangeManager);
                                } else if (goldAmount == -2) log("Спасити");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        selectTask();
                        break;
                }
            }
        } else {
            selectTask();
        }

        return 0;
    }

    @Override
    public final void onExit() {

    }

    @Override
    public void onPaint(final Graphics2D g) {
        final long runTime = System.currentTimeMillis() - startTime;
        final long breakTime = breakingUntil - System.currentTimeMillis();
        final long timeUntilBreak = untilBreak - System.currentTimeMillis() / 1000;

        if (!isBreaking) {
            g.drawString(String.format("Runtime: %s", formatTime(runTime)), 35, 340);
            g.drawString(String.format("Time until next break: %s", formatTime(timeUntilBreak)), 35, 300);
        }
        else g.drawString(String.format("Breaking: %s", formatTime(breakTime)), 35, 340);
    }

    public void solver() {
        RS2Widget decline = getWidgets().get(326, 96);

        if (decline != null && decline.isVisible()) decline.interact();
    }

    private void goToGrandExchange() {
        if(!Banks.GRAND_EXCHANGE.contains(myPosition())) {
            bankManager.openBank();

            String ringOfWealth[] = "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)".split(",");

            boolean isTeleportExists = false;

            for (String ring : ringOfWealth) {
                if(getInventory().contains(ring)) {
                    getBank().close();

                    getInventory().getItem(ring).interact("Rub");

                    Sleep.sleepUntil(() -> getWidgets().get(219, 1, 2) != null && getWidgets().get(219, 1, 2).isVisible(), 5000, 500);

                    RS2Widget RoW = getWidgets().get(219, 1, 2);

                    RoW.interact();

                    isTeleportExists = true;

                    break;
                }
                else if(getBank().contains(ring)) {
                    getBank().withdraw(ring, 1);

                    getBank().close();

                    getInventory().getItem(ring).interact("Rub");

                    Sleep.sleepUntil(() -> getWidgets().get(219, 1, 2) != null && getWidgets().get(219, 1, 2).isVisible(), 5000, 500);

                    RS2Widget RoW = getWidgets().get(219, 1, 2);

                    RoW.interact();

                    isTeleportExists = true;

                    break;
                }
            }

            if (!isTeleportExists) {
                Area GE = new Area(3169, 3488, 3161, 3486);

                while(GE.contains(myPosition())) getWalking().webWalk(GE);
            }
        }
    }

    public void createTaskList(int offset) throws IOException {
        File file = new File(Constants.DATA_DIR + "\\Ultimate Money Maker V3\\tasks.txt");
        System.gc();

        if (file.exists()) {

            log("Creating task list");

            String devidedTime[] = timeOffset.split(":");

            GregorianCalendar now = new GregorianCalendar();
            GregorianCalendar cal = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), Integer.parseInt(devidedTime[0]), Integer.parseInt(devidedTime[1]));

            cal.add(Calendar.DAY_OF_MONTH, offset);

            int random = random(-60, 60);
            cal.add(Calendar.MINUTE, random);

            int rand = random(4, 6);

            try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < rand; i++) {
                    int randTask = random(5, 8); // Task cases

                    int param = 0;

                    if (randTask == 0 || randTask == 1 || randTask == 2) param = random(1, 2);

                    int randMinutes = random(45, 120);

                    long startSecs = cal.getTimeInMillis() / 1000;

                    cal.add(Calendar.MINUTE, randMinutes);

                    long endSecs = cal.getTimeInMillis() / 1000;

                    br.write(String.format("%d;%d;%d;%d", randTask, param, startSecs, endSecs) + System.lineSeparator());

                    int randSleep = random(30, 90);

                    cal.add(Calendar.MINUTE, randSleep);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            if (now.after(cal)) {
                createTaskList(1);
            }
        } else {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    public void selectTask() {
        File file = new File(Constants.DATA_DIR + "\\Ultimate Money Maker V3\\tasks.txt");

        if (file.exists() && file.length() != 0) {
            GregorianCalendar now = new GregorianCalendar();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                long lineCount;
                try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
                    lineCount = stream.count();
                }

                boolean isNeededToRecreateFile = false;

                for (int i = 0; i < lineCount; i++) {
                    String line = br.readLine();

                    if (i + 1 == lineCount && line != null && Long.parseLong(line.split(";")[3]) < now.getTimeInMillis() / 1000)
                        isNeededToRecreateFile = true;
                    else continue;
                }

                if (isNeededToRecreateFile) createTaskList(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean deleteLine = false;
            String lastLine = "";

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line = br.readLine();

                String data[] = line.split(";");

                long nowSecs = now.getTimeInMillis() / 1000;

                log("Changing task");

                if (nowSecs > Long.parseLong(data[2]) && nowSecs < Long.parseLong(data[3])) {

                    taskID = Integer.parseInt(data[0]);
                    taskParam = Integer.parseInt(data[1]);

                    untilBreak = Long.parseLong(data[3]);

                    log(untilBreak);
                    log(System.currentTimeMillis());

                    firstLoop = true;

                    isBreaking = false;
                } else {
                    if (now.getTimeInMillis() / 1000 > Long.parseLong(data[3])) {
                        if ((lastLine = br.readLine()) == null) {
                            createTaskList(0);
                        } else deleteLine = true;
                    } else if (now.getTimeInMillis() / 1000 < Long.parseLong(data[2])) {

                        if (Long.parseLong(data[2]) - now.getTimeInMillis() / 1000 < 6000) {
                            long sleepTimeSecs = (Long.parseLong(line.split(";")[2]) - (now.getTimeInMillis() / 1000)) * 1000;

                            breakingUntil = nowSecs * 1000 + sleepTimeSecs;

                            customBreakManager.startBreaking(sleepTimeSecs, true);

                            isBreaking = true;
                        } else {
                            taskID = -3;

                            long sleepTimeSecs = (Long.parseLong(line.split(";")[2]) - (now.getTimeInMillis() / 1000)) * 1000;

                            breakingUntil = nowSecs * 1000 + sleepTimeSecs;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (deleteLine) {
                deleteLine(Constants.DATA_DIR + "\\Ultimate Money Maker V3\\tasks.txt", 1, 1);

                long sleepTimeSecs = ((now.getTimeInMillis() / 1000) - Long.parseLong(lastLine.split(";")[2])) * 1000;

                long nowSecs = now.getTimeInMillis() / 1000;

                breakingUntil = nowSecs * 1000 + sleepTimeSecs;

                customBreakManager.startBreaking(sleepTimeSecs, true);

                isBreaking = true;
            }
        } else {
            try {
                createTaskList(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void deleteLine(String filename, int startline, int numlines) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            //String buffer to store contents of the file
            StringBuffer sb = new StringBuffer("");

            //Keep track of the line number
            int linenumber = 1;
            String line;

            while ((line = br.readLine()) != null) {
                //Store each valid line in the string buffer
                if (linenumber < startline || linenumber >= startline + numlines)
                    sb.append(line + "\n");
                linenumber++;
            }
            if (startline + numlines > linenumber)
                System.out.println("End of file reached.");
            br.close();

            FileWriter fw = new FileWriter(new File(filename));
            //Write entire string buffer into the file
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            System.out.println("Something went horribly wrong: " + e.getMessage());
        }
    }

    public final String formatTime(final long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60;
        m %= 60;
        h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }
}
