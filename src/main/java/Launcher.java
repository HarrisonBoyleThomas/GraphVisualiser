/**
*    The Launcher class is used to start the app. JavaFX classes cannot be called
*    directly from the command line
**/
public class Launcher {
    public static void main(String[] args){
        GraphVisualiser app = new GraphVisualiser();
        app.main(args);
    }
}
