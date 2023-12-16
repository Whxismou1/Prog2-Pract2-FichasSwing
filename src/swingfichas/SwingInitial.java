package swingfichas;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingInitial {
    // Tablero principal del menu
    private JFrame mainWindowMenu;
    private JPanel mainPanelMenu;
    // Tablero de creacion de juego
    private JFrame frameGameCreation;

    SwingInitial() {

        initializeCcomponents();
        // addEventListeners2Components();
        // addComponents2Panel();
        // addComponents2Frame();
    }

    private void initializeCcomponents() {
        mainWindowMenu = new JFrame("Swing Fichas Iguales");
        mainPanelMenu = new JPanel();
        mainPanelMenu.setLayout(new BoxLayout(mainPanelMenu, BoxLayout.Y_AXIS));

    }

    private void addEventListeners2Components() {

    }

    private void addComponents2Panel() {
        addButtonToPanel("Crear juego");
        addButtonToPanel("Exportar juego");
        addButtonToPanel("Guardar");
    }

    private void addButtonToPanel(String buttonText) {
        JButton button = new JButton(buttonText);

        mainPanelMenu.add(button);
    }

    private void addComponents2Frame() {
        mainWindowMenu.add(mainPanelMenu);
    }

    protected void show() {
        mainWindowMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindowMenu.setSize(1000, 1000);
        mainWindowMenu.setLocationRelativeTo(null);
        mainWindowMenu.setVisible(true);
    }

}
