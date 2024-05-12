package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

// represent an expense list that user entered or loaded from file:
// includes add/delete button with its action classes, and a scrollable expense table
public class ExpenseTable extends JPanel {

    private JFrame parentFrame;
    private ExpenseCalculatorGUI expGUI;
    private static final String[] HEADINGS = {"Year", "Month", "Day", "Amount($)", "Category", "Description"};
    private JTable expTable;
    private JScrollPane scrollable;
    private DefaultTableModel model;
    private int selectedRow;

    // MODIFIES: this
    // EFFECTS: initializes fields, formats current panel, and add buttons and table
    public ExpenseTable(ExpenseCalculatorGUI parent) {
        this.expGUI = parent;
        parentFrame = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addButtons();
        setTableModel();
        addTable();
        setBorder(BorderFactory.createTitledBorder("Expenses"));
    }

    // MODIFIES: this
    // EFFECTS: sets table model according to the length of headings and make each cell not editable,
   //  setting up a table is learned from online tutorial:
    // https://stackoverflow.com/questions/2316016/how-to-instantiate-an-empty-jtable/8515753
    private void setTableModel() {

        // set table cells to be not editable, learned from online tutorial:
        // https://newbedev.com/how-to-make-a-jtable-not-editable-in-java
        model = new DefaultTableModel(0, HEADINGS.length) {
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        model.setColumnIdentifiers(HEADINGS);
    }

    // MODIFIES: this
    //EFFECTS: add "Add", "Delete" button on top of this panel with their action classes
    private void addButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2));
        JButton add = new JButton(new AddExpense());
        add.setBorder(BorderFactory.createLineBorder(Color.red)); // make it more obvious
        JButton delete = new JButton(new DeleteExpense());
        buttons.add(add);
        buttons.add(delete);
        add(buttons);

    }

    // MODIFIES: this
    // EFFECT: add expense table to parent panel, set up mouse listener to the table, put table to the scrollable
    private void addTable() {
        expTable = new JTable(model);

        // adding mouse listener is learned from, so that a row can be selection on click
        //https://coderanch.com/t/343164/java/jTable-selectedRowIndex-mouse-click
        expTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                selectedRow = expTable.rowAtPoint(evt.getPoint());
                // set row to be selected
                expTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
            }
        });
        // set up rows selected to be red
        expTable.setSelectionBackground(Color.RED);

        // put table into scrollable
        scrollable = new JScrollPane(expTable);
        scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollable);
    }


    // a class represent the action after "Add Expense" button is clicked
    private class AddExpense extends AbstractAction {

        // set button display
        public AddExpense() {
            super("Add Expense");
        }

        // EFFECT: once button is click, a AddExpPopupWindow occur
        @Override
        public void actionPerformed(ActionEvent e) {
            AddExpPopupWindow dialog = new AddExpPopupWindow(parentFrame, false, expGUI, model);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);
            dialog.pack();
        }
    }

    // MODIFIES: model, expTable
    // EFFECTS: reset table to default empty table
    public void resetTable() {
        model.setRowCount(0);
        expTable = new JTable(model);
    }

    //EFFECTS: returns model of current table
    public DefaultTableModel getModel() {
        return this.model;
    }


    // action after clicking "DELETE selected", how to delete a row from a table is learned from:
    //https://www.tutorialspoint.com/how-can-we-remove-a-selected-row-from-a-jtable-in-java
    private class DeleteExpense extends AbstractAction {

        public DeleteExpense() {
            super("Delete Selected");
        }

        // MODIFIES: model, expGUI.rp
        // EFFECTS: deletes selected row from table, deletes one expense from expGUI.rp,
        //          shows a pop-up window that indicates successful action
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRowsIndex = expTable.getSelectedRow();
            if (selectedRowsIndex > -1) {
                expGUI.rp.deleteOneEntry(expGUI.rp.getExpenseFromIndex(selectedRowsIndex));
                model.removeRow(selectedRowsIndex);
                JOptionPane.showMessageDialog(null, "Selected row deleted successfully");
            }
        }

    }


}
