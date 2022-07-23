public class Vector2 {
    public float x;
    public float y;

    public Vector2() {
        this.x = 0.0F;
        this.y = 0.0F;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Vector2 other) {
        return (this.x == other.x && this.y == other.y);
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public double distanceTo(Vector2 other) {
        float v0 = this.x - other.x;
        float v1 = this.y - other.y;
        return Math.sqrt(v0*v0 + v1*v1);
    }

    public static String toString(Vector2 vec) {
        return ("(" + String.valueOf(vec.x) + ", " + String.valueOf(vec.y) + ")");
    }


    public static Vector2 invert(Vector2 vec) {
        return new Vector2(-(vec.x), -(vec.y));
    }

    public Vector2 scale(float factor) {
        return new Vector2(this.x * factor, this.y * factor);
    }

    public Vector2 normalize() {
        double length = Math.sqrt(this.x*this.x + this.y*this.y);

        if (length != 0.0) {
            return new Vector2(this.x / (float)length, this.y / (float)length);
        }
        return this;
    }

    public Vector2 toOne() {
        return new Vector2(Math.signum(x), Math.signum(y));
    }

    public float getAngle() {
        double cosx = this.normalize().x;
        double sinx = this.normalize().y;
        double sinAsAngle = Math.toDegrees(Math.asin(sinx));
        System.out.println(sinx);
        System.out.println(cosx);
        if (sinx >= 0 && cosx >= 0) return (float)sinAsAngle;
        else if (sinx >= 0 && cosx <= 0) return (float)(180 - sinAsAngle);
        else if (sinx <= 0 && cosx >= 0) return (float)(360 - sinAsAngle);
        else if (sinx <= 0 && cosx <= 0) return (float)(270 + sinAsAngle);
        else return 0;
    }

    public boolean isInBounds(int lowX, int highX, int lowY, int highY) {
        return (x >= lowX && x < highX && y >= lowY && y < highY);
    }

}
