import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MyBoard extends JPanel implements KeyListener, ActionListener{
    
    final int PANEL_WIDTH = 800, PANEL_HEIGHT = 600;
    final int BALL_UP_BORDER, BALL_LEFT_BORDER, BALL_RIGHT_BORDER;
    final int BALL_DOWN_BORDER_LOSE, BALL_DOWN_BORDER_BOUNCE;
    final int PLAYER_LEFT_BORDER, PLAYER_RIGHT_BORDER;
    final int ENEMIES_X1, ENEMIES_X2, ENEMIES_Y1, ENEMIES_Y2;
    final int TILE_WIDTH, TILE_HEIGHT;
    
    boolean game_started = false;
    boolean game_paused = false;

    Moveable player, ball;
    ArrayList<ArrayList<Moveable>> tiles;
    int total_tiles = 0;
    Timer timer;

    ArrayList<Life> lives;
    int lives_count = 3;

    JLabel score_field;
    int score = 0;

    MyBoard()
    {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);

        InitializeBoard();

        ENEMIES_X1 = tiles.get(0).get(0).location_X;
        ENEMIES_X2 = ENEMIES_X1 + tiles.get(0).size()*tiles.get(0).get(0).width;
        ENEMIES_Y1 = tiles.get(0).get(0).location_Y;
        ENEMIES_Y2 = ENEMIES_Y1 + tiles.size()*tiles.get(0).get(0).height;
        
        PLAYER_RIGHT_BORDER = PANEL_WIDTH - player.width;
        PLAYER_LEFT_BORDER = BALL_UP_BORDER = BALL_LEFT_BORDER = 0;
        BALL_DOWN_BORDER_LOSE = PANEL_HEIGHT - ball.height;
        BALL_RIGHT_BORDER = PANEL_WIDTH - ball.width;
        BALL_DOWN_BORDER_BOUNCE = BALL_DOWN_BORDER_LOSE - player.height;
        
        TILE_WIDTH = tiles.get(0).get(0).width;
        TILE_HEIGHT = tiles.get(0).get(0).height;

        timer = new Timer(10, this);
        timer.start();      
    }

    private void InitializeBoard()
    {
        InitializeScoreLives();
        InitializePlayer();
        InitializeBall();
        InitializeEnemies();
    }

    private void update_score_lives()
    {
        game_started = false;
        score_field.setText("SCORE : " + score);
        this.remove(lives.get(lives_count));
        lives.remove(lives_count);
    }
    
    private void game_replay()
    {
        update_score_lives();
        this.remove(player);
        this.remove(ball);
        InitializePlayer();
        InitializeBall();
        this.revalidate();
        this.repaint();
        return;
    }

    private void game_end()
    {
        update_score_lives();
        this.revalidate();
        this.repaint();

        Object[] options1 = {"Try Again", "Close"};
        
        JOptionPane jop = new JOptionPane("GAME OVER\nSCORE :  " + score, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options1, options1[0]);
        JDialog dialog = jop.createDialog(null, "Nice Try");
        
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // In real code, you should invoke this from AWT-EventQueue using invokeAndWait() or something
        dialog.setVisible(true);
        
        
        String answer = (String) jop.getValue();
        switch (answer) {
            case "Try Again":
                score = 0;
                lives_count = 3;
                this.removeAll();
                InitializeBoard();
                this.revalidate();
                this.repaint();
                break;
        
            case "Close":
                this.setVisible(false); 
                System. exit(0);
                
        }
    }

    private void game_win()
    {
        game_started = false;
        score = 0;
        lives_count = 3;
        this.removeAll();
        InitializeBoard();
        this.revalidate();
        this.repaint();
        return;
    }

    private void InitializeScoreLives()
    {
        lives = new ArrayList<Life>();

        for (int i = 1; i < lives_count + 1; i ++)
        {
            Life life = new Life("heart.png");
            lives.add(life);
            life.setLocation(PANEL_WIDTH - i*life.width, 0);
            this.add(life);
        }

        score_field = new JLabel("SCORE : " + score);  
        score_field.setBounds(0,0, 100, 50);
        score_field.setForeground(Color.WHITE);
        this.add(score_field);
    }

    private void InitializePlayer()
    {
        player = new Moveable("player.png");
        player.location_X = PANEL_WIDTH/2 - player.width/2;
        player.location_Y = PANEL_HEIGHT - player.height;
        player.setLocation(player.location_X, player.location_Y);
        player.move_interval = 5;
        player.velocity_X = 5;
        this.add(player);
    }

    private void InitializeBall()
    {
        ball = new Moveable("ball.png");
        ball.location_X = player.location_X + player.width/2 - ball.width/2;
        ball.location_Y = player.location_Y  - ball.width;
        ball.setLocation(ball.location_X, ball.location_Y);
        ball.move_interval = 5;
        ball.velocity_X = 0;
        ball.velocity_Y = -1;
        this.add(ball);
    }
    private void InitializeEnemies()
    {
        int start_position_x = 100;
        int start_position_y = 100;
        int rows = 5;
        int cols = 8;
        total_tiles = rows*cols;
        Moveable tile = new Moveable("tile.png");
        ArrayList<Moveable> tmp;

        tiles = new ArrayList<ArrayList<Moveable>>();
        
        for (int i = 0; i < rows; i ++)
        {
            tmp = new ArrayList<Moveable>();
            for (int j = 0; j < cols; j ++)
            {
                tile = new Moveable("tile.png");
                tile.location_X = start_position_x + j*tile.width;
                tile.location_Y = start_position_y + i*tile.height;
                tile.setLocation(tile.location_X, tile.location_Y);
                tmp.add(tile);
                this.add(tile);
            }
            tiles.add(tmp);
        }     
    }

    public void move_BALL()
    {
        int prev_ball_x = ball.location_X;
        ball.location_X += ball.velocity_X*ball.move_interval;
        ball.location_Y += ball.velocity_Y*ball.move_interval;

        if ((ball.location_X + ball.width>= ENEMIES_X1) && (ball.location_X <= ENEMIES_X2 + TILE_WIDTH))
        {
            if ((ball.location_Y + ball.width >= ENEMIES_Y1) && (ball.location_Y <= ENEMIES_Y2 + TILE_HEIGHT))
            {
                for (int row = 0; row < tiles.size(); row ++)
                {
                    for(Moveable tile : tiles.get(row))
                    {
                        if((ball.location_X + ball.width  >= tile.location_X) && (ball.location_X <= tile.location_X + tile.width))
                        {
                            if ((ball.location_Y + ball.width >= tile.location_Y) && (ball.location_Y <= tile.location_Y + tile.height))
                            {
                                if ((prev_ball_x  + ball.width < tile.location_X) || (prev_ball_x > tile.location_X + tile.width))
                                {                                    
                                    ball.velocity_X *= -1;
                                    ball.location_X += ball.velocity_X * ball.move_interval;
                                }                              
                                else
                                {
                                    ball.velocity_Y *= -1;
                                    ball.location_Y += ball.velocity_Y * ball.move_interval;
                                }
                                tiles.get(row).remove(tile);
                                this.remove(tile);
                                total_tiles -= 1;
                                
                                ball.setLocation(ball.location_X, ball.location_Y);
                                score += 100;
                                score_field.setText("SCORE : " + score);
                                this.revalidate();
                                this.repaint();
                                if (total_tiles == 0)
                                    game_win();
                                return;
                            }
                        }
                    }
                }
                   
            }
        }
        if (ball.location_Y < BALL_UP_BORDER)
        {
            ball.velocity_Y *= -1;
            ball.location_Y += ball.velocity_Y * ball.move_interval;
        }
        else if (ball.location_Y > BALL_DOWN_BORDER_LOSE)
        {
            lives_count -= 1;
            score -= 100;

            if (lives_count == 0)
            {
                game_end();
            }
            else
                game_replay();
            
        }
        else if (ball.location_Y > BALL_DOWN_BORDER_BOUNCE)
        {
            if ((ball.location_X + ball.width >= player.location_X) && (ball.location_X  <= player.location_X + player.width))
            {
                if (ball.location_X <= player.location_X + player.width/3) 
                {
                    ball.velocity_X = -1;
                    ball.location_X += ball.velocity_X * ball.move_interval;
                    ball.velocity_Y *= -1;
                    ball.location_Y += ball.velocity_Y * ball.move_interval;
                }
                else if (ball.location_X  + ball.width >= player.location_X + player.width - player.width/3) 
                {
                    ball.velocity_X = 1;
                    ball.location_X += ball.velocity_X * ball.move_interval;
                    ball.velocity_Y *= -1;
                    ball.location_Y += ball.velocity_Y * ball.move_interval;
                }
                else
                {
                    ball.velocity_X = 0;
                    ball.velocity_Y *= -1;
                    ball.location_Y += ball.velocity_Y * ball.move_interval;
                }
            }

        }
        else if ((ball.location_X < BALL_LEFT_BORDER) || (ball.location_X > BALL_RIGHT_BORDER))
        {
            ball.velocity_X *= -1;
            ball.location_X += ball.velocity_X * ball.move_interval;
        }
        
        ball.setLocation(ball.location_X, ball.location_Y);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        
        if (game_started == false)
            return;
        move_BALL();
        
    }

    @Override
    public void keyTyped(KeyEvent e) {

        int change = player.move_interval*player.velocity_X;
        int current_player_location = player.location_X;
        
        switch (e.getKeyChar()) {
            case 'a':
                if (current_player_location - change >= PLAYER_LEFT_BORDER)
                {
                    player.location_X -= change;
                    player.setLocation(player.location_X, player.location_Y);  
                }
                else
                {
                    change = current_player_location - PLAYER_LEFT_BORDER;
                    if (change == 0)
                        return;
                    player.location_X = PLAYER_LEFT_BORDER;
                    player.setLocation(player.location_X, player.location_Y);
                }
                change *= -1;   
                break;
            case 'd':
                if (current_player_location + change <= PLAYER_RIGHT_BORDER)
                {
                    player.location_X += change;
                    player.setLocation(player.location_X, player.location_Y);      
                }
                else
                {
                    change = PLAYER_RIGHT_BORDER - current_player_location;
                    if (change == 0)
                        return;
                    player.location_X = PLAYER_RIGHT_BORDER;
                    player.setLocation(player.location_X, player.location_Y);
                }
                
                break;
            case ' ':
                game_started = true;
                break;
        
        }
        if (game_started == false)
        {
            ball.location_X += change;
            ball.setLocation(ball.location_X, ball.location_Y);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        return;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        return; 
    }
}