package threads;

import menu.MainWindow;
import viewport.VisualGraphNode;
import viewport.VisualGraphEdge;

import java.util.ArrayList;

/**
*    The RenderThread sorts the VGN list such that nodes that are further from
*    the camera are at the front of the list
*    @author Harrison Boyle-Thomas
*    @date 05/02/21
**/
public class RenderThread extends Thread{
    private static RenderThread renderThread;


    public static void startRenderThread(){
        if(renderThread == null){
            renderThread = new RenderThread();
            renderThread.start();
        }
    }

    private RenderThread(){

    }

    public void run(){
        while(true){
            VisualGraphNode.sortByDistance(MainWindow.get().getCamera());
            VisualGraphEdge.sortByDistance(MainWindow.get().getCamera());
        }
    }
}
