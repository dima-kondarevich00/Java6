import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
@SuppressWarnings("serial")
public class Field extends JPanel {
    private Integer[][] matrix3D;

    public int getConstuctorTimer() {
        return constuctorTimer;
    }

    public void setConstuctorTimer(int constuctorTimer) {
        this.constuctorTimer = constuctorTimer;
    }

    private int constuctorTimer = 0;
    public Integer[][] getMatrix3D() {
        return matrix3D;
    }
    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });
    public Field() {
        setBackground(Color.WHITE);
        constuctorTimer = 0;
        repaintTimer.start();
        matrix3D = new Integer[14][10] ;
        //x scale - 49
        //y scale - 44
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 10; j++){
                matrix3D[i][j] = 0;
            }
        }
        matrix3D[5][5] = 1;
        matrix3D[8][8] = 2;
        matrix3D[3][1] = 3;
        matrix3D[8][4] = 4;
    }

    public void setDurabilityMatrix(Integer[][] Matrix) {
        matrix3D = Matrix;
    }

    public ArrayList<BouncingBall> getBalls() {
        return balls;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }


        canvas.setFont(new Font("TimesRoman", Font.BOLD, 14));
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 10; j++){
                if(matrix3D[i][j].equals(1)){
                    canvas.setColor(Color.BLUE);
                    canvas.drawRect(i * 49, j * 44, 49, 44);
                    canvas.drawString("Constructor", (int) (i * 49 + 24.5), j * 44 + 22);
                }
                if(matrix3D[i][j].equals(2)){
                    canvas.setColor(Color.RED);
                    canvas.drawRect(i * 49, j * 44, 49, 44);
                    canvas.drawString("Destructor", (int) (i * 49 + 24.5), j * 44 + 22);
                }
                if(matrix3D[i][j].equals(3)){
                    canvas.setColor(Color.ORANGE);
                    canvas.drawRect(i * 49, j * 44, 49, 44);
                    canvas.drawString("INPUT", (int) (i * 49 + 24.5), j * 44 + 22);
                }
                if(matrix3D[i][j].equals(4)){
                    canvas.setColor(Color.BLACK);
                    canvas.drawRect(i * 49, j * 44, 49, 44);
                    //canvas.drawOval(i * 49, j * 44, 49, 44);
                    canvas.drawString("OUTPUT", (int) (i * 49 + 24.5), j * 44 + 22);
                }
            }
        }
    }
    public void addBall() {
        balls.add(new BouncingBall(this));
    }

    public void addBall(BouncingBall ball) {
        balls.add(new BouncingBall(this, ball.getRadius(), ball.getColor(), ball.getX(),
                ball.getY(), ball.getSpeed(), ball.getSpeedX(), ball.getSpeedY()));
    }
    public void DeliteBall(BouncingBall ball){
        for(int i = 0; i < balls.size(); i++){
            if(balls.get(i).getId() == ball.getId()){
                balls.remove(balls.get(i));
            }
        }
    }
    public synchronized void pause() {
        paused = true;
    }
    public synchronized void resume() {
        paused = false;
        notifyAll();
    }
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
            wait();
        }
    }
}

