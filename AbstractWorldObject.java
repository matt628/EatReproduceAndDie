public class AbstractWorldObject {
    protected Vector2d position;
    public AbstractWorldObject(Vector2d position) {
        this.position = position;
    }
    public Vector2d getPosition(){ return position;}

}
