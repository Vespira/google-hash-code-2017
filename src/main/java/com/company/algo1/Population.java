package com.company.algo1;

import com.company.Situation;
import com.company.Video;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Population {

    SortedSet<Individual> individuals;
    Situation situation;




    /*
     * Constructor
     */
    // Create a population
    public Population(int populationSize, boolean initialise, Situation situation) {
        this.situation = situation;
        individuals = new TreeSet<>();




        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < populationSize; i++) {
                Individual newIndividual = new Individual(this);
                newIndividual.generateIndividual();
                addIndividual(newIndividual);
            }
        }
    }



		/* Getters */
    //        public Individual getIndividual(int index) {
    //            return individuals.
    //        }

    public SortedSet<Individual> getFirstElements(int nbFirst) {
        int idx = 0;
        SortedSet<Individual> result = new TreeSet<>();
        for (Individual i : individuals) {
            if (idx <= nbFirst) {
                //					return individuals.headSet(i);
                result.add(i);
            }
            idx++;
        }
        return result;
        //			return individuals;
    }

    public Individual getMoreCompetent() {
        return individuals.first();
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.size();
    }

    // Save individual
    public void addIndividual(Individual indiv) {
        individuals.add(indiv);
    }

    /**
     * @param nb
     * @return
     */
    public Set<Individual> selectionIndividus(int nb) {
        Set<Individual> result = new HashSet<>();
        Set<Integer> setIndexToKeep = new HashSet<>();
        // On garde d'office les 5 premiers
        setIndexToKeep.addAll(Arrays.asList(0, 1, 2, 3, 4));
        // Puis on fait la roulette sur les suivants
        if (individuals.size() > 5) {
            for (int i = 0; i < nb; i++) {
                setIndexToKeep.add(ThreadLocalRandom.current().nextInt(5, individuals.size()));
            }
        }

        int idx = 0;
        for (Individual ind : individuals) {
            if (setIndexToKeep.contains(idx)) {
                result.add(ind);
            }
            idx++;
        }

        return result;
    }
}


