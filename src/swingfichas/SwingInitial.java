package swingfichas;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SwingInitial implements ActionListener {
    // Tablero principal del menu
    private JFrame mainWindowMenu;
    private JPanel mainPanelMenu;
    private JButton createGameButton;
    private JButton playGameButton;
    private JButton exitButton;
    private JButton[] mainMenuButtonsArr;
    private JLabel titleGame;
    private ImageIcon menuIcon;
    private Image imageMenuScaled;
    private ImageIcon iconMenuScaled;
    private JLabel imageMenu;

    // Tablero de creacion de juego
    private JFrame frameGameCreation;

    SwingInitial() {
        initializeComponents();
        addButtonsMainMenu2Arr();
        addComponents2Panel();
        addEventListeners2Components();
        addComponents2Frame();
    }

    private void initializeComponents() {
        mainWindowMenu = new JFrame("Swing Fichas Iguales");
        mainPanelMenu = new JPanel();
        mainPanelMenu.setLayout(new BoxLayout(mainPanelMenu, BoxLayout.Y_AXIS));
        mainPanelMenu.setAlignmentX(0.5f);

        createGameButton = new JButton("CREATE GAME");
        playGameButton = new JButton("PLAY GAME");
        exitButton = new JButton("EXIT");

        titleGame = new JLabel("FICHAS IGUALES");
        titleGame.setAlignmentX(0.5f);

        menuIcon = new ImageIcon("etc/images/fichasroja,verde,azul.png");

        imageMenuScaled = menuIcon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        iconMenuScaled = new ImageIcon(imageMenuScaled);
        imageMenu = new JLabel(iconMenuScaled);
        imageMenu.setAlignmentX(0.5f);

    }

    private void addButtonsMainMenu2Arr() {
        mainMenuButtonsArr = new JButton[3];
        mainMenuButtonsArr[0] = createGameButton;
        mainMenuButtonsArr[1] = playGameButton;
        mainMenuButtonsArr[2] = exitButton;

    }

    private void addEventListeners2Components() {

    }

    private void addComponents2Panel() {
        mainPanelMenu.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanelMenu.add(titleGame);
        mainPanelMenu.add(Box.createVerticalGlue());
        mainPanelMenu.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanelMenu.add(imageMenu);
        addButtonToPanel();

        mainPanelMenu.add(Box.createVerticalGlue());
    }

    private void addButtonToPanel() {

        for (JButton butElem : mainMenuButtonsArr) {
            butElem.setAlignmentX(0.5f);
            butElem.addActionListener(this);
            mainPanelMenu.add(butElem);

        }

    }

    private void addComponents2Frame() {

        mainWindowMenu.add(mainPanelMenu);
    }

    protected void show() {
        mainWindowMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindowMenu.setSize(500, 500);
        mainWindowMenu.setResizable(false);
        mainWindowMenu.setLocationRelativeTo(null);
        mainWindowMenu.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionActivated) {
        if (actionActivated.getSource() == exitButton) {
            int reponseExit = JOptionPane.showConfirmDialog(null, "Do you want to exit the game?", "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (reponseExit == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Thanks for playing!");

                System.exit(0);
            }
        }

        if (actionActivated.getSource() == createGameButton) {
            frameGameCreation = new JFrame("Create Game");
            // Configura y añade los componentes al frameGameCreation según tus necesidades
            frameGameCreation.setSize(500, 500);
            frameGameCreation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameGameCreation.setLocationRelativeTo(null);
            frameGameCreation.setVisible(true);
            mainWindowMenu.dispose();

        }

        if (actionActivated.getSource() == playGameButton) {
            mainWindowMenu.setVisible(false);
        }

    }

}
