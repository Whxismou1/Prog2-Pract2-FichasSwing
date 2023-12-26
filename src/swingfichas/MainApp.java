/***
 * Clase Main enncargada de ejecutar el programa
 */

package swingfichas;

import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingInitial().show();
            }

        });

    }
}
