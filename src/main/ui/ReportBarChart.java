package ui;

import model.Category;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;


// a class that draws a bar chart representing the amount under category in the report
// modified from https://stackoverflow.com/questions/29708147/custom-graph-java-swing
public class ReportBarChart extends JPanel {

    private ExpenseCalculatorGUI expGUI;
    private static final int CHAR_HEIGHT = 150;
    private static final int BAR_WIDTH = 50;
    private static final Color BAR_COLOR = Color.PINK;


    private JPanel barPanel;
    private JPanel labelPanel;

    private ArrayList<Bar> bars = new ArrayList<>(); // a list of bars

    // MODIFIES: this
    // EFFECTS: formats parent panel, bar panel, and label panel
    public ReportBarChart(ExpenseCalculatorGUI expGUI) {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());

        this.expGUI =  expGUI;

        barPanel = new JPanel(new GridLayout(1, 0));
        labelPanel = new JPanel(new GridLayout(1, 0));

        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
        addCategoryBars();
    }

    // MODIFIES: this
    // EFFECTS: add default bar
    public void addHistogramColumn(String label, double value, Color color) {
        Bar bar = new Bar(label, value, color);
        bars.add(bar);
    }

    // MODIFIES: this
    // EFFECTS: generate bars to histogram according to bar's value
    public void layoutHistogram() {
        barPanel.removeAll();
        labelPanel.removeAll();

        int maxValue = 0;
        maxValue = getBarMaxValue(bars, maxValue);
        setBars(maxValue);

    }

    // MODIFIES: bar
    // EFFECTS: set everyBar's height relative to maxValue for bar's value,
    //          set value label for each bar
    private void setBars(int maxValue) {
        for (Bar bar: bars) {
            setBar(maxValue, bar);
            setBarLabel(bar);
        }
    }

    // MODIFIES: labelPanel
    // EFFECTS: set label for each bar (on x-axis)
    private void setBarLabel(Bar bar) {
        JLabel barLabel = new JLabel(bar.getLabel());
        barLabel.setHorizontalAlignment(JLabel.CENTER);
        labelPanel.add(barLabel);
    }

    // MODIFIES: barPanel
    //EFFECTS: set one bar with its value label
    private void setBar(int maxValue, Bar bar) {
        JLabel valueBarWithLabel = setBarValueLabel(bar);
        int barHeight = (int) Math.round((bar.getValue() * CHAR_HEIGHT) / maxValue);
        Icon icon = new ColorIcon(bar.getColor(), BAR_WIDTH, barHeight);
        valueBarWithLabel.setIcon(icon);
        barPanel.add(valueBarWithLabel);
    }

    // EFFECTS: set value label on top of bars according to its value and returns it,
    // formats value label on top of each bars
    private JLabel setBarValueLabel(Bar bar) {
        JLabel valueLabel = new JLabel(bar.getValue() + "");
        valueLabel.setHorizontalTextPosition(JLabel.CENTER);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setVerticalTextPosition(JLabel.TOP);
        valueLabel.setVerticalAlignment(JLabel.BOTTOM);
        return valueLabel;
    }

    // MODIFIES: maxValue
    // EFFECTS: get the maximum value among the value of all bars and returns it
    private int getBarMaxValue(ArrayList<Bar> bars,int maxValue) {
        for (Bar bar: bars) {
            maxValue = Math.max(maxValue, (int)Math.round(bar.getValue()));
        }
        return maxValue;

    }

    // MODIFIES: this
    // EFFECTS: adds bars to bar chart panel by accessing the value under user data
    //          in expGUI.rp
    private void addCategoryBars() {
        for (Category c : expGUI.rp.getCategories().getCategoryList()) {
            String label = c.getCategoryName();
            double value = c.getAmount();
            addHistogramColumn(label, value, BAR_COLOR);

        }
        layoutHistogram();
    }

    // MODIFIES: this
    // EFFECTS: reset category chart
    public void resetChart() {
        bars = new ArrayList<>();
        addCategoryBars();
    }


    // represent a bar with label, value, and the color of the bar
    private class Bar {
        private String label;
        private double value;
        private Color color;

        // MODIFIES: this
        // EFFECTS: initialize fields
        public Bar(String label, double value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public double getValue() {
            return value;
        }

        public Color getColor() {
            return color;
        }
    }

    // a class draws rectangles that could represent bars
    private class ColorIcon implements Icon {

        private Color color;
        private int width;
        private int height;

        // MODIFIES: this
        // EFFECTS: initialize fields
        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        // MODIFIES: g
        // EFFECT: draw rectangle with this.color and with size and location
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }
    }
}
