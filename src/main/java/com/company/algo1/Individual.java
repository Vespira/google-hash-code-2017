package com.company.algo1;

import com.company.Cache;
import com.company.ResultEvaluator;
import com.company.Video;

import java.util.*;

public  class Individual
        implements Comparable<Individual> {

    static int defaultGeneLength = 8;
    final Gene[] genes = new Gene[defaultGeneLength];
    // Cache
    private Integer competence = null;
    boolean recupVieux = false;
    boolean mutation = false;
    boolean croisement = false;
    Population pop;


    public Individual(Population pop) {
        this.pop = pop;
    }

    // Create a random individual
    public void generateIndividual() {
        for (int i = 0; i < size(); i++) {
            Gene action = generateGene(i);
            genes[i] = action;
        }
    }



    public Gene generateGene(int index) {
        Cache cache = pop.situation.getCacheList().get(index);
        Gene action = new Gene(cache, cache.randomSet(pop.situation.getVideoList()));
        return action;
    }

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }

    public Gene getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, Gene value) {
        genes[index] = value;
        competence = null;
    }

    /* Public methods */
    public int size() {
        return genes.length;
    }

    public Integer getCompetence() {
        if (competence == null) {
            competence = ResultEvaluator.evaluateResult(pop.situation,genesToMap(genes));
        }
        return competence;
    }

    private Map<Cache,Set<Video>> genesToMap(Gene[] genes) {
        Map<Cache,Set<Video>> result = new HashMap<>();
        for ( int i = 0 ; i < genes.length ; i++ ) {
            result.put(genes[i].getCache(), genes[i].getVideoList());
        }
        return result;
    }

    public Map<Cache,Set<Video>> genesToMap() {
        return genesToMap(getGenes());
    }

    @Override
    public String toString() {
        return Arrays.toString(genes);
        //            StringBuilder sb = new StringBuilder();
        //            for (int i = 0; i < size(); i++) {
        //                sb.append(getGene(i));
        //            }
        //            return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Individual o) {
        if (Arrays.equals(this.genes, o.genes)) {
            return 0;
        }
        return o.getCompetence().compareTo(this.getCompetence());
    }


    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    /**
     * @return
     */
    public Gene[] getGenes() {
        return genes;
    }


}
