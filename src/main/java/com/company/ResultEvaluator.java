package com.company;

import java.util.*;
import java.util.stream.Collectors;

import static com.sun.corba.se.impl.util.RepositoryId.cache;

/**
 * Created by mfreche on 23/02/17.
 */
public class ResultEvaluator {

    public static int evaluateResult(Situation situation, Map<Cache, Set<Video>> mapCacheVideos) {
        int totalScore = 0;
        int totalNbReq = 0;
        Map<String, Integer> mapLatences = new HashMap<>();
        for ( Endpoint endpoint : situation.getTableRequest().rowKeySet() ) {
            int latenceDirecte = endpoint.latencyToDataCenter;
            //Integer plusRapide = findPlusRapide(mapLatences, mapCacheVideos, situation.getTableRequest().row(endpoint));
            List<Map.Entry<Cache, Integer>> sortedEntries = endpoint.getCacheConnexions().entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getValue))
                    .collect(Collectors.toList());

            Map<Video, Integer> row = situation.getTableRequest().row(endpoint);
            Set<Video> videosRecherchees = new HashSet<>(row.keySet());
            for ( Map.Entry<Cache, Integer> cacheConnexion : sortedEntries ) {
                Set<Video> videosSurLeCache = mapCacheVideos.get(cacheConnexion.getKey());
                Set<Video> copyRecherche = new HashSet<>(videosRecherchees);
                copyRecherche.retainAll(videosSurLeCache); // on a les vidéos recherchées sur ce cache
                videosRecherchees.removeAll(copyRecherche);
                for ( Video v : copyRecherche ) {
                    int economie = latenceDirecte - cacheConnexion.getValue();
                    Integer nbReq = row.get(v);
                    totalScore += economie * nbReq;
                    totalNbReq += nbReq;
                }
                if ( videosRecherchees.isEmpty() ) {
                    break;
                }
            }




        }
        return (int)(((double)totalScore / (double)totalNbReq) * 1000d);

    }




}
