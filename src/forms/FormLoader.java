package forms;

import javax.swing.*;

/**
 * Created by henrique.quintino on 7/12/2017.
 */
public class FormLoader {
    private JLabel lblUnicorn;
    private JPanel panel1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("FormLoader");
        frame.setContentPane(new FormLoader().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
