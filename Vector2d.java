import java.util.Random;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public boolean equals (Object other){
        if(this == other) return  true;
        if(!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return this.x == that.x && this.y == that.y;
    }

    public Vector2d add (Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }
    public Vector2d substract (Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }
    public Vector2d oposite (){
        return new Vector2d((-1*this.x), (-1*this.y));
    }

    public boolean preceeds(Vector2d other){
        return this.x <= other.x && this.y <= other.y;
    }
    public boolean follows(Vector2d other){
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public static Vector2d random(Vector2d lowerBoundary, Vector2d upperBoundary){
        int x = lowerBoundary.getX() + new Random().nextInt((upperBoundary.getX()-lowerBoundary.getY())+1);
        int y = lowerBoundary.getY() + new Random().nextInt((upperBoundary.getY()-lowerBoundary.getY())+1);
        return new Vector2d(x,y);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public String toString(){
        return String.format("(%d,%d)", x, y);
    }

}
