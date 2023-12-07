package swingfichas;

import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingInitial();
            }
        });

    }
}
