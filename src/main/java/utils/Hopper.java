package utils;

import org.osbot.rs07.api.Client;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.osbot.rs07.input.mouse.WidgetDestination;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;

import java.awt.*;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Hopper extends MethodProvider {

    private final static int WIDGET_INDEX_ROOT = 14;
    private final static int WIDGET_ROOT = 69;
    private final static int WIDGET_SCROLL = 15;
    private final static int WIDGET_SCROLL_UP = 4;
    private final static int WIDGET_SCROLL_DOWN = 5;

    private final static int MIN_FRAME_Y = 230;
    private final static int MAX_FRAME_Y = 416;


    public boolean hop(int world, BooleanSupplier condition) {

        if(condition.getAsBoolean())
            return false;

        if (getClient().getLoginState() != Client.LoginState.LOGIN_SUCCESSFUL)
            return false;

        if (trimWorldNumber(getWorlds().getCurrentWorld()) == world)
            return true;

        if (getTabs().getOpen().equals(Tab.LOGOUT)) {

            if (isQuickhopOpen()) {

                if (loadingWorlds())
                    return false;

                RS2Widget worldWidget = getWorld(world);

                if (worldWidget == null)
                    return false;

                if (isSelectable(worldWidget)) {

                    Rectangle rect = worldWidget.getBounds();
                    rect.translate(0, 2);
                    rect.setSize((int) rect.getWidth(), (int) rect.getHeight() - 4);
                    RectangleDestination dest = new RectangleDestination(getBot(), rect);
                    if (getMouse().click(dest, false))
                        return false;

                } else
                    scrollToWidget(worldWidget, condition);

            } else
                openQuickhop();

        } else
            getTabs().open(Tab.LOGOUT);

        return false;

    }

    private boolean scrollToWidget(RS2Widget widget, BooleanSupplier condition) {

        RS2Widget scroll;

        if (widget.getPosition().getY() < MIN_FRAME_Y) {
            scroll = getWidgets().get(WIDGET_ROOT, WIDGET_SCROLL, WIDGET_SCROLL_UP);
        } else if (widget.getPosition().getY() > MAX_FRAME_Y) {
            scroll = getWidgets().get(WIDGET_ROOT, WIDGET_SCROLL, WIDGET_SCROLL_DOWN);
        } else {
            return false;
        }

        if (scroll != null) {
            final long startTime = System.currentTimeMillis();
            WidgetDestination wDest = new WidgetDestination(getBot(), scroll);
            getMouse().continualClick(wDest, new Condition() {
                @Override
                public boolean evaluate() {
                    return isSelectable(widget) || (System.currentTimeMillis() - startTime) >= 6000 || condition.getAsBoolean();
                }
            });
        }

        return true;
    }

    private boolean isSelectable(RS2Widget widget) {
        return widget.getPosition().getY() >= MIN_FRAME_Y && widget.getPosition().getY() <= MAX_FRAME_Y;
    }

    private RS2Widget getWorld(int world) {
        @SuppressWarnings("unchecked")
        RS2Widget worldWidget = getWidgets().filter(WIDGET_ROOT, (Filter<RS2Widget>) (RS2Widget w) -> {

            if (!w.isThirdLevel() || w.getSecondLevelId() != WIDGET_INDEX_ROOT || w.getThirdLevelId() <= 300 || w.getThirdLevelId() > 400 || w.getHiddenUntilMouseOver()) {
                return false;
            }

            int worldNumber = trimWorldNumber(w.getThirdLevelId());
            return worldNumber == world;

        }).get(0);
        return worldWidget;
    }

    private int trimWorldNumber(int number) {
        return (number > 300) ? number - 300 : number;
    }

    public boolean loadingWorlds() {
        return !getWidgets().containingText("Loading...").isEmpty();
    }

    private boolean isQuickhopOpen() {
        return getTabs().getOpen() == Tab.LOGOUT && getWidgets().containingText("World Switcher").isEmpty();
    }

    private boolean openQuickhop() {
        List<RS2Widget> list = getWidgets().containingText("World Switcher");
        if (!list.isEmpty()) {
            return list.get(0).interact();
        }
        return false;
    }

}