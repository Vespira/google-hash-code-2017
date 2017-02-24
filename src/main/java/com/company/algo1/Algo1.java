package com.company.algo1;

import com.company.Cache;
import com.company.IAlgo;
import com.company.Situation;
import com.company.Video;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by mfreche on 23/02/17.
 */
public class Algo1 implements IAlgo {

    int timeout;



    public Algo1(int timeout) {
        this.timeout = timeout;
    }


    @Override public Map<Cache, Set<Video>> trouveMeilleurSolution(Situation situation) {

        // Create an initial population
        Individual.setDefaultGeneLength(situation.getCacheList().size());
        long start = System.nanoTime();
        Population myPop = new Population(50, true, situation);
        long end = System.nanoTime();
        long duration = (end - start) / 1000000;
        System.out.println("Génération de la population : "+duration+" ms");
        // Evolve our population until we reach an optimum solution



        start = System.nanoTime();
        ExecutorService bigExecutor = Executors.newSingleThreadExecutor();
        GenetiqueRunnable genetiqueRunnable = new GenetiqueRunnable(situation, myPop);
        bigExecutor.submit(genetiqueRunnable);
        bigExecutor.shutdown();
        try {
            bigExecutor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }

        bigExecutor.shutdownNow();
        try {
            bigExecutor.awaitTermination(5, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        myPop = genetiqueRunnable.innerPop;


        //        System.err.println("Solution found!");
        end = System.nanoTime();
        duration = (end - start) / 1000000;
        System.out.println("Evolution de la population : "+duration+" ms");

        System.err.println("Generation: " + genetiqueRunnable.generationCount);
        Individual moreCompetent = myPop.getMoreCompetent();
        System.err.println("Score : "+moreCompetent.getCompetence());
        return moreCompetent.genesToMap();
    }
}
