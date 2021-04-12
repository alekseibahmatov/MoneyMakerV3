import activities.CollectionPlanks.CollectingPlanks;
import activities.CraftingLimestoneBricks.CuttingOpals;
import activities.CraftingLimestoneBricks.GrindingLimestoneBricks;
import activities.FillingBullseyeLantern.PlayerHasHouse;
import activities.FillingBullseyeLantern.PlayerNoHouse;
import activities.GrindingDesertGoatHorns.GrindingDesertGoatHorns;
import activities.MakingGuamPotions.DruidicRitual;
import activities.MakingGuamPotions.MakingGuamPotions;
import activities.MakingTarrominPotions.HerbloreBelow12Lvl;
import activities.MakingTarrominPotions.MakingTarrominPotions;
import activities.MakingUltracompost.MakingUltracompost;
import activities.SpinningFlax.SpinningFlax;
import activities.UncornHornGrinder.UnicornHornGrinder;
import activities.WineOfZamorak.TrainingMagic;
import activities.WineOfZamorak.WineOfZamorak;
import activities.setup.Task;
import antiban.Antiban;
import bank.GetItemsFromBank;
import bank.utils.BankManager;
import grandExchange.GrandExchangeBuy;
import grandExchange.GrandExchangeSell;
import grandExchange.buy.GetBuyItems;
import grandExchange.sell.GetSellItems;
import grandExchange.utils.GrandExchangeManager;
import models.TaskData;
import models.Timetable;
import org.osbot.rs07.api.Chatbox;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    private DruidicRitual druidicRitual = new DruidicRitual();
    private MakingGuamPotions guamPotions = new MakingGuamPotions();

    private HerbloreBelow12Lvl herbloreBelow12Lvl = new HerbloreBelow12Lvl();
    private MakingTarrominPotions tarrominPotions = new MakingTarrominPotions();

    private SpinningFlax spinningFlax = new SpinningFlax();

    private TrainingMagic trainingMagic = new TrainingMagic();
    private WineOfZamorak wineOfZamorak = new WineOfZamorak();


    // Utilities initialization

    private CustomBreakManager customBreakManager = new CustomBreakManager();
    private Config config = new Config();
    private HouseValidation houseValidation = new HouseValidation();
    private Hopper hopper = new Hopper();
    private Antiban antiban = new Antiban();

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
    private boolean firstLoop = true;

    private boolean isBreaking;
    private long breakingUntil, untilBreak;

    private long goldAmount;

    private boolean isHouse;

    private List<Task> tasks = new ArrayList<>();
    private List<TaskData> taskHistory = new ArrayList<>();

    private int tasksCountWithoutSleep = 0, taskTotalCount = 0;
    private long taskUntil, sleepUntil;
    private TaskData task;

    private boolean changingTask;

    //------------Essentials---------------
    //Font size & Text Padding will determine the size of paint
    //Look at forum for examples
    final int baseFontSize = 14; //Recommended 12-20
    final int textPadding = 5; //space between text,columns, & rows
    final String title = "MoneyMaker V3"; //add script name here
    final boolean warningEnabled = true; //Warning window pops up on paint if a player says one of the keywords, Make sure public chat is visible
    final String[] warningChatKeywords = {"bot", "botting", "report", "reported", "Botting", "Bot", "Report", "Reported"};

    //--Custom Counters - Read Forum for details
    //Set customInfo=null if you don't want to add anything
    String[] customInfo = null;

    //------------Cosmetics---------------
    final float paintTransparency = 0.60F; // 0.00F is transparent, 1.00F is opaque
    final Color textColor = new Color(255, 255, 255);
    final Color paintOutlineColor = new Color(86, 0, 147);
    final Color paintFillColor = new Color(0, 0, 0);
    final Color captionColor = new Color(255, 255, 255);
    final Color dividerColor = new Color(169, 169, 169);
    final Color progressBarFillColor = new Color(139, 0, 0);
    final Color progressBarOutlineColor = new Color(139, 0, 0);
    final Color warningColor = new Color(255, 2, 2);
    final String fontStyle = "SansSerif";
    private final Font baseFont = new Font(fontStyle, Font.PLAIN, baseFontSize);
    private final Font boldFont = new Font(fontStyle, Font.BOLD, baseFontSize);
    private final Font warningFont = new Font(fontStyle, Font.BOLD, 40);

    int skillsRowLength, titlePaintLength, rowHeight, skillsPaintHeight, titlePaintHeight, skillsPaintX, titlePaintX, skillsPaintY,
            titlePaintY, lengthC1, lengthC2, lengthC3, lengthC4, lengthC5, lengthC6, lengthDivide, lengthHide, lengthReset,
            titleRowHeight, l;
    int[] rowY;
    int[] xpForLevels = {0, 83, 174, 276, 388, 512, 650, 801, 969, 1154,
            1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470,
            5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363,
            14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648,
            37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014,
            91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040,
            203254, 224466, 247886, 273742, 302288, 333804, 368599, 407015,
            449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257,
            992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808,
            1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792,
            3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629,
            7944614, 8771558, 9684577, 10692629, 11805606, 13034431};

    String state, sd;
    String shide = "Hide";
    String sshow = "Show";
    String reset = "Reset";
    String[][] skillsPaintData;

    double[] startingLevelsAll = new double[23];
    double[] startingExpAll = new double[23];

    public long startTime = 0L, millis = 0L, hours = 0L;
    public long minutes = 0L, seconds = 0L, last = 0L;

    Rectangle hideBtn, resetBtn;
    Rectangle[] skillRectangles, progressRectangles;

    boolean hidePaint = false;
    boolean showWarning = false;

    final Skill[] allSkills = {Skill.ATTACK, Skill.MINING, Skill.SLAYER, Skill.HITPOINTS, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED,
            Skill.STRENGTH, Skill.AGILITY, Skill.CONSTRUCTION, Skill.COOKING, Skill.CRAFTING, Skill.FARMING, Skill.FIREMAKING, Skill.FISHING,
            Skill.FLETCHING, Skill.HERBLORE, Skill.HUNTER, Skill.PRAYER, Skill.RUNECRAFTING, Skill.SMITHING, Skill.THIEVING, Skill.WOODCUTTING};

    Point p;


    @Override
    public void onStart() {
        log("/");

        // Activities initialization

        unicornHornGrinder.exchangeContext(getBot());
        grindingDesertGoatHorns.exchangeContext(getBot());
        cuttingOpals.exchangeContext(getBot());
        limestoneBricks.exchangeContext(getBot());
        ultracompost.exchangeContext(getBot());
        hasHouse.exchangeContext(getBot());
        noHouse.exchangeContext(getBot());
        collectingPlanks.exchangeContext(getBot());
        druidicRitual.exchangeContext(getBot());
        guamPotions.exchangeContext(getBot());
        herbloreBelow12Lvl.exchangeContext(getBot());
        tarrominPotions.exchangeContext(getBot());
        spinningFlax.exchangeContext(getBot());
        trainingMagic.exchangeContext(getBot());
        wineOfZamorak.exchangeContext(getBot());

        tasks.add(unicornHornGrinder);
        tasks.add(grindingDesertGoatHorns);
        tasks.add(cuttingOpals);
        tasks.add(ultracompost);
        tasks.add(collectingPlanks);
        tasks.add(guamPotions);
        tasks.add(herbloreBelow12Lvl);
        tasks.add(spinningFlax);
        tasks.add(trainingMagic);


        // Utilities initialization

        customBreakManager.exchangeContext(getBot());
        hopper.exchangeContext(getBot());
        getBot().getRandomExecutor().overrideOSBotRandom(customBreakManager);
        config.exchangeContext(getBot());
        houseValidation.exchangeContext(getBot());
        antiban.exchangeContext(getBot());

        // Banking initialization

        getItemsFromBank.exchangeContext(getBot());
        bankManager.exchangeContext(getBot());

        // GrandExchange initialization

        grandExchangeManager.exchangeContext(getBot());
        grandExchangeBuy.exchangeContext(getBot());
        grandExchangeSell.exchangeContext(getBot());
        getBuyItems.exchangeContext(getBot());
        getSellItems.exchangeContext(getBot());

        for (int i = 0; i < allSkills.length; i++) {
            startingLevelsAll[i] = getSkills().getStatic(allSkills[i]);
            startingExpAll[i] = getSkills().getExperience(allSkills[i]);
        }

        startTime = System.currentTimeMillis();

        // Startup setups

        List<String> configData = config.getConfig();

        timeOffset = configData.get(0);

        isHouse = houseValidation.validate();

        if (!getTabs().isOpen(Tab.SETTINGS)) {
            getTabs().open(Tab.SETTINGS);
            RS2Widget zoomScroll = getWidgets().get(116, 60);
            RS2Widget mouseSettingTab = getWidgets().get(116, 69);
            if (zoomScroll != null) {
                getMouse().move(random(611, 620), 280);
                Sleep.sleepUntil(random(300, 500));
                getMouse().click(false);
                log("zoomed mouse");
            } else if (mouseSettingTab != null) mouseSettingTab.interact();
        }

    }

    @Override
    public int onLoop() throws InterruptedException {
        log("/loop");


        getBot().getCanvas().addMouseListener(listener);

        log(isBreaking);

        if (!isBreaking) {
            if (taskID == -1) taskManager();


            new ConditionalSleep(60000, 1000) {
                @Override
                public boolean condition() {
                    RS2Widget pizda = getWidgets().get(192, 1, 11);
                    if (pizda != null && pizda.isVisible()) {
                        pizda.interact();
                    }
                    return pizda == null;
                }
            }.sleep();

            log("/1");

            if (firstLoop) {
                if (tasks.get(taskID).isNeededToStartAtGE()) goToGrandExchange();
                firstLoop = false;
                log("/2");
            } else {
                log("/3");
                switch (taskID) {
                    case 0:
                        if (unicornHornGrinder.validate()) {
                            antiban.randomMove();
                            unicornHornGrinder.execute(taskParam);
                        }
                        else {
                            taskID = -2;
                            prevTaskID = 0;
                            taskParam = 0;
                        }
                        break;
                    case 1:
                        if (grindingDesertGoatHorns.validate()) {
                            antiban.randomMove();
                            grindingDesertGoatHorns.execute(taskParam);
                        }
                        else {
                            taskID = -2;
                            prevTaskID = 1;
                            taskParam = 0;
                        }
                        break;
                    case 2:
                        if (getSkills().getDynamic(Skill.CRAFTING) >= 40) {
                            if (limestoneBricks.validate()) {
                                antiban.randomMove();
                                limestoneBricks.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 2;
                                taskParam = 1;
                            }
                        } else {
                            if (cuttingOpals.validate()) {
                                antiban.randomMove();
                                cuttingOpals.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 2;
                                taskParam = 0;
                            }
                        }
                        break;
                    case 3:
                        if (ultracompost.validate()) {
                            antiban.randomMove();
                            ultracompost.execute(taskParam);
                        }
                        else {
                            taskID = -2;
                            prevTaskID = 3;
                            taskParam = 0;
                        }
                        break;
//                    case 4:
//                        if (isHouse && getSkills().getDynamic(Skill.CRAFTING) >= 49) {
//                            if (hasHouse.validate()) hasHouse.execute(taskParam);
//                            else {
//                                taskID = -2;
//                                prevTaskID = 4;
//                                taskParam = 0;
//                            }
//                        } else {
//                            if (noHouse.validate()) noHouse.execute(taskParam);
//                            else {
//                                taskID = -2;
//                                prevTaskID = 4;
//                                taskParam = 0;
//                            }
//                        }
//                        break;
                    case 4:
                        if (collectingPlanks.validate()) {
                            antiban.randomMove();
                            collectingPlanks.execute(taskParam);
                        }
                        else {
                            antiban.randomMove();
                            if ((getInventory().contains("Games necklace(8)") ||
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
                                prevTaskID = 4;
                                taskParam = 0;
                            }
                        }
                        break;
                    case 5:
                        if (getSkills().getDynamic(Skill.HERBLORE) >= 3) {
                            if (guamPotions.validate()) {
                                antiban.randomMove();
                                guamPotions.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 5;
                                taskParam = 0;
                            }
                        } else {
                            if (druidicRitual.validate()) {
                                antiban.randomMove();
                                druidicRitual.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 5;
                                taskParam = 1;
                            }
                        }
                        break;
                    case 6:
                        if (getSkills().getDynamic(Skill.HERBLORE) >= 12) {
                            if (tarrominPotions.validate()) {
                                antiban.randomMove();
                                tarrominPotions.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 6;
                                taskParam = 0;
                            }
                        } else {
                            if (herbloreBelow12Lvl.validate()) {
                                antiban.randomMove();
                                herbloreBelow12Lvl.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 6;
                                taskParam = 1;
                            }
                        }
                        break;
                    case 7:
                        if (getSkills().getDynamic(Skill.CRAFTING) >= 10) {
                            if (spinningFlax.validate()) {
                                antiban.randomMove();
                                spinningFlax.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 7;
                                taskParam = 0;
                            }
                        } else {
                            if (cuttingOpals.validate()) {
                                antiban.randomMove();
                                cuttingOpals.execute(taskParam);
                            }
                            else {
                                taskID = -2;
                                prevTaskID = 7;
                                taskParam = 1;
                            }
                        }
                        break;
                    case 8:
                        if (trainingMagic.validate()) {
                            antiban.randomMove();
                            trainingMagic.execute(taskParam);
                        }
                        else {
                            taskID = -2;
                            prevTaskID = 8;
                            taskParam = 0;
                        }
                        break;
                    case -3:
                        try {
                            goldAmount = grandExchangeSell.sellItems(2, bankManager, grandExchangeManager);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (goldAmount == -999) {

                            sleepUntil = random(28800, 39600) + System.currentTimeMillis() / 1000;

                            customBreakManager.startBreaking(sleepUntil, true);

                            isBreaking = true;
                        }
                        break;
                    case -2:
                        log("Case -2 started");
                        log("Getting items");

                        int state = getItemsFromBank.getItems(prevTaskID, taskParam, bankManager, changingTask);

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

                        changingTask = false;

                        if (taskUntil == -1) {

                            log("Ockohnik");

                            taskUntil = System.currentTimeMillis() / 1000 + task.getTaskDuration();

                        }

                        taskID = task.getTaskID();
                        taskParam = random(1, 2);

                        taskManager();
                        break;
                }
            }
        } else {
            log("/5");
            taskManager();
        }

        return random(500, 700);
    }

    @Override
    public final void onExit() {

    }

    @Override
    public void onPaint(final Graphics2D g) {
//Check for warning keywords in chat
        if (warningEnabled && checkChat(warningChatKeywords)) {
            showWarning = true;
        }

        // Timer Variables
        millis = (System.currentTimeMillis() - startTime);
        hours = (millis / 3600000L);
        millis -= hours * 3600000L;
        minutes = (millis / 60000L);
        millis -= minutes * 60000L;
        seconds = (millis / 1000L);

        ArrayList<Skill> paintSkills = new ArrayList<Skill>();
        ArrayList<Double> startingExp = new ArrayList<Double>();
        ArrayList<Integer> startingLvl = new ArrayList<Integer>();

        Graphics2D g2 = (Graphics2D) g;

        FontMetrics fmb = g2.getFontMetrics(baseFont);
        FontMetrics fmBold = g2.getFontMetrics(boldFont);
        FontMetrics fmWarning = g2.getFontMetrics(warningFont);
        FontRenderContext context = g2.getFontRenderContext();

        //Title Paint Dimensions
        titleRowHeight = fmb.getHeight() + textPadding;
        if (customInfo != null)
            titlePaintHeight = titleRowHeight * (5 + customInfo.length) + textPadding;
        else
            titlePaintHeight = titleRowHeight * 5 + textPadding;
        titlePaintX = 10 + skillsRowLength;
        titlePaintY = 480 - titlePaintHeight;
        Rectangle titlePaintRect = new Rectangle(titlePaintX, titlePaintY, titlePaintLength + 2 * textPadding, titlePaintHeight);

        //Rectangles for buttons & warning screen
        hideBtn = new Rectangle(titlePaintX + textPadding, titlePaintY + titlePaintHeight - titleRowHeight, lengthHide + 4, titleRowHeight - textPadding);
        resetBtn = new Rectangle(titlePaintX + 4 * textPadding + lengthHide, titlePaintY + titlePaintHeight - titleRowHeight, lengthReset + 4, titleRowHeight - textPadding);
        Rectangle warningRect = new Rectangle(0, 0, 520, 340);

        //Display only "Show" button if paint is hidden
        if (hidePaint) {
            g2.setColor(paintFillColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, paintTransparency));
            g2.fill(hideBtn);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    1.00f));
            g2.setColor(paintOutlineColor);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(hideBtn);
            g2.setFont(boldFont);
            g2.setColor(dividerColor);
            g2.drawString(sshow, getCenterX(titlePaintX + textPadding, lengthHide + 4, sshow, fmBold),
                    getCenterY(titlePaintY + titlePaintHeight - titleRowHeight, titleRowHeight - textPadding, fmBold));
            if (boldFont.getStringBounds(sshow, context).getWidth() > lengthHide)
                lengthHide = (int) boldFont.getStringBounds(sshow, context).getWidth();
        } else {

            //Title Paint Fill & Draw
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, paintTransparency));
            if (showWarning) {
                g2.setColor(warningColor);
                g2.fill(warningRect);
            }
            g2.setColor(paintFillColor);
            g2.fill(titlePaintRect);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(paintOutlineColor);
            g2.draw(titlePaintRect);
            if (showWarning) {
                g2.setColor(Color.white);
                g2.setFont(warningFont);
                g2.drawString("Warning: Keyword in Chat", (520 - fmWarning.stringWidth("Warning: Keyword in Chat")) / 2, (fmWarning.getAscent() + (340 - (fmWarning.getAscent() + fmWarning.getDescent())) / 2));
            }

            //Add Hide and Reset Buttons
            g2.setColor(paintOutlineColor);
            g2.draw(hideBtn);
            g2.draw(resetBtn);
            g2.setFont(boldFont);
            g2.setColor(dividerColor);
            g2.drawString(shide, getCenterX(titlePaintX + textPadding, lengthHide + 4, shide, fmBold),
                    getCenterY(titlePaintY + titlePaintHeight - titleRowHeight, titleRowHeight - textPadding, fmBold));
            if (boldFont.getStringBounds(shide, context).getWidth() > lengthHide)
                lengthHide = (int) boldFont.getStringBounds(shide, context).getWidth();
            g2.drawString(reset, getCenterX(titlePaintX + 4 * textPadding + lengthHide, lengthReset + 4, reset, fmBold),
                    getCenterY(titlePaintY + titlePaintHeight - titleRowHeight, titleRowHeight - textPadding, fmBold));
            if (boldFont.getStringBounds(reset, context).getWidth() > lengthReset)
                lengthReset = (int) boldFont.getStringBounds(reset, context).getWidth();

            //Add Title
            g2.setColor(captionColor);
            g2.setFont(boldFont);
            g2.drawString(title, getCenterX(titlePaintX + textPadding, titlePaintLength, title, fmBold), getCenterY(titlePaintY + textPadding, titleRowHeight, fmBold));
            if (boldFont.getStringBounds(title, context).getWidth() + 2 * textPadding > titlePaintLength)
                titlePaintLength = (int) boldFont.getStringBounds(title, context).getWidth() + 2 * textPadding;

            //Add text
            ArrayList<String> textTitlePaint = new ArrayList<String>();
            textTitlePaint.add("Time Ran: " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
            if (customInfo != null) {
                for (int i = 0; i < customInfo.length; i++) {
                    textTitlePaint.add(customInfo[i]);
                }
            }
            textTitlePaint.add("World " + getWorlds().getCurrentWorld() + " | " + (getMap().getPlayers().getAll().size() - 1) + " players on map");

            g2.setColor(textColor);
            g2.setFont(baseFont);
            for (int i = 0; i < textTitlePaint.size(); i++) {
                g2.drawString(textTitlePaint.get(i), getCenterX(titlePaintX + textPadding, titlePaintLength, textTitlePaint.get(i), fmb), getCenterY(titlePaintY + textPadding, titleRowHeight, fmb) + (1 + i) * titleRowHeight);
                if (baseFont.getStringBounds(textTitlePaint.get(i), context).getWidth() > titlePaintLength)
                    titlePaintLength = (int) baseFont.getStringBounds(textTitlePaint.get(i), context).getWidth();
            }
            g2.setColor(captionColor);
            String signature = "Z3Die & G3R4M0N";
            g2.drawString(signature, getCenterX(titlePaintX + textPadding, titlePaintLength, signature, fmb), getCenterY(titlePaintY + titlePaintHeight - titleRowHeight, titleRowHeight - textPadding, fmBold) - titleRowHeight);

            //Check for an experience gain in any skill
            for (int i = 0; i < allSkills.length; i++) {
                Skill s = allSkills[i];
                if (skills.getExperience(s) != startingExpAll[i]) {
                    paintSkills.add(s);
                    startingLvl.add((int) startingLevelsAll[i]);
                    startingExp.add(startingExpAll[i]);
                }
            }
            l = paintSkills.size();

            //If experience has been gained in any skill, skills paint is initialized
            if (l > 0) {
                //Skills Paint Dimensions
                rowHeight = fmb.getHeight() + 2 * textPadding;
                skillsPaintHeight = (l + 1) * rowHeight;
                skillsPaintX = 10;
                skillsPaintY = 480 - skillsPaintHeight;
                Rectangle skillsPaintRect = new Rectangle(skillsPaintX, skillsPaintY, skillsRowLength, skillsPaintHeight);
                rowY = new int[l];
                skillRectangles = new Rectangle[l];
                progressRectangles = new Rectangle[l];

                //Calculate data for skills paint
                skillsPaintData = new String[l + 1][6];
                String[] skillsPaintCaptions = {"Skill", "LVL", "kXP", "kXP/hr", "%TNL", "TimeTNL"};
                for (int i = 0; i < 6; i++) {
                    skillsPaintData[0][i] = skillsPaintCaptions[i];
                }
                for (int i = 1; i < l + 1; i++) {
                    Skill s = paintSkills.get(i - 1);
                    skillsPaintData[i][0] = s.name();
                    skillsPaintData[i][1] = getSkills().getStatic(s) + "/" + startingLvl.get(i - 1);
                    double xpGained = getSkills().getExperience(s) - startingExp.get(i - 1);
                    DecimalFormat df = new DecimalFormat("###.##");
                    skillsPaintData[i][2] = df.format(xpGained / 1000) + "";
                    double xpPhr = ((int) (xpGained * 3600000.0D / (System.currentTimeMillis() - startTime)));
                    skillsPaintData[i][3] = df.format(xpPhr / 1000) + "";
                    skillsPaintData[i][4] = Double.toString((100 * (experiencePrevLevel(s) / experienceTotalNeeded(s)))).substring(0, 4);
                    skillsPaintData[i][5] = formatTime(timeTnl(experienceToNextLevel(s), xpPhr));
                }
                for (int i = 0; i < l; i++) {
                    Skill s = paintSkills.get(i);
                    rowY[i] = (skillsPaintY + (i + 1) * rowHeight);
                    skillRectangles[i] = new Rectangle(skillsPaintX, rowY[i], skillsRowLength, rowHeight);
                    progressRectangles[i] = (new Rectangle(skillsPaintX, rowY[i] + 2, 0, rowHeight - 4));
                    progressRectangles[i].setSize((int) ((100 * (experiencePrevLevel(s) / experienceTotalNeeded(s))) * skillsRowLength / 100), rowHeight - 4);
                }

                //Add transparent paint background
                g2.setColor(paintFillColor);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, paintTransparency));
                g2.fill(skillsPaintRect);

                //Add progress bars & progress bar outline
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < l; i++) {
                    g2.setColor(progressBarFillColor);
                    g2.fill(progressRectangles[i]);
                    g2.setColor(progressBarOutlineColor);
                    g2.draw(progressRectangles[i]);
                }

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        1.00f));

                //Add lines to seperate skills
                g2.setColor(paintOutlineColor);
                for (int i = 0; i < l; i++) {
                    g2.drawLine(skillsPaintX, rowY[i], skillsPaintX + skillsRowLength, rowY[i]);
                }

                //Draw thick line around paint
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(skillsPaintRect);
                g2.draw(titlePaintRect);

                //Arrays to keep track of string lengths for dynamic window size optimization
                int[] x = {skillsPaintX,
                        skillsPaintX + lengthC1,
                        skillsPaintX + lengthC1 + lengthC2,
                        skillsPaintX + lengthC1 + lengthC2 + lengthC3,
                        skillsPaintX + lengthC1 + lengthC2 + lengthC3 + lengthC4,
                        skillsPaintX + lengthC1 + lengthC2 + lengthC3 + lengthC4 + lengthC5};
                int[] lengths = {lengthC1, lengthC2, lengthC3, lengthC4, lengthC5, lengthC6};
                sd = "|";

                //Add Skill Paint Captions
                g2.setFont(boldFont);
                g2.setColor(captionColor);
                for (int i = 0; i < 6; i++) {
                    g2.drawString(skillsPaintData[0][i], getCenterX(x[i] + i * lengthDivide + (1 + 2 * i) * textPadding, lengths[i], skillsPaintData[0][i], fmBold), getCenterY(skillsPaintY, rowHeight, fmBold));
                    if (boldFont.getStringBounds(skillsPaintData[0][i], context).getWidth() > lengths[i]) {
                        lengths[i] = (int) boldFont.getStringBounds(skillsPaintData[0][i], context).getWidth();

                    }
                }

                //Add dividers
                g2.setColor(dividerColor);
                context = g2.getFontRenderContext();
                for (int i = 0; i < l + 1; i++) {
                    for (int j = 0; j < 5; j++) {
                        g2.drawString(sd, getCenterX(x[j + 1] + j * lengthDivide + (2 + 2 * j) * textPadding, lengthDivide, sd, fmBold), getCenterY(skillsPaintY + rowHeight * i, rowHeight, fmBold));
                        lengthDivide = (int) boldFont.getStringBounds(sd, context).getWidth();
                    }
                }

                //Add data
                g2.setFont(baseFont);
                g2.setColor(textColor);
                for (int i = 1; i < l + 1; i++) {
                    for (int j = 0; j < 6; j++) {
                        g2.drawString(skillsPaintData[i][j], getCenterX(x[j] + j * lengthDivide + (1 + 2 * j) * textPadding, lengths[j], skillsPaintData[i][j], fmb), getCenterY(skillsPaintY + rowHeight * i, rowHeight, fmb));
                        if (baseFont.getStringBounds(skillsPaintData[i][j], context).getWidth() > lengths[j]) {
                            lengths[j] = (int) baseFont.getStringBounds(skillsPaintData[i][j], context).getWidth();
                        }
                    }
                }

                //Update lengths
                lengthC1 = lengths[0];
                lengthC2 = lengths[1];
                lengthC3 = lengths[2];
                lengthC4 = lengths[3];
                lengthC5 = lengths[4];
                lengthC6 = lengths[5];
                skillsRowLength = lengthC1 + lengthC2 + lengthC3 + lengthC4 + lengthC5 + lengthC6 + lengthDivide * 5 + textPadding * 12;
            }

        }
    }

    public void solver() {
        RS2Widget decline = getWidgets().get(326, 96);

        if (decline != null && decline.isVisible()) decline.interact();
    }

    private void goToGrandExchange() {
        if (!Banks.GRAND_EXCHANGE.contains(myPosition())) {
            bankManager.openBank();
            log("/6");

            String ringOfWealth[] = "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)".split(",");

            boolean isTeleportExists = false;

            for (String ring : ringOfWealth) {
                if (getInventory().contains(ring)) {
                    getBank().close();
                    log("/7");

                    getInventory().getItem(ring).interact("Rub");

                    Sleep.sleepUntil(() -> getWidgets().get(219, 1, 2) != null && getWidgets().get(219, 1, 2).isVisible(), 5000, 500);

                    RS2Widget RoW = getWidgets().get(219, 1, 2);

                    RoW.interact();

                    isTeleportExists = true;

                    break;
                } else if (getBank().contains(ring)) {
                    log("/8");

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
                log("/9");

                while (!getWalking().webWalk(Banks.GRAND_EXCHANGE)) ;
                bankManager.openBank();
                Sleep.sleepUntil(random(500, 1200));
                getBank().depositAll();
                Sleep.sleepUntil(random(500, 1200));
                getBank().depositWornItems();

            }
        } else {
            bankManager.openBank();
            Sleep.sleepUntil(random(500, 1200));
            getBank().depositAll();
            Sleep.sleepUntil(random(500, 1200));
            getBank().depositWornItems();
        }
    }

    public void taskManager() {
        log("TaskManager");
        if ((System.currentTimeMillis() / 1000) > taskUntil || System.currentTimeMillis() > sleepUntil) {
            if (taskTotalCount < 20) {
                if (tasksCountWithoutSleep < 3) {
                    if (random(1, 3) > 1 || tasksCountWithoutSleep == 0) {

                        Timetable[] timetable = {
                                new Timetable(1920, 2820, 2, 3),
                                new Timetable(1920, 2820, 2, 3),
                                new Timetable(1920, 2820, 2, 3),
                                new Timetable(1920, 2820, 2, 3),
                                new Timetable(2000, 3000, 4, 2),
                                new Timetable(1920, 2820, 2, 3),
                                new Timetable(1920, 2820, 2, 3),
                                new Timetable(2000, 3000, 4, 1),
                                new Timetable(2000, 3000, 4, 1),
                        };

                        boolean loop = true;

                        int newTask = -1;

                        while (loop) {
                            newTask = random(0, timetable.length - 1);
                            int taskCount = 0;
                            boolean allowed = true;

                            for (int i = 0; i < taskHistory.size(); i++) {
                                if(taskHistory.get(i).getTaskID() == newTask) ++taskCount;
                                if(timetable[newTask].getMinIntervalBetweenSameTasks() < (i+1) && taskHistory.get(i).getTaskID() == newTask) allowed = false;
                            }

                            for (int i = 0; i < taskHistory.size(); i++) {
                                if(taskCount == timetable[newTask].getMaxTasksPerDay()) break;
                                else if(allowed) loop = false;
                            }
                        }

                        taskID = newTask;

                        log("Task ID: " + newTask);

                        int workDuration = random(timetable[newTask].getMinTime(), timetable[newTask].getMaxTime());
                        //int taskParam; If suck some dick
                        task = new TaskData(taskID, 0, workDuration);

                        taskHistory.add(task);

                        taskUntil = -1;

                        ++tasksCountWithoutSleep;
                        ++taskTotalCount;

                        sleepUntil = Long.MAX_VALUE;

                        changingTask = true;

                        isBreaking = false;
                    } else {
                        tasksCountWithoutSleep = 0;

                        log("Going to sleep 1");

                        sleepUntil = random(2880000, 4200000);
//                        sleepUntil = random(120, 180) * 1000L + System.currentTimeMillis();

                        log(sleepUntil);

                        customBreakManager.startBreaking(sleepUntil, true);

                        isBreaking = true;

                        taskUntil = Long.MAX_VALUE;
                    }
                } else {

                    log("Going to sleep 2");

                    tasksCountWithoutSleep = 0;

                    sleepUntil = random(2880000, 4200000);
//                    sleepUntil = 60000;

                    log(sleepUntil);

                    customBreakManager.startBreaking(sleepUntil, true);

                    isBreaking = true;

                    taskUntil = Long.MAX_VALUE;
                }
            } else {
                log("/16");

                taskTotalCount = 0;

                taskID = -3;
            }
        }
    }

    MouseListener listener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            p = mouse.getPosition();
            if (hideBtn.contains(p)) {
                hidePaint = !hidePaint;
            } else if (resetBtn.contains(p)) {
                for (int i = 0; i < allSkills.length; i++) {
                    startingLevelsAll[i] = getSkills().getStatic(allSkills[i]);
                    startingExpAll[i] = getSkills().getExperience(allSkills[i]);
                }
                startTime = System.currentTimeMillis();
                skillsRowLength = 0;
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    };

    private int getCenterX(int x, int w, String s, FontMetrics f) {
        return x + (w - f.stringWidth(s)) / 2;
    }

    private int getCenterY(int y, int h, FontMetrics f) {
        return y + (f.getAscent() + (h - (f.getAscent() + f.getDescent())) / 2);
    }

    private boolean checkChat(String[] keywords) {
        for (String s : keywords) {
            if (chatbox.contains(Chatbox.MessageType.ALL, s)) {
                return true;
            }
        }
        return false;
    }

    private int experienceToNextLevel(Skill skill) {
        int xp = getSkills().getExperience(skill);
        for (int i = 0; i < 99; i++) {
            if (xp < xpForLevels[i]) {
                return xpForLevels[i] - xp;
            }
        }
        return 200000000 - xp;
    }

    private double experiencePrevLevel(Skill skill) {
        int xp = getSkills().getExperience(skill);
        for (int i = 0; i < 99; i++) {
            if (xp < xpForLevels[i]) {
                return xp - xpForLevels[i - 1];
            }
        }
        return 200000000 - xp;
    }

    private double experienceTotalNeeded(Skill skill) {
        int xp = getSkills().getExperience(skill);
        for (int i = 0; i < 99; i++) {
            if (xp < xpForLevels[i]) {
                return xpForLevels[i] - xpForLevels[i - 1];
            }
        }
        return 200000000 - xp;
    }

    long timeTnl(double xpTNL, double xpPH) {

        if (xpPH > 0) {
            long timeTNL = (long) ((xpTNL / xpPH) * 3600000.0D);
            return timeTNL;
        }
        return 0;
    }

    private String formatTime(long time) {
        int sec = (int) (time / 1000L), d = sec / 86400, h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return new StringBuilder()
                .append(h < 10 ? new StringBuilder().append("0").append(h)
                        .toString() : Integer.valueOf(h))
                .append(":")
                .append(m < 10 ? new StringBuilder().append("0").append(m)
                        .toString() : Integer.valueOf(m))
                .append(":")
                .append(s < 10 ? new StringBuilder().append("0").append(s)
                        .toString() : Integer.valueOf(s)).toString();
    }
}
