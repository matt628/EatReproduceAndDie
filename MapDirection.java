import java.util.List;
import java.util.Random;

public enum MapDirection {
    NORTH,
    NE,
    EAST,
    SE,
    SOUTH,
    SW,
    WEST,
    NW;

    public String toString(){
        switch (this){
            case EAST:
                return "Wschód";
            case WEST:
                return "Zachód";
            case NORTH:
                return  "Północ";
            case SOUTH:
                return  "Południe";
            case NE: return "NE";
            case NW: return "NW";
            case SW: return "SW";
            case SE: return  "SE";
            default:
                return "błędny kierunek";
        }
    }

    private static final List<MapDirection> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static MapDirection randomDirection()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    public static MapDirection rotate(MapDirection from, MapDirection rotation){
        return MapDirection.values()[(from.ordinal() + rotation.ordinal()) % 8];
    }

    public Vector2d toUnitVector(){
        switch(this){
            case NORTH:
                return new Vector2d(0,1);
            case NE:
                return  new Vector2d(1,1);
            case EAST:
                return new Vector2d(1, 0);
            case SE:
                return new Vector2d(1,-1);
            case SOUTH:
                return new Vector2d(0, -1);
            case SW:
                return new Vector2d(-1,-1);
            case WEST:
                return new Vector2d(-1, 0);
            case NW:
                return new Vector2d(-1,1);
            default:
                return null;

        }
    }


}
