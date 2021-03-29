Welcome to Graph Visualiser!

Graph Visualiser is a small program that allows you to create graphs, and run algorithms on them
More algorithms can be created by extending the class GraphAlgorithm, or any generic-named algorithm, 
such as DijkstraShortestPath.

Installation instructions

    •Extract the provided JAR file in any desired location
    
    •Open a terminal in the chosen location

    •Enter <java Launcher> without the < and > to run GraphVisualiser


Requirements
 
    •Java 11
        -It is recommended to use at least Java 8, however this will not guarantee the app to be error-free
 
    •JavaFX
        -It is recommended to use the newest JavaFX release, especially for linux users.

    •Should be able to run on any OS


PLEASE DO NOT DISTRIBUTE ANY SOURCE CODE INCLUDED IN THIS JAR FILE




Known bugs

    •Rotating the camera does not work in some orientations - this is due to a gimbal lock in the method used

    •On my machine at least, some saved graphs become unreadable after a random amount of time(this may be a 
     file system error rather than an app error)
 
    
New features

    •New algorithms!
        -Bucket queue dsp
        -Bellman ford(standard)
        -Kruskal's algorithm
        -Prim's algorithm

    •Undo and redo commands

    •Customisable node and edge colour codes

    •Customisable key bindings

    •Improved manual

    •Ease of use improvements
        -Double click a node or edge to go straight to the top editable field in the details panel
        -Pixel-perfect hitboxes for nodes and edges

To do list:
    
    •polishing
