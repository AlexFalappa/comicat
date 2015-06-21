package af.uiswing;

import af.uiswing.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comicat Swing GUI entry point.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);
    public static MainFrame frame;

    /**
     * Main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log.info("Starting");
        // Set the Nimbus LAF
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // will go with default LAF
        }
        SwingUtilities.invokeLater(() -> {
            frame = new MainFrame();
            // show centered on screen
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            log.info("UI shown");
        });
    }

}
