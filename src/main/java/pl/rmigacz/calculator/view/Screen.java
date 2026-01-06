package pl.rmigacz.calculator.view;

import java.awt.*;

/**
 * Top element of main frame. <code>Screen</code> has 3x3 GridBagLayout.
 * The first row contains <code>mainTextField</code> which shows calculations
 * result, second row has <code>mainLabel</code> which presents the last
 * calculated value and the third pop-up menu of numeral system choices.
 */
class Screen extends Panel {
    private TextField mainTextField;

    Screen() {
        initScreen();
    }

    private void initScreen() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        setLayout(new GridBagLayout());

        mainTextField = new TextField();

        mainTextField.setColumns(50);
        mainTextField.setFocusable(false);
        mainTextField.setEditable(false);
        mainTextField.setFont(new Font("SansSerif", Font.BOLD, 22));

        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1; // take whole Screen area
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        add(mainTextField, gridBagConstraints);
    }

    public void setDisplay(String s) {
        mainTextField.setText(s);
    }
}

