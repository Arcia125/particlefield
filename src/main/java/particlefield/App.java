package particlefield;

import java.awt.EventQueue;
import javax.swing.JFrame;

public final class App extends JFrame {
    private static final long serialVersionUID = -1801753581653136123L;

    public App() {
        initUI();
    }

    private void initUI() {
        // Dimension maximumSize = new Dimension(2000, 1000);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(new ParticleField());
        pack();
        setTitle("Particle Field");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}
