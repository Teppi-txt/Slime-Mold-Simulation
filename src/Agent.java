import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Agent {
    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();
    Vector2 slimeDimensions = new Vector2(3, 3);
    Angle direction = new Angle(0);
    BufferedImage map;

    int sensorDistance = 12;
    Vector2 bounds = new Vector2(0, 0);

    Vector2 leftSensorOffset = new Vector2(6, 6);
    Vector2 centerSensorOffset = new Vector2(6, 0);
    Vector2 rightSensorOffset = new Vector2(6, -6);

    float turningFactor = 25;

    //turning concentrations
    float strengthOne = 5;
    float strengthTwo = 25;
    float strengthThree = 50;

    float decisionThreshold = 30;

    public Agent(int maxX, int maxY, BufferedImage map) {
        this.map = map;
        this.bounds.x = maxX; this.bounds.y = maxY;
        position.x = (float)Math.random() * maxX;
        position.y = (float)Math.random() * maxY;
        direction.setAs((float)Math.random() * 360);
    }

    public void paint(Graphics g) {
        //sets the color of the agent
        g.setColor(Color.WHITE);

        //sensors
        sensorHandler(g);

        //sets the velocity used for movement calculation appropiately based on the direction
        velocity = direction.toVector();

        //movement logic
        position.x += velocity.x * 3; position.y -= velocity.y * 3;

        //redirectAt Bounds
        redirectAtBound((int)bounds.x, (int)bounds.y);

        //test calls
        //System.out.println(direction.angle);
        g.fillRect((int)(position.x - slimeDimensions.x/2), (int)(position.y - slimeDimensions.x/2), (int)slimeDimensions.x, (int)slimeDimensions.y);
    }

    public void sensorHandler(Graphics g) {
        //x value stores sum, y value stores how many data points
        Vector2 leftSensorSum = new Vector2(0, 0);
        Vector2 centerSensorSum = new Vector2(0, 0);
        Vector2 rightSensorSum = new Vector2(0, 0);

        Vector2 centerPixel = new Vector2(position.x + velocity.x * sensorDistance, position.y - velocity.y * sensorDistance).add(new Vector2(0, 0));
        Vector2 leftPixel = new Vector2(position.x + (direction.add(25).toVector()).x * sensorDistance, position.y - (direction.add(25).toVector()).y * sensorDistance);
        Vector2 rightPixel = new Vector2(position.x + (direction.add(-25).toVector()).x * sensorDistance, position.y - (direction.add(-25).toVector()).y * sensorDistance);

        for (int xoffset = -2; xoffset <= 2; xoffset++) {
            for (int yoffset = -2; yoffset <= 2; yoffset++) {
                if (leftPixel.add(new Vector2(xoffset, yoffset)).isInBounds(0, (int)bounds.x, 0, (int)bounds.y)) {
                    leftSensorSum.y++;
                    leftSensorSum.x += (map.getRGB((int)leftPixel.x + xoffset, (int)leftPixel.y + yoffset) >> 16) & 0xff;
                }
                if (centerPixel.add(new Vector2(xoffset, yoffset)).isInBounds(0, (int)bounds.x, 0, (int)bounds.y)) {
                    centerSensorSum.y++;
                    centerSensorSum.x += (map.getRGB((int)centerPixel.x + xoffset, (int)centerPixel.y + yoffset) >> 16) & 0xff;
                    //System.out.println((map.getRGB((int)centerPixel.x + xoffset, (int)centerPixel.y + yoffset) >> 16) & 0xff);
                }
                if (rightPixel.add(new Vector2(xoffset, yoffset)).isInBounds(0, (int)bounds.x, 0, (int)bounds.y)) {
                    rightSensorSum.y++;
                    rightSensorSum.x += (map.getRGB((int)rightPixel.x + xoffset, (int)rightPixel.y + yoffset) >> 16) & 0xff;
                }

//                g.setColor(Color.RED);
//                g.fillRect((int)centerPixel.x, (int)centerPixel.y, 2, 2);
//                g.setColor(Color.BLUE);
//                g.fillRect((int)leftPixel.x, (int)leftPixel.y, 2, 2);
//                g.setColor(Color.GREEN);
//                g.fillRect((int)rightPixel.x, (int)rightPixel.y, 2, 2);
//                g.setColor(Color.WHITE);
            }
        }
        moveAgentFromSensors(leftSensorSum, rightSensorSum, centerSensorSum);
    }

    private void moveAgentFromSensors(Vector2 leftSensorSum, Vector2 rightSensorSum, Vector2 centerSensorSum) {
        float leftAverage = leftSensorSum.x/leftSensorSum.y;
        float rightAverage = rightSensorSum.x/rightSensorSum.y;
        float centerAverage = centerSensorSum.x/centerSensorSum.y;
        int leftForce = (leftAverage >= strengthThree) ? 3 : (leftAverage >= strengthTwo) ? 2 : (leftAverage >= strengthOne) ? 1 : 0;
        int rightForce = (rightAverage >= strengthThree) ? 3 : (rightAverage >= strengthTwo) ? 2 : (rightAverage >= strengthOne) ? 1 : 0;
        int centerForce = (centerAverage >= strengthThree) ? 3 : (centerAverage >= strengthTwo) ? 2 : (centerAverage >= strengthOne) ? 1 : 0;

        if (leftAverage <= 15 && rightAverage <= 15);
        else if (leftAverage > rightAverage + decisionThreshold && rightAverage <= 150) direction = direction.add(turningFactor);
        else if (rightAverage > leftAverage + decisionThreshold && leftAverage <= 150) direction = direction.add(-turningFactor);
        else if (Math.abs(rightForce - leftForce) < decisionThreshold) direction = direction.add(Math.signum((float)Math.random() - 0.5F) * turningFactor);

//        if (leftForce <= 1 && rightForce <= 1);
//        else if (leftForce >= centerForce && centerForce > rightForce) direction = direction.add(-turningFactor);
//        else if (rightForce >= centerForce && centerForce > leftForce) direction = direction.add(turningFactor);
//        else if (rightForce == leftForce) direction = direction.add(Math.signum((float)Math.random() - 0.5F) * turningFactor);
    }

    public void redirectAtBound(int boundX, int boundY) {
        if (position.x > boundX - 50) {
            //sets the direction to a random direction facing to the left
            direction.setAs(90 + (float)Math.random() * 180);
        } else if (position.x < 0 + 50) {
            //sets the direction to a random direction facing to the left
            direction.setAs(270 + (float)Math.random() * 180);
        }
        if (position.y > boundY - 50) {
            //sets the direction to a random direction facing to the left
            direction.setAs((float)Math.random() * 180);
        } else if (position.y < 0 + 50) {
            //sets the direction to a random direction facing to the left
            direction.setAs(180 + (float)Math.random() * 180);
        }
    }
}
