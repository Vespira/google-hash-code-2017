package com.company.algo1;

import com.company.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by aberthiot on 24/02/2017.
 * Very simple algo to scatter videos on the cache servers -
 */

public class EvaluateSolution implements IAlgo {

    //liste des associations interdites cache/ Videos (pour ne pas avoir de duplicatas de vidéos id par cache)
    private Map<Cache,List<Video>> forbiddenAssociations = new HashMap<>();

    @Override
    public Map<Cache,List<Video>> trouveMeilleurSolution(Situation situation) {

        //Classify videos by size
        /*List<Video> smallVideos = situation.getVideoList().stream().filter(v -> v.getSize() < 200).collect(Collectors.toList());
        List<Video> mediumVideos = situation.getVideoList().stream().filter(v -> v.getSize() >= 200 && v.getSize() < 500).collect(Collectors.toList());
        List<Video> bigVideos = situation.getVideoList().stream().filter(v -> v.getSize() >= 500).collect(Collectors.toList());*/

        //map a retourner après opti
        Map<Cache,List<Video>> finalResult = new HashMap<>();

        // pour chaque endpoint
        for(Map.Entry<Integer, Endpoint> endpoint : situation.getEndpointList().entrySet()) {
            //on récupère toutes les requetes qui lui sont associé
            List<Request> requestsMadeByEndpoint = situation.getRequestList()
                    .stream()
                    .filter(r -> r.getFrom().getId() == endpoint.getValue().getId())
                    .collect(Collectors.toList());

            List<Cache> cachesSortedByLatency = new ArrayList<>();
            List<Video> videosRequestByEndpoints = new ArrayList<>();
            int totalSizeOfVideos = 0;

            //on parcours toutes les requêtes associées
            for(Request request : requestsMadeByEndpoint) {
                //on ordonne les caches connectés au endpoint par latence (ordre croissant)
                cachesSortedByLatency = endpoint.getValue().getCacheConnexions().entrySet()
                        .stream()
                        .sorted(Map.Entry.<Cache, Integer>comparingByValue())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                //forEachOrdered(x -> result.put(x.get...))

                //on ajoute la video de la requete dans la liste que l'on va traiter
                if(!videosRequestByEndpoints.contains(request.getVid())) {
                    videosRequestByEndpoints.add(request.getVid());
                }
                //on ajoute sa taille au passage a la taille totale des videos
                totalSizeOfVideos += request.getVid().getSize();
            }

            // si le endpoint a au moins un cache connecté
            if(cachesSortedByLatency.size() > 0) {
                // on ne garde que les vidéos plus petite que la taille des serveurs de cache
                // et on les trie par ordre de taille (décroissant)
                int cacheSize = cachesSortedByLatency.get(0).getSize();
                videosRequestByEndpoints = videosRequestByEndpoints
                        .stream()
                        .filter(v -> v.getSize() <=  cacheSize)
                        .sorted(Comparator.comparing(Video::getSize).reversed())
                        .collect(Collectors.toList());

                //on parcours les caches dispos
                for(Cache cache : cachesSortedByLatency) {
                    this.forbiddenAssociations.computeIfAbsent(cache, k -> new ArrayList<>());
                    //si on peut tout mettre dans le premier cache
                    if(cache.getSize() >= totalSizeOfVideos) {
                        finalResult.put(cache, videosRequestByEndpoints);
                        this.forbiddenAssociations.get(cache).addAll(videosRequestByEndpoints);
                        break;
                    } else {
                        //sinon, on essaye d'en mettre un max
                        List<Video> videosToStore = calculateVideosToStore(
                                cachesSortedByLatency.size(),
                                cache,
                                videosRequestByEndpoints);
                        //on prend les vidéos a ajouter et on les met dans la liste des associations interdites
                        this.forbiddenAssociations.get(cache).addAll(videosToStore);
                        finalResult.put(cache, videosToStore);

                        //on dégage les vidéos ajoutées de la liste a traiter
                        videosRequestByEndpoints = videosRequestByEndpoints.stream()
                                .filter(p -> !videosToStore.stream().map(Video::getId).collect(Collectors.toList()).contains(p.getId()))
                                .collect(Collectors.toList());
                    }
                }
            }
        }

        Long score = calculateScore(finalResult, situation);
        System.out.println("SCORE FINAL TOTAL = " + score);
        return finalResult;
    }

    private List<Video> calculateVideosToStore(int cacheNumber, Cache cache, List<Video> videosRequestByEndpoints) {

        //on doit recalculer ca, car la liste se réduit petit a petit
        //int newTotalSizeOfVideos = videosRequestByEndpoints.stream().mapToInt(v -> v.getSize()).sum();
        // taille qui va etre utilisée sur le cache server
        int usedSize = 0;

        List<Video> videosToStore = new ArrayList<>();

        // on a la place pour répartir équitablement
        //if((cacheNumber * cacheSize) - newTotalSizeOfVideos >= 0) {
            for(Video video : videosRequestByEndpoints) {
                // si la taille de la video rentre dans le cache (double check)
                if(video.getSize() <= cache.getSize() || !this.forbiddenAssociations.get(cache).contains(video)) {
                    if (cache.getSize() >= video.getSize() + usedSize) {
                        videosToStore.add(video);
                        usedSize += video.getSize();
                    } else {
                        break;
                    }
                }
            }
        //}

        return videosToStore;
    }

    private Long calculateScore(Map<Cache, List<Video>> finalResult, Situation situation) {

        Long score = 0L;
        //calcul de la somme des latences  Endpoints<->Datacenter
        Long totalSumLatency = situation.getEndpointList().entrySet().stream().mapToLong(e -> e.getValue().getLatencyToDataCenter()).sum();

        // somme totale des nb de requetes x (latence datacenter - latence au cache quu heberge la video)
        for(Request request : situation.getRequestList()) {

            Endpoint endpoint = request.getFrom();
            Integer latencyToCache = 0;
            //si on a pas de cache connecté, pas d'économie de temps possible -> 0
            for(Map.Entry<Cache, List<Video>> current : finalResult.entrySet()) {
                if(current.getValue().contains(request.getVid()) && endpoint.getCacheConnexions().containsKey(current.getKey())) {
                    latencyToCache = endpoint.getCacheConnexions().get(current.getKey());
                }
            }

            Long timeSaved = 0L;
            //si la latence au serveur de cache = 0
            if(!latencyToCache.equals(0)) {
                timeSaved = Long.valueOf(
                        situation.getEndpointList().get(request.getFrom().getId()).getLatencyToDataCenter() - latencyToCache
                );
            }

            Long totalSavedMsForRequestGroup = Long.valueOf(request.getNbRequests()) * timeSaved;
            score += totalSavedMsForRequestGroup;
        }

        return (score / totalSumLatency) * 1000;
    }
}
