import javax.swing.JFrame;

public class MyFrame extends JFrame{
    
    MyBoard gameBoard;

    MyFrame()
    {
        gameBoard = new MyBoard();
        
        this.setTitle("Brick Breaker");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.add(gameBoard);        
        this.pack();
    }
}