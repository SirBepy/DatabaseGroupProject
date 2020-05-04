import javax.swing.*;
import javax.swing.table.*;

public class TextTableRenderer extends JTextArea implements TableCellRenderer {
    private static final long serialVersionUID = 1L;

    public TextTableRenderer() {
        setOpaque(true);
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    public JTextArea getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        setText((value == null) ? "" : value.toString());
        return this;
    }
}