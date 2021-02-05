package threads;

import model.algorithm.GraphAlgorithm;
import viewport.Viewport;
import maths.Functions;

/**
*    The AlgorithmRunner keeps callinng the step() function of algorithms
*    in a separate thread, until either it is told to stop, or the algorithm
*    terminates
*    @author Harrison Boyle-Thomas
*    @date 04/02/21
**/
public class AlgorithmRunner extends Thread{
    boolean running = true;
    boolean paused = false;
    Viewport viewport;
    //delay in milliseconds
    int sleepDelay;

    public AlgorithmRunner(Viewport viewportIn, int delayIn){
        viewport = viewportIn;
        setSleepDelay(delayIn);
    }

    public boolean isRunning(){
        return running;
    }
    /**
    *    Calls the step() function repeatedly
    **/
    public void run(){
        if(viewport == null){
            return;
        }
        if(viewport.getAlgorithm() == null){
            return;
        }
        GraphAlgorithm algorithm = viewport.getAlgorithm();
        while(running && !algorithm.isFinished() && algorithm.isRunning()){
            while(paused){
                try{
                    wait();
                }
                catch(Exception e){

                }
            }
            System.out.println(algorithm.step());
            try{
                sleep(sleepDelay);
            }
            catch(Exception e){

            }
        }
        running = false;
        System.out.println("runner thread completed");
    }
    /**
    *    Set the running flag to false, so that the thread terminates
    **/
    public void terminate(){
        GraphAlgorithm algorithm = viewport.getAlgorithm();
        algorithm.terminate();
        running = false;
    }
    /**
    *    set the sleep delay to the given value
    *    the value is clamped between 0 and 1000
    **/
    public void setSleepDelay(int delayIn){
        sleepDelay = (int) Functions.clamp(delayIn, 0, 1000);
    }
    /**
    *       Flip the value of the paused flag
    *    @return true if the paused flag is true
    **/
    public boolean togglePause(){
        paused = !paused;
        return paused;
    }
    /**
    *    @return the value of the paused flag
    **/
    public boolean isPaused(){
        return paused;
    }
}
