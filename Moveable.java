import javax.swing.*;
import javax.swing.JLabel;

public class Moveable extends JLabel{

    final int width, height;
    
    int location_X = 0, location_Y = 0;
    int velocity_X = 0, velocity_Y = 0;

    int move_interval;
    Icon image;

    Moveable(String fileName)
    {
        
        image = new ImageIcon(fileName);

        this.setIcon(image);

        width = image.getIconWidth();
        height = image.getIconHeight();

        this.setSize(width, height);
    }   
}