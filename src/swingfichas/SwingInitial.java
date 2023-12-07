package swingfichas;

import javax.swing.JFrame;

public class SwingInitial {

    SwingInitial() {

        iniciarJframe();

    }

    private void iniciarJframe() {
        JFrame aaaa = new JFrame("GameFichas");
        aaaa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aaaa.setLocationRelativeTo(null);
        aaaa.setSize(400, 400);
        aaaa.setVisible(true);

    }

}
