package com.qnh.weekly;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.StyleSheet;
import com.philemonworks.typewise.UIBuilder;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.typed.EmailField;
import com.philemonworks.typewise.cwt.typed.NumberField;

/**
 * @author E.M.Micklei
 * 
 * Example program startline
 * D:\TypeWise\TypeWise.EXE http://localhost:8081/weekly/weektool/main
 *
 */
public class WeekTool extends ApplicationModel {
    private String[] headers = new String[] { "Ma", "Di", "Wo", "Do", "Vr", "Za", "Zo" };
    private String[] topics = null;
    private static final int TOPICWIDTH = 22;
    private static final int TIMEWIDTH = 5;
    private static final int DETAILSWIDTH = 22;
    private NumberField yearField = null;
    private NumberField weekField = null;

    public WeekTool(ApplicationModel parent) {
        super(parent);
    }

    public static void main(String[] args) {
        WeekTool tool = new WeekTool(null);
        tool.loadTopics();
        tool.mainScreen().preview();
        tool.test("");
    }

    public void main(HashMap args) {
        new Login(this).main(args);
    }

    public Screen mainScreen() {
        this.loadTopics();
        UIBuilder ui = new UIBuilder();
        Screen main = ui.addScreen("main", this, topics.length + 13, 65);
        main.setTitle("QNH - Weekly");
        main.setStyleSheet(this.createStyleSheet());
        main.setStyle("default");
        ui.setLeftEdge(2);
        ui.setTopEdge(2);
        this.addDetails(ui);
        ui.setLeftEdge(DETAILSWIDTH + TOPICWIDTH + 2);
        ui.setTopEdge(2);
        this.addDetails2(ui);
        ui.setLeftEdge(2);
        ui.setTopEdge(9);
        this.addWeekHours(ui);
        this.addButtons(ui);
        ui = null; // release it
        return main;
    }

    /**
     * This method is sent just before a Screen is returned to the presentation.
     * Fill the day numbers of the days of the current week + fill the year and month.
     */
    public void afterShow() {
        this.fillDates();
    }

    /**
     * Load the available topics from the file named topics.txt
     * It should be available in the directory where the Java classes are found.
     */
    public void loadTopics() {
        if (topics != null)
            return;
        ArrayList lines = new ArrayList();
        try {
            InputStream in = this.getClass().getResourceAsStream("topics.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while (reader.ready()) {
                lines.add(reader.readLine());
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass()).error("Unable to read topics.txt", ex);
        }
        topics = new String[lines.size()];
        System.arraycopy(lines.toArray(), 0, topics, 0, topics.length);
    }

    /**
     *  Create and return the stylesheet definition that can be read from the file stylesheet.properties.
     *  This file should be available in the directory of the web application.
     *  @return StyleSheet : loaded from a properties file
     */
    public StyleSheet createStyleSheet() {
        // URL url = ClassLoader.getSystemResource("stylesheet.properties");
        StyleSheet sheet = new StyleSheet(System.getProperty("user.dir") + "//stylesheet.properties");
        sheet.load();
        return sheet;
    }

    /**
     * Add the buttons in the footer of the screen
     * @param ui
     * 	the builder that is used to compose a Screen
     */
    public void addButtons(UIBuilder ui) {
        ui.setPosition(ui.top().getRows() - 1, 2);
        Button reset = ui.addButton("reset", " F2-Wissen ");
        reset.setStyle("button");
        reset.onSendTo(EVENT_CLICKED, "resetAll", this);
        ui.top().onSendTo(EVENT_F2, "resetAll", this);
        ui.space(1);
        Button compute = ui.addButton("compute", " F3-Bereken ");
        compute.setStyle("button");
        compute.onSendTo(EVENT_CLICKED, "computeAll", this);
        ui.top().onSendTo(EVENT_F3, "computeAll", this);
        ui.space(1);
        Button email = ui.addButton("email", " F4-Email ");
        email.setStyle("button");
        email.onSendTo(EVENT_CLICKED, "sendEmails", this);
        ui.top().onSendTo(EVENT_F4, "sendEmails", this);
    }

    /**
     * Add the project details to the screen
     * @param ui
     * 	the builder that is used to compose a Screen
     */
    public void addDetails(UIBuilder ui) {
        Label employeeL = ui.addLabel("employeeL", " Medewerk(st)er:", DETAILSWIDTH);
        employeeL.setStyle("label");
        TextField employeeF = ui.addTextField("employeeF", DETAILSWIDTH);
        employeeF.setStyle("field");
        ui.cr(1);
        Label customerL = ui.addLabel("customerL", " Klant:", DETAILSWIDTH);
        customerL.setStyle("label");
        TextField customerF = ui.addTextField("customerF", DETAILSWIDTH);
        customerF.setStyle("darkfield");
        ui.cr(1);
        Label managerL = ui.addLabel("managerL", " Opdrachtgever:", DETAILSWIDTH);
        managerL.setStyle("label");
        TextField managerF = ui.addTextField("managerF", DETAILSWIDTH);
        managerF.setStyle("field");
        ui.cr(1);
        Label poL = ui.addLabel("poL", " PO nr:", DETAILSWIDTH);
        poL.setStyle("label");
        TextField poF = ui.addTextField("poF", DETAILSWIDTH);
        poF.setStyle("darkfield");
        ui.cr(1);
        Label refL = ui.addLabel("refL", " Email klant:", DETAILSWIDTH);
        refL.setStyle("label");
        EmailField refF = new EmailField("refF", ui.getRow(), ui.getColumn(), 1, DETAILSWIDTH);
        ui.add(refF);
        refF.setStyle("field");
    }

    /**
     * Add some Date details to the screen
     * @param ui
     * 	the builder that is used to compose a Screen
     */
    public void addDetails2(UIBuilder ui) {
        Label yearL = ui.addLabel("yearL", " Jaar:", 10);
        yearL.setStyle("label");
        yearField = new NumberField("yearF", ui.getRow(), ui.getColumn(), 1, 4);
        ui.add(yearField);
        yearField.onSendTo(EVENT_LOSINGFOCUS, "changedYear", this);
        yearField.setStyle("field");
        yearField.setIntValue(Utils.year());
        ui.space(4);
        Button yearDown = ui.addButton("yearDown", " < ");
        yearDown.onSendTo(EVENT_CLICKED, "gotoPreviousYear", this);
        yearDown.setStyle("button");
        Button yearUp = ui.addButton("yearUp", " > ");
        yearUp.onSendTo(EVENT_CLICKED, "gotoNextYear", this);
        yearUp.setStyle("button");
        ui.cr(1);
        Label weekL = ui.addLabel("weekL", " Week:", 10);
        weekL.setStyle("label");
        weekField = new NumberField("weekF", ui.getRow(), ui.getColumn(), 1, 4);
        ui.add(weekField);
        weekField.onSendTo(EVENT_LOSINGFOCUS, "changedWeek", this);
        weekField.setIntValue(Utils.weekNumber());
        weekField.setStyle("field");
        ui.space(4);
        Button weekDown = ui.addButton("weekDown", " < ");
        weekDown.onSendTo(EVENT_CLICKED, "gotoPreviousWeek", this);
        weekDown.setStyle("button");
        Button weekUp = ui.addButton("weekUp", " > ");
        weekUp.onSendTo(EVENT_CLICKED, "gotoNextWeek", this);
        weekUp.setStyle("button");
    }

    /**
     * Add fields for storing the hour totals per week, per day and per topic
     * @param ui
     * 	the builder that is used to compose a Screen
     */
    public void addWeekHours(UIBuilder ui) {
        int top = ui.getRow();
        int left = ui.getColumn();
        ui.cr(1);
        // vertical list of topics
        for (int t = 0; t < topics.length; t++) {
            Label label = ui.addLabel("topic" + t, " " + topics[t], TOPICWIDTH);
            label.setStyle("label");
            ui.cr(1);
        }
        left += TOPICWIDTH;
        ui.setTopEdge(top);
        int row = top;
        int column = left;
        // horizontal list of day names
        for (int h = 0; h < headers.length; h++) {
            ui.setPosition(row, column);
            Label label = ui.addLabel("header" + h, " " + headers[h], TIMEWIDTH);
            // label.setAlignment(ALIGNMENT_RIGHT);
            label.setStyle("label");
            ui.setPosition(row - 1, column);
            label = ui.addLabel("date" + h, " " + (20 + h), TIMEWIDTH);
            label.setStyle("date");
            column += TIMEWIDTH;
        }
        column = left;
        row += 1;
        // matrix of time fields
        int f = 0;
        for (int r = 0; r < topics.length; r++) {
            ui.setPosition(row, column);
            for (int h = 0; h < headers.length; h++) {
                TextField field = ui.addTextField(headers[h] + r, TIMEWIDTH);
                if (f++ % 2 == 0)
                    field.setStyle("field");
                else
                    field.setStyle("darkfield");
                field.setMaxLength(TIMEWIDTH);
            }
            Label label = ui.addLabel("topicTotal" + r, "", TIMEWIDTH);
            label.setStyle("subtotal");
            label.setAlignment(CWT.RIGHT);
            column = left;
            row += 1;
        }
        // Total per header
        for (int h = 0; h < headers.length; h++) {
            ui.setPosition(row, column);
            Label label = ui.addLabel("dayTotal" + h, "", TIMEWIDTH);
            label.setStyle("subtotal");
            label.setAlignment(CWT.RIGHT);
            column += TIMEWIDTH;
        }
        Label label = ui.addLabel("total", "0.0", TIMEWIDTH);
        label.setStyle("total");
        label.setAlignment(CWT.RIGHT);
    }

    /**
     * Update all date related information
     */
    public void changedWeek() {
        this.fillDates();
    }

    /**
     * Update all date related information
     */
    public void changedYear() {
        this.fillDates();
    }

    /**
     * Set the current year to the previous one. 
     * No check for valid year (yet)
     */
    public void gotoPreviousYear() {
        yearField.setIntValue(yearField.getIntValue() - 1);
        this.fillDates();
    }

    /**
     * Set the current year to the next one. 
     * No check for valid year (yet)
     */
    public void gotoNextYear() {
        yearField.setIntValue(yearField.getIntValue() + 1);
        this.fillDates();
    }

    /**
     * Set the current week to the previous one. 
     * Minimum value is 1
     */
    public void gotoPreviousWeek() {
        weekField.setIntValue(Math.max(1, weekField.getIntValue() - 1));
        this.fillDates();
    }

    /**
     * Set the current week to the next one. 
     * Maximum value is 54 (for now)
     */
    public void gotoNextWeek() {
        weekField.setIntValue(Math.min(54, weekField.getIntValue() + 1)); // should compute weeks of year
        this.fillDates();
    }

    /**
     * (Re)compute all totals (per day, per topic, per week)
     */
    public void computeAll() {
        this.fillDates();
        this.fillDayTotals();
        this.fillTopicTotals();
        this.fillTotal();
    }

    public void fillDayTotals() {
        for (int h = 0; h < headers.length; h++) {
            Label total = (Label) view.getCurrentScreen().widgetNamed("dayTotal" + h);
            int minutes = 0;
            for (int r = 0; r < topics.length; r++) {
                TextField each = (TextField) view.getCurrentScreen().widgetNamed(headers[h] + r);
                try {
                    minutes += Utils.displayToMinutes(each.getString());
                } catch (NumberFormatException ex) {
                    each.setStyle("invalidfield");
                    return;
                }
            }
            total.setString(Utils.minutesToDisplay(minutes));
        }
    }

    public void fillTopicTotals() {
        for (int r = 0; r < topics.length; r++) {
            Label total = (Label) view.getCurrentScreen().widgetNamed("topicTotal" + r);
            int minutes = 0;
            for (int h = 0; h < headers.length; h++) {
                TextField each = (TextField) view.getCurrentScreen().widgetNamed(headers[h] + r);
                try {
                    minutes += Utils.displayToMinutes(each.getString());
                } catch (NumberFormatException ex) {
                    each.setStyle("invalidfield");
                    return;
                }
            }
            total.setString(Utils.minutesToDisplay(minutes));
        }
    }

    public void fillTotal() {
        int minutes = 0;
        for (int r = 0; r < topics.length; r++) {
            Label total = (Label) view.getCurrentScreen().widgetNamed("topicTotal" + r);
            try {
                minutes += Utils.displayToMinutes(total.getString());
            } catch (NumberFormatException ex) {
                total.setStyle("invalidfield");
                return;
            }
        }
        Label weekTotal = (Label) view.getCurrentScreen().widgetNamed("total");
        weekTotal.setString(Utils.minutesToDisplay(minutes));
    }

    public void fillDates() {
        for (int h = 0; h < headers.length; h++) {
            Label dateLabel = (Label) view.getCurrentScreen().widgetNamed("date" + h);
            dateLabel.setString(" " + Utils.dayOfMonday(yearField.getIntValue(), weekField.getIntValue(), h));
        }
    }

    public void resetAll() {
        Logger.getLogger(this.getClass()).debug("resetAll");
        for (int r = 0; r < topics.length; r++) {
            for (int h = 0; h < headers.length; h++) {
                TextField each = (TextField) view.getCurrentScreen().widgetNamed(headers[h] + r);
                each.setString("");
            }
        }
        this.computeAll();
    }

    public void sendEmails() {}
}