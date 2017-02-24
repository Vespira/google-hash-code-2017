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


    private static Integer findPlusRapide(Map<String, Integer> mapLatences, Map<Cache, Set<Video>> mapCacheVideos, Request req) {
        String key = req.getFrom().getId() + " " + req.getVid().getId();
        if ( !mapLatences.containsKey(key) ) {
            Endpoint end = req.getFrom();
            alimenterMapLatencePourEndpoint(end, mapLatences, mapCacheVideos);
        }
        return mapLatences.get(key);
    }

    private static void alimenterMapLatencePourEndpoint(Endpoint endpoint, Map<String, Integer> mapLatences,
            Map<Cache, Set<Video>> mapCacheVideos) {
        for ( Map.Entry<Cache, Integer> cacheConnexion : endpoint.cacheConnexions.entrySet() ) {
            Set<Video> videoDispos = mapCacheVideos.get(cacheConnexion.getKey());
            parcoureVideosDansUnCache(endpoint, mapLatences, cacheConnexion, videoDispos);
        }
    }

    private static void parcoureVideosDansUnCache(Endpoint endpoint, Map<String, Integer> mapLatences,
            Map.Entry<Cache, Integer> cacheConnexion, Set<Video> videoDispos) {
        for ( Video v : videoDispos ) {
            String key2 = endpoint.getId() + " " + v.getId();
            Integer meilleureLatence = mapLatences.get(key2);
            if ( meilleureLatence == null || cacheConnexion.getValue() < meilleureLatence ) {
                mapLatences.put(key2, cacheConnexion.getValue());
            }
        }
    }

}
