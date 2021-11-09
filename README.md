Welcome to Graph Visualiser!

Graph Visualiser is a small program that allows you to create graphs, and run algorithms on them.
More algorithms can be created by extending the class GraphAlgorithm, or any generic-named algorithm, 
such as DijkstraShortestPath.

Installation instructions

    •Extract the graphVisualiser folder in a desired location
    
    •Open a terminal in the graphVisualiser folder

    •Enter <gradle run> without the < and > to run GraphVisualiser. If the run fails, throwing hundreds of errors in
     the code, retry the command two more times.

    •The app will print the output of operations it is executing into the terminal window, which is a good indicator
     that everything is set up correctly

    •A manual for the app is available from the HELP tab inside the app, which provides information about the app, controls 
     and a video explaining how to use the app.


Requirements

    •Gradle
        -Gradle build automation tools are required to run this distribution of GraphVisualiser
        -Available at https://gradle.org/install/

    •Should be able to run on any OS


PLEASE DO NOT DISTRIBUTE ANY SOURCE CODE INCLUDED IN THIS PROJECT




Known bugs

    •Rotating the camera does not work in some orientations - this is due to a gimbal lock in the rotation method used
 
    •The viewport does not always update correctly-this can be worked around by moving the camera or clicking in the
     viewport a few times. This issue is more common on low-end devices

    •gdk warnings are displayed if running on Linux. This is common when performing drag and drop operations
