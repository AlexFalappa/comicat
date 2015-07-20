/*
 * Unlicensed
 */
package af.uiswing.ui.widgets;

import af.uiswing.data.RoledAuthor;
import java.awt.Component;
import static java.lang.String.valueOf;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class RoledAuthorListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof RoledAuthor) {
            RoledAuthor ra = (RoledAuthor) value;
            setText(String.format("%s (%s)", ra.getAuthor().getNameSurname(), ra.getRole().toString()));
        } else {
            setText(valueOf(value));
        }
        return this;
    }

}
