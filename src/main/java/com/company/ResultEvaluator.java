package com.company;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by mfreche on 23/02/17.
 */
public class ResultEvaluator {

    public static int evaluateResult(Situation situation, Map<Cache, List<Video>> mapCacheVideos) {
        int totalScore = 0;
        int totalNbReq = 0;
        for ( Request req : situation.getRequestList() ) {
            Endpoint endpoint = req.getFrom();
            int latenceDirecte = endpoint.latencyToDataCenter;
            Map.Entry<Cache, Integer> plusRapide = endpoint.cacheConnexions.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue))
                    .findFirst().orElse(null);
            int economie = 0;
            if ( plusRapide != null ) {
                economie = latenceDirecte - plusRapide.getValue();
            }
            totalScore += economie * req.getNbRequests();
            totalNbReq += req.getNbRequests();
        }
        return (int)(((double)totalScore / (double)totalNbReq) * 1000d);

    }

}
