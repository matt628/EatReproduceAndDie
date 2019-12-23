public class World {
    public static void main(String[] args){
        try {
    //WORLD CONST
        //MAP
            Vector2d upperRight = new Vector2d(30,30);
            Vector2d jungleLowerLeft = new Vector2d(10,10);
            Vector2d jungleUpperRight = new Vector2d(20,20);
            Boundary boundary = new Boundary(upperRight,jungleLowerLeft,jungleUpperRight);
        //CREATURES
            int startAnimalNumber = 30;
            int startEnegry =10;
            int grassNumber = 1;
                //BASIC PARAMETERS
            int parousiaDay =20000;
        //CREATION
            Multiversum multiversum = new Multiversum(grassNumber, parousiaDay, boundary,
                    startAnimalNumber, startEnegry);
            multiversum.createNewWorld();
        } catch(IllegalArgumentException | IllegalStateException | InterruptedException ex) {
            System.out.println("There is an interesting message for you.");
            System.out.println(ex);
        }
    }
}
