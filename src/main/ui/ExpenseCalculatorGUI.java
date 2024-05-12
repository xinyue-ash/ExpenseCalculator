package ui;


import model.Event;
import model.EventLog;
import model.GenerateReport;
import model.SingleExpenseEntry;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

// a representation of the main frame of Expense Distribution Calculator
// compose a main menu panel that has start new and load from history button;
// a panel that compose of an expense list and add/delete buttons
// a panel that compose of a budget setter and report displaying panel
// the layout for the whole ui package is learned from
// https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html

public class ExpenseCalculatorGUI extends JFrame {
    private static final int WIDTH = 2000;
    private static final int HEIGHT = 2000;

    protected GenerateReport rp;
    private Container pane;  // the JFrame itself
    private JPanel mainMenu;
    private ExpenseTable expenseListPanel;
    private BudgetPanel budgetPanel;
    private ReportPanel reportPanel;

    // set lay out and size of this frame
    // add panels to the frame
    // log all the event after frame is closed
    public ExpenseCalculatorGUI() {

        rp = new GenerateReport();

        // set layout of the frame
        pane = getContentPane();
        setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // set title of the plane
        setTitle("Expense Distribution Calculator");
        setSize(WIDTH, HEIGHT);

        // add panels
        addMenuPane();
        expenseListPanel = new ExpenseTable(this);
        addExpenseListPanel();
        addBudgetPanel();
        addReportPanel();

        // visuals

        // print event log after exit
        // format modified from
        // https://stackoverflow.com/questions/9725311/how-to-override-windowsclosing-event-in-jframe
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog();
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            }
        });
        pack();
        centreOnScreen();
        setVisible(true);
    }


    //add mainMenu that has title and main menu buttons to the frame
    // MODIFIES: this
    // EFFECTS: initialize mainMenu panel, set layout to this menu pane, add title label and main menu buttons,
    //              add this panel to main frame
    private void addMenuPane() {
        mainMenu = new JPanel();
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        addMainMenuLabel();
        mainMenu.add(addMainButtons());  // add buttons
        add(mainMenu);
    }

    // MODIFIES: this
    // EFFECTS: create/format main menu label and add it to main menu panel
    private void addMainMenuLabel() {
        JLabel mainLabel = new JLabel("Expense Distribution Calculator");
        mainLabel.setFont(new Font("Courier New", Font.BOLD, WIDTH / 100));
        mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainMenu.add(mainLabel);
    }


    // EFFECTS: create a new panel that contains "start new" and "load" buttons,
    // and calls method that represent the action on the buttons, return this button panel
    private JPanel addMainButtons() {
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainButtonPanel.add(new JButton(new StartNewRoundAction()));
        mainButtonPanel.add(new JButton(new LoadData()));
        mainButtonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        return mainButtonPanel;

    }

    // MODIFIES: this
    // EFFECTS: add expense list panel to the main frame and format it
    private void addExpenseListPanel() {
        add(expenseListPanel);
        Dimension size = new Dimension();
        size.height = this.getHeight() / 8;
        expenseListPanel.setPreferredSize(size);
    }

    // MODIFIES: this
    // EFFECTS: initialize a BudgetPanel and add it to main frame
    private void addBudgetPanel() {
        budgetPanel = new BudgetPanel(this);
        add(budgetPanel);
    }

    // MODIFIES: this
    // EFFECTS: initialize a ReportPanel and add it to main frame
    private void addReportPanel() {
        reportPanel = new ReportPanel(this);
        add(reportPanel);
    }


    // EFFECTS:Helper to centre main application window on desktop
    //adapt from AlarmSystem : AlarmControlerUI class
    //https://github.students.cs.ubc.ca/CPSC210/AlarmSystem/blob/master/src/main/ca/ubc/cpsc210/alarm/ui/AlarmControllerUI.java
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    //EFFECT: iterate events in event log and print them to the console
    private void printLog() {
        for (Event next : EventLog.getInstance()) {
            System.out.println(next.toString() + "\n");
        }
    }

    // all methods below that has structure that extends AbstractAction is learned from sample demo:
    //https://github.students.cs.ubc.ca/CPSC210/AlarmSystem/blob/34ffaafee0810f67aa7801a30ee03807de21f039/src/main/ca/ubc/cpsc210/alarm/ui/AlarmControllerUI.java

    // a class represents the mouse action on "Start New Round" button
    private class StartNewRoundAction extends AbstractAction {

        // MODIFIES: this
        // EFFECTS: set title for the button
        public StartNewRoundAction() {
            super("Start New Round");
        }

        // MODIFIES: rp, expenseListPanel,budgetPanel,reportPanel
        // EFFECTS: reset report object, reset table, budget panel and report display
        @Override
        public void actionPerformed(ActionEvent e) {
            rp = new GenerateReport();
            expenseListPanel.resetTable();
            budgetPanel.resetBudget();
            reportPanel.resetReportPanel();

        }
    }

    // a class that represent the action after clicking "Load List From Last Calculation"
    //  the method to load from json file is modified from loadData() in sample demo:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/ui/WorkRoomApp.java
    private class LoadData extends AbstractAction {

        private JsonReader jsonReader;
        private static final String JSON_STORE = "./data/report.json";
        private static final String SUCCESS_MESSAGE =
                "Loaded expense list" + " from " + JSON_STORE + "successfully :) ";
        private static final String ERROR_MESSAGE = "Unable to read from file: " + JSON_STORE;

        // MODIFIES: this
        // EFFECTS: set button display, initialize json writer
        public LoadData() {
            super("Load List From Last Calculation");
            jsonReader = new JsonReader(JSON_STORE);
        }

        //MODIFIES: rp,expenseListPanel
        //EFFECTS: loads expenses from file to the table,
        //      if catch IOException, pop up a warning message,
        //       otherwise, pop up a message indicate success
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                rp = jsonReader.read();
                expenseListPanel.resetTable();
                for (SingleExpenseEntry curExp : rp.getExpenses()) {
                    expenseListPanel.getModel().addRow(curExp.toStringArray());
                }
                JOptionPane.showMessageDialog(null,
                        SUCCESS_MESSAGE,
                        "Nice", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ie) {
                JOptionPane.showMessageDialog(null,
                        ERROR_MESSAGE,
                        "Oops", JOptionPane.ERROR_MESSAGE);
            }
        }


    }
}