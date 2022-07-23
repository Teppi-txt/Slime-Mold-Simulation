public class Angle {
    float angle;

    public Angle(float angle) {
        this.angle = angle;
    }

    public void setAs(float newAngle) {
        this.angle = newAngle;
    }

    public Vector2 toVector(){
        return new Vector2((float)Math.cos(Math.toRadians(angle)), (float)Math.sin(Math.toRadians(angle)));
    }

    public Angle add(float i) {
        return new Angle(angle + i);
    }
}
