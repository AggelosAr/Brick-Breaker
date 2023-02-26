import javax.swing.*;
import javax.swing.JLabel;

public class Life extends JLabel{

    final int width, height;
    Icon lives_field;
    

    Life(String fileName)
    {
        lives_field = new ImageIcon(fileName);
        
        this.setIcon(lives_field);

        width = lives_field.getIconWidth();
        height = lives_field.getIconHeight();

        this.setSize(width, height);
    }
}