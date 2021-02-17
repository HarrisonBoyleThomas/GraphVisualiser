package threads;

import data.Data;

import model.algorithm.GraphAlgorithm;
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
    boolean terminated = false;
    GraphAlgorithm algorithm;
    //delay in milliseconds
    int sleepDelay;

    public AlgorithmRunner(GraphAlgorithm algorithmIn, int delayIn){
        algorithm = algorithmIn;
        updateSleepDelay();
    }

    public boolean isRunning(){
        return running;
    }
    /**
    *    Calls the step() function repeatedly
    **/
    public void run(){
        if(algorithm == null){
            return;
        }
        while(running && !algorithm.isFinished() && algorithm.isRunning() && !terminated){
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
        algorithm.terminate();
        running = false;
    }
    /**
    *    set the sleep delay to the given value
    *    the value is clamped between 0 and 1000
    **/
    public void updateSleepDelay(){
        sleepDelay = (int) Functions.clamp(Data.getExecutionSleepDelay(), 0, 1000);
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

    public void stopThread(){
        paused = false;
        terminated = true;
    }
}
