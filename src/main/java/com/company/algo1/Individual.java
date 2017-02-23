package com.company.algo1;

import com.company.Cache;
import com.company.ResultEvaluator;
import com.company.Video;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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

    public void generateIndividualFromLast(Individual indiv) {
        recupVieux = true;
        for (int i = 0; i < size(); i++) {
            if (i+1 < indiv.size()) {
                genes[i] = indiv.getGene(i + 1);
            } else {
                Gene action = generateGene(i);
                genes[i] = action;
            }
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

    private Map<Cache,List<Video>> genesToMap(Gene[] genes) {
        Map<Cache,List<Video>> result = new HashMap<>();
        for ( int i = 0 ; i < genes.length ; i++ ) {
            result.put(genes[i].getCache(), genes[i].getVideoList());
        }
        return result;
    }

    public Map<Cache,List<Video>> genesToMap() {
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
        return this.getCompetence().compareTo(o.getCompetence());
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
