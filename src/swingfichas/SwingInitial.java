package swingfichas;

import java.awt.LayoutManager;

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
        addEventListeners2Components();
        addComponents2Panel();
        addComponents2Frame();
    }

    private void initializeCcomponents() {
        mainWindowMenu = new JFrame("Swing Fichas Iguales");
        mainPanelMenu = new JPanel();
        mainPanelMenu.setAlignmentX(0.5f);

        //como centro el panel en el frame?
        //mainPanelMenu.setAlignmentX(0.5f);

    }

    private void addEventListeners2Components() {

    }

    private void addComponents2Panel() {
        addButtonToPanel("CREATE GAME");
        addButtonToPanel("PLAY GAME");
        addButtonToPanel("EXIT");
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
        mainWindowMenu.setSize(500, 500);
        mainWindowMenu.setLocationRelativeTo(null);
        mainWindowMenu.setVisible(true);
    }

}
