Welcome to Graph Visualiser!

Graph Visualiser is a small program that allows you to create graphs, and run algorithms on them
The current distribution only contains variants of the Dijkstra shortest path algorithm, however
more algorithms can be created by extending the class GraphAlgorithm or ShortestPathAlgorithm.

Installation instructions

    •Extract the provided JAR file in any desired location
    
    •Open a terminal in the chosen location

    •Enter <java Launcher> without the < and > to run GraphVisualiser


Requirements
 
    •Java 11
        -It is recommended to use at least Java 8, however this will not guarantee the app to be error-free

    •Should be able to run on any OS


PLEASE DO NOT DISTRIBUTE ANY SOURCE CODE INCLUDED IN THIS JAR FILE




Known bugs

    •Rotating the camera does not work at some orientations - this is due to a gimbal lock in the method used

    •On my machine at least, some saved graphs become unreadable after a random amount of time(this may be a 
     file system error rather than an app error)
 
    

To do list

    •Fix camera gimbal lock

    •Undo and redo commands for graph editing

    •More dijkstra variants
        -Bucket-queue dsp
        -geographic dsp?
    •Ability to edit the colour codes of nodes and edges?
