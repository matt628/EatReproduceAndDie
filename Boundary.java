public class Boundary {//KEEPS THE BOUNDARY OF THE A GRASS FIELD.
    Vector2d upperRight;
    Vector2d jungleLowerLeft;
    Vector2d jungleUpperRight;

    public Boundary(Vector2d upperRight, Vector2d jungleLowerLeft, Vector2d jungleUpperRight){
        this.upperRight = upperRight;
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = jungleUpperRight;
    }
    public Vector2d randomPositionJungle(){
        return Vector2d.random(jungleLowerLeft, jungleUpperRight);
    }
    public Vector2d randomPositionSavanna(){

        return Vector2d.random(new Vector2d(0,0), upperRight); //todo
    }
    public Vector2d randomPosition(){
        return Vector2d.random(new Vector2d(0,0), upperRight);
    }
    public Vector2d keepInsideBoundaries(Vector2d position){
        if(isInBoundary(position)) return position;
        Vector2d tmp = null;
        if(position.getX()<0) tmp = new Vector2d(upperRight.getX(), position.getY());
        if(position.getX()> upperRight.getX()) tmp = new Vector2d(0,position.getY());
        if(position.getY()<0) tmp = new Vector2d(position.getX(), upperRight.getY());
        if(position.getY()> upperRight.getY()) tmp = new Vector2d(position.getX(), 0);
        return tmp;
    }
    public boolean isInBoundary(Vector2d position){
        return  0 <= position.getX() && position.getX() <= upperRight.getX() && 0 <= position.getY() && position.getY() <= upperRight.getY();
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }
}
