import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.stream.StreamSupport;

//import static jdk.jfr.internal.consumer.EventLog.stop;

public class BouncingBall implements Runnable {

    static int ID;
    private int id;
    private static final int MAX_RADIUS = 20;
    private static final int MIN_RADIUS = 10;
    private static final int MAX_SPEED = 2;
    private Field field;
    private int radius;
    private Color color;
    private double x;
    private double y;
    private int speed;
    private double speedX;
    private double speedY;
    public BouncingBall(Field field) {
        id = ID++;
        this.field = field;
        radius = new Double(Math.random()*(MAX_RADIUS -
                MIN_RADIUS)).intValue() + MIN_RADIUS;
        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        double angle = Math.random() * 2 * Math.PI;
        speedX = 3*Math.cos(angle);
        speedY = 3*Math.sin(angle);
        color = new Color((float)Math.random(), (float)Math.random(),
                (float)Math.random());
        x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    public int getId() {
        return id;
    }

    public BouncingBall(Field field, int radius, Color color, double x, double y, int speed, double speedX, double speedY) {
        id = ID++;
        this.field = field;
        this.radius = radius;
        this.color = color;
        this.x = x;
        this.y = y;
        this.speed = speed;
        double angle = Math.random() * 2 * Math.PI;
        this.speedX = 3*Math.cos(angle);
        this.speedY = 3*Math.sin(angle);
        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    public Field getField() {
        return field;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void run() {
        try {
            while(true) {
                field.canMove(this);
                if (x + speedX <= radius) {
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                    x=new Double(field.getWidth()-radius).intValue();
                } else
                if (y + speedY <= radius) {
                    speedY = -speedY;
                    y = radius;
                } else
                if (y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                    y = new Double(field.getHeight()-radius).intValue();
                }
                else {
                    Integer[][] matrix3D = field.getMatrix3D();
                    for(int i = 0; i < 14; i++){
                        Boolean x_Condition = (Math.abs((x + speedX - 49 * i)) >= 0.5 * radius && Math.abs((x + speedX - 49 * (i + 1))) <= 0.5 * radius) ||
                                (Math.abs((x + speedX - 49 * (i + 1))) >= radius && Math.abs((x + speedX - 49 * (i))) <= radius);

                        for(int j = 0; j < 10; j++) {
                            Boolean y_Condition = (Math.abs((y + speedY - 44 * j)) >= 0.5 * radius && Math.abs((y + speedY - 44 * (j + 1))) <= 0.5 * radius) ||
                                    (Math.abs((y + speedY - 44 * (j + 1))) >= 0.5 * radius && Math.abs((y + speedY - 44 * (j))) <= 0.5 * radius);
                            if (matrix3D[i][j] == 1 && field.getConstuctorTimer() == 0) {
                                if(x_Condition && y_Condition) {
                                    if(field.getConstuctorTimer() == 0) {
                                        field.addBall(this);
                                        field.setConstuctorTimer(100 * field.getBalls().size());
                                    }
                                }
                            }
                            if(matrix3D[i][j] == 2){
                                if(x_Condition && y_Condition){
                                    field.DeliteBall(this);
                                    speedX = 0;
                                    speedY = 0;
                                    //stop();
                                }
                            }
                            if(matrix3D[i][j] == 3){
                                if(x_Condition && y_Condition){
                                    for(int k = 0; k < 14; k++){
                                        for(int h = 0; h < 10; h++){
                                            if(matrix3D[k][h] == 4){
                                                x = k * 49;
                                                y = h * 44;
                                            }
                                        }
                                    }
                                }
                            }
//

                        }
                    }
                }
                if(field.getConstuctorTimer() > 0){
                    field.setConstuctorTimer(field.getConstuctorTimer() - 1);
                }
                System.out.println(field.getConstuctorTimer());

                x += speedX;
                y += speedY;
                Thread.sleep(16-speed);
            }

        } catch (InterruptedException ex) {
        }
    }
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius,
                2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}
