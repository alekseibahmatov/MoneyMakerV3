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
import activities.WineOfZamorak.TrainingHp;
import activities.WineOfZamorak.TrainingMagic;
import activities.WineOfZamorak.WineOfZamorak;
import activities.setup.Task;
import bank.GetItemsFromBank;
import bank.utils.BankManager;
import grandExchange.GrandExchangeBuy;
import grandExchange.GrandExchangeSell;
import grandExchange.buy.GetBuyItems;
import grandExchange.sell.GetSellItems;
import grandExchange.utils.GrandExchangeManager;
import models.TaskData;
import org.osbot.Constants;
import org.osbot.rs07.api.Chatbox;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
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

    private DruidicRitual druidicRitual = new DruidicRitual();
    private MakingGuamPotions guamPotions = new MakingGuamPotions();

    private HerbloreBelow12Lvl herbloreBelow12Lvl = new HerbloreBelow12Lvl();
    private MakingTarrominPotions tarrominPotions = new MakingTarrominPotions();

    private SpinningFlax spinningFlax = new SpinningFlax();

    private TrainingMagic trainingMagic = new TrainingMagic();
    private TrainingHp trainingHp = new TrainingHp();
    private WineOfZamorak wineOfZamorak = new WineOfZamorak();


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

    private long goldAmount;

    private boolean isHouse;

    private List<Task> tasks = new ArrayList<>();

    private int tasksCountWithoutSleep = 0, taskTotalCount = 0;
    private long taskUntil, sleepUntil;
    private TaskData task;

    //------------Essentials---------------
    //Font size & Text Padding will determine the size of paint
    //Look at forum for examples
    final int baseFontSize = 14; //Recommended 12-20
    final int textPadding = 5; //space between text,columns, & rows
    final String title = "DylanSRT Paint Demo"; //add script name here
    final boolean warningEnabled = true; //Warning window pops up on paint if a player says one of the keywords, Make sure public chat is visible
    final String[] warningChatKeywords = {"bot", "botting", "report", "reported", "Botting", "Bot", "Report", "Reported"};

    //--Custom Counters - Read Forum for details
    //Set customInfo=null if you don't want to add anything
    String[] customInfo = null;

    //------------Cosmetics---------------
    final float paintTransparency = 0.60F; // 0.00F is transparent, 1.00F is opaque
    final Color textColor = new Color(255, 255, 255);
    final Color paintOutlineColor = new Color(0, 0, 0);
    final Color paintFillColor = new Color(0, 0, 0);
    final Color captionColor = new Color(139, 0, 0);
    final Color dividerColor = new Color(169, 169, 169);
    final Color progressBarFillColor = new Color(139, 0, 0);
    final Color progressBarOutlineColor = new Color(139, 0, 0);
    final Color warningColor = new Color(139, 0, 0);
    final String fontStyle = "SansSerif";
    private final Font baseFont = new Font(fontStyle, Font.PLAIN, baseFontSize);
    private final Font boldFont = new Font(fontStyle, Font.BOLD, baseFontSize);
    private final Font warningFont = new Font(fontStyle, Font.BOLD, 40);

    int skillsRowLength, titlePaintLength, rowHeight, skillsPaintHeight, titlePaintHeight, skillsPaintX, titlePaintX, skillsPaintY,
            titlePaintY, lengthC1, lengthC2, lengthC3, lengthC4, lengthC5, lengthC6, lengthDivide, lengthHide, lengthReset,
            titleRowHeight,l;
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
        trainingHp.exchangeContext(getBot());
        wineOfZamorak.exchangeContext(getBot());

        tasks.add(unicornHornGrinder);
        tasks.add(grindingDesertGoatHorns);
        tasks.add(cuttingOpals);
        tasks.add(ultracompost);
        tasks.add(hasHouse);
        tasks.add(collectingPlanks);
        tasks.add(guamPotions);
        tasks.add(herbloreBelow12Lvl);
        tasks.add(spinningFlax);
        tasks.add(trainingMagic);


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

        for (int i = 0; i < allSkills.length; i++) {
            startingLevelsAll[i] = getSkills().getStatic(allSkills[i]);
            startingExpAll[i] = getSkills().getExperience(allSkills[i]);
        }

        startTime = System.currentTimeMillis();

        // Startup setups

        List<String> configData = config.getConfig();

        timeOffset = configData.get(0);

        isHouse = houseValidation.validate();

    }

    @Override
    public int onLoop() throws InterruptedException {

        getBot().getCanvas().addMouseListener(listener);

        if (!isBreaking) {
            if (taskID == -1) taskManager();

            if (firstLoop) {
                if (tasks.get(taskID).isNeededToStartAtGE()) goToGrandExchange();
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
                            if (noHouse.validate()) noHouse.execute(taskParam);
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
                        if (getSkills().getDynamic(Skill.HERBLORE) >= 3) {
                            if (guamPotions.validate()) guamPotions.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 6;
                                taskParam = 0;
                            }
                        } else {
                            if (druidicRitual.validate()) druidicRitual.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 6;
                                taskParam = 1;
                            }
                        }
                        break;
                    case 7:
                        if (getSkills().getDynamic(Skill.HERBLORE) >= 12) {
                            if (tarrominPotions.validate()) tarrominPotions.execute(taskParam);
                            else {
                                taskID = -2;
                                prevTaskID = 7;
                                taskParam = 0;
                            }
                        } else {
                            if (herbloreBelow12Lvl.validate()) herbloreBelow12Lvl.execute(taskParam);
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
                    case 9:
                        if (trainingMagic.validate()) {
                            trainingMagic.execute(taskParam);
                        }
                        else {
                            taskID = -2;
                            prevTaskID = 9;
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

                        if(taskUntil == -1) {
                            taskUntil = System.currentTimeMillis() / 1000 + task.getTaskDuration();

                            taskID = task.getTaskID();
                        }
                        break;
                }
            }
        } else {
            taskManager();
        }

        return 0;
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
            String signature = "Created with DylanSRT Paint Template";
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

            String ringOfWealth[] = "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)".split(",");

            boolean isTeleportExists = false;

            for (String ring : ringOfWealth) {
                if (getInventory().contains(ring)) {
                    getBank().close();

                    getInventory().getItem(ring).interact("Rub");

                    Sleep.sleepUntil(() -> getWidgets().get(219, 1, 2) != null && getWidgets().get(219, 1, 2).isVisible(), 5000, 500);

                    RS2Widget RoW = getWidgets().get(219, 1, 2);

                    RoW.interact();

                    isTeleportExists = true;

                    break;
                } else if (getBank().contains(ring)) {
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

                while (GE.contains(myPosition())) getWalking().webWalk(GE);
            }
        }
    }

    public void taskManager() {
        if(System.currentTimeMillis() / 1000 > taskUntil || System.currentTimeMillis() / 1000 > sleepUntil) {
            if(taskTotalCount < 14) {
                if(tasksCountWithoutSleep < 3) {
                    if(random(1, 3) > 1 || tasksCountWithoutSleep == 0) {
                        int taskID = random(9, 9); // 4
                        int workDuration = random(1920, 2820);
                        //int taskParam; If suck some dick
                        task = new TaskData(taskID, 0, workDuration);

                        this.taskID = taskID;

                        taskUntil = -1;

                        ++tasksCountWithoutSleep;
                        ++taskTotalCount;

                        sleepUntil = Long.MAX_VALUE;

                        isBreaking = false;
                    } else {
                        tasksCountWithoutSleep = 0;

                        sleepUntil = random(2880, 4200) + System.currentTimeMillis() / 1000;

                        customBreakManager.startBreaking(sleepUntil, true);

                        isBreaking = true;

                        taskUntil = Long.MAX_VALUE;
                    }
                } else {
                    tasksCountWithoutSleep = 0;

                    sleepUntil = random(2880, 4200) + System.currentTimeMillis() / 1000;

                    customBreakManager.startBreaking(sleepUntil, true);

                    isBreaking = true;

                    taskUntil = Long.MAX_VALUE;
                }
            } else {
                taskTotalCount = 0;

                taskID = -3;
            }
        }
    }

//    public void createTaskList(int offset) throws IOException {
//        File file = new File(Constants.DATA_DIR + "\\Ultimate Money Maker V3\\tasks.txt");
//        System.gc();
//
//        if (file.exists()) {
//
//            log("Creating task list");
//
//            String devidedTime[] = timeOffset.split(":");
//
//            GregorianCalendar now = new GregorianCalendar();
//            GregorianCalendar cal = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), Integer.parseInt(devidedTime[0]), Integer.parseInt(devidedTime[1]));
//
//            cal.add(Calendar.DAY_OF_MONTH, offset);
//
////            int random = random(-60, 60);
////            cal.add(Calendar.MINUTE, random);
//
//            int rand = random(4, 6);
//
//            try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
//                for (int i = 0; i < rand; i++) {
//                    int randTask = random(9, 9); // Task cases
//
//                    int param = 0;
//
//                    if (randTask == 0 || randTask == 1 || randTask == 2) param = random(1, 2);
//
//                    int randMinutes = random(45, 120);
//
//                    long startSecs = cal.getTimeInMillis() / 1000;
//
//                    cal.add(Calendar.MINUTE, randMinutes);
//
//                    long endSecs = cal.getTimeInMillis() / 1000;
//
//                    br.write(String.format("%d;%d;%d;%d", randTask, param, startSecs, endSecs) + System.lineSeparator());
//
//                    int randSleep = random(30, 90);
//
//                    cal.add(Calendar.MINUTE, randSleep);
//                }
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            if (now.after(cal)) {
//                createTaskList(1);
//            }
//        } else {
//            file.getParentFile().mkdirs();
//            file.createNewFile();
//        }
//    }
//
//    public void selectTask() {
//        File file = new File(Constants.DATA_DIR + "\\Ultimate Money Maker V3\\tasks.txt");
//
//        if (file.exists() && file.length() != 0) {
//            GregorianCalendar now = new GregorianCalendar();
//
//            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//
//                long lineCount;
//                try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
//                    lineCount = stream.count();
//                }
//
//                boolean isNeededToRecreateFile = false;
//
//                for (int i = 0; i < lineCount; i++) {
//                    String line = br.readLine();
//
//                    if (i + 1 == lineCount && line != null && Long.parseLong(line.split(";")[3]) < now.getTimeInMillis() / 1000)
//                        isNeededToRecreateFile = true;
//                    else continue;
//                }
//
//                if (isNeededToRecreateFile) createTaskList(0);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            boolean deleteLine = false;
//            String lastLine = "";
//
//            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//
//                String line = br.readLine();
//
//                String data[] = line.split(";");
//
//                long nowSecs = now.getTimeInMillis() / 1000;
//
//                log("Changing task");
//
//                if (nowSecs > Long.parseLong(data[2]) && nowSecs < Long.parseLong(data[3])) {
//
//                    taskID = Integer.parseInt(data[0]);
//                    taskParam = Integer.parseInt(data[1]);
//
//                    untilBreak = Long.parseLong(data[3]);
//
//                    log(untilBreak);
//                    log(System.currentTimeMillis());
//
//                    firstLoop = true;
//
//                    isBreaking = false;
//                } else {
//                    if (now.getTimeInMillis() / 1000 > Long.parseLong(data[3])) {
//                        if ((lastLine = br.readLine()) == null) {
//                            createTaskList(0);
//                        } else deleteLine = true;
//                    } else if (now.getTimeInMillis() / 1000 < Long.parseLong(data[2])) {
//
//                        if (Long.parseLong(data[2]) - now.getTimeInMillis() / 1000 < 6000) {
//                            long sleepTimeSecs = (Long.parseLong(line.split(";")[2]) - (now.getTimeInMillis() / 1000)) * 1000;
//
//                            breakingUntil = nowSecs * 1000 + sleepTimeSecs;
//
//                            customBreakManager.startBreaking(sleepTimeSecs, true);
//
//                            isBreaking = true;
//                        } else {
//                            taskID = -3;
//
//                            long sleepTimeSecs = (Long.parseLong(line.split(";")[2]) - (now.getTimeInMillis() / 1000)) * 1000;
//
//                            breakingUntil = nowSecs * 1000 + sleepTimeSecs;
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (deleteLine) {
//                deleteLine(Constants.DATA_DIR + "\\Ultimate Money Maker V3\\tasks.txt", 1, 1);
//
//                long sleepTimeSecs = ((now.getTimeInMillis() / 1000) - Long.parseLong(lastLine.split(";")[2])) * 1000;
//
//                long nowSecs = now.getTimeInMillis() / 1000;
//
//                breakingUntil = nowSecs * 1000 + sleepTimeSecs;
//
//                customBreakManager.startBreaking(sleepTimeSecs, true);
//
//                isBreaking = true;
//            }
//        } else {
//            try {
//                createTaskList(0);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    void deleteLine(String filename, int startline, int numlines) {
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(filename));
//
//            //String buffer to store contents of the file
//            StringBuffer sb = new StringBuffer("");
//
//            //Keep track of the line number
//            int linenumber = 1;
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                //Store each valid line in the string buffer
//                if (linenumber < startline || linenumber >= startline + numlines)
//                    sb.append(line + "\n");
//                linenumber++;
//            }
//            if (startline + numlines > linenumber)
//                System.out.println("End of file reached.");
//            br.close();
//
//            FileWriter fw = new FileWriter(new File(filename));
//            //Write entire string buffer into the file
//            fw.write(sb.toString());
//            fw.close();
//        } catch (Exception e) {
//            System.out.println("Something went horribly wrong: " + e.getMessage());
//        }
//    }

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
