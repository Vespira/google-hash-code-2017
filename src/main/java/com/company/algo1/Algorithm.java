package com.company.algo1;

import com.company.Situation;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Algorithm {

    private static final double MUTATION_RATE = 0.7;
    private static final double REPRODUCTION_RATE = 0.8;

		/* Public methods */

    // Evolve a population
    public static Population evolvePopulation(Population pop, Situation situation, int maxPop) {

        // Mutation
        Set<Individual> toMutate = pop.selectionIndividus((int) (pop.size() / 1.5));
        mutate(toMutate, MUTATION_RATE, pop);

        // Croisements
        Set<Individual> toCross = pop.selectionIndividus((int) (pop.size() / 2));
        cross(toCross, REPRODUCTION_RATE, pop);

        SortedSet<Individual> truncatedPop = pop.getFirstElements(maxPop);
        pop.individuals = new TreeSet<>(truncatedPop);

        return pop;
    }

    private static void mutate(Set<Individual> toMutate, double d, Population into) {

        for (Individual ind : toMutate) {
            Individual newInd = new Individual(into);
            for (int i = 0; i < ind.genes.length; i++) {
                if (Math.random() < d) {
                    // Mutation ! On remplace le gene par un nouveau gene al�atoire
                    Gene gene = newInd.generateGene(i);
                    newInd.setGene(i, gene);
                } else {
                    newInd.setGene(i, ind.getGene(i));
                }
            }
            newInd.mutation = true;
            newInd.recupVieux = ind.recupVieux;
            into.addIndividual(newInd);
        }

    }

    private static void cross(Set<Individual> toCross, double d, Population into) {
        Iterator<Individual> itCross = toCross.iterator();
        while (itCross.hasNext()) {
            if (Math.random() < d) {
                // Croisement !
                Individual indiv1 = itCross.next();
                if (!itCross.hasNext()) {
                    // Ok on en avait 1 mais pas 2 dans la liste
                    break;
                }
                Individual indiv2 = itCross.next();

                int a = ThreadLocalRandom.current().nextInt(0, indiv1.genes.length);
                int b = ThreadLocalRandom.current().nextInt(0, indiv1.genes.length);
                int min1 = (a < b) ? a : b;
                int min2 = (a < b) ? b : a;

                Individual fiston = new Individual(into);
                for (int i = 0; i < fiston.genes.length; i++) {
                    if (i < min1) {
                        fiston.setGene(i, indiv1.genes[i]);
                    } else if (i < min2) {
                        fiston.setGene(i, indiv2.genes[i]);
                    } else {
                        fiston.setGene(i, indiv1.genes[i]);
                    }
                }
                fiston.croisement = true;
                fiston.recupVieux = indiv1.recupVieux || indiv2.recupVieux;
                into.addIndividual(fiston);
            }
        }
    }

}
