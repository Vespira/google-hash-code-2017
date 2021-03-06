package com.company;

import com.company.algo1.Algo1;
import com.company.algo1.EvaluateSolution;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /*static final String BASE_PATH_IN = "/home/mfreche/workspace/github/git/google-hash-code-2017/src/main/resources/";
    static final String BASE_PATH_OUT = "/home/mfreche/";*/
    static final String BASE_PATH_IN = "C:/Users/aberthiot/Workspace/google-hash-code-2017/src/main/resources/";
    static final String BASE_PATH_OUT = "C:/tmp/";
/*    static final String BASE_PATH_IN = "C:/dev/github/google-hash-code-2017/src/main/resources/";
    static final String BASE_PATH_OUT = "C:/dev/github/google-hash-code-2017/out/";*/

    public static void main(String[] args)
            throws IOException {

        //IAlgo algo = new Algo1(20 * 1000);
        //fichierSample(algo);
        IAlgo algo = new EvaluateSolution();
        //fichierReel(algo, "sampleFile.in");
        //fichierReel(algo, "kittens.in");
        fichierReel(algo, "me_at_the_zoo.in");
        //fichierReel(algo, "trending_today.in");
        //fichierReel(algo, "videos_worth_spreading.in");

    }

    private static void fichierSample(IAlgo algo)
            throws IOException {
        Situation situation = new Situation();
        situation.getVideoList().add(new Video(0,50));
        situation.getVideoList().add(new Video(1,50));
        situation.getVideoList().add(new Video(2,80));
        situation.getVideoList().add(new Video(3,30));
        situation.getVideoList().add(new Video(4,110));
        situation.getCacheList().put(0, new Cache(0,100));
        situation.getCacheList().put(1,new Cache(1,100));
        situation.getCacheList().put(2, new Cache(2,100));
        Map<Cache, Integer> cacheConnexion = new HashMap<>();
        cacheConnexion.put(situation.getCacheList().get(0), 100);
        cacheConnexion.put(situation.getCacheList().get(1), 300);
        cacheConnexion.put(situation.getCacheList().get(2), 200);
        situation.getEndpointList().put(0,new Endpoint(0, cacheConnexion, 1000));
        situation.getEndpointList().put(1,new Endpoint(1, cacheConnexion, 1000));
        situation.getRequestList().add(new Request
                (situation.getEndpointList().get(0),1500, situation.getVideoList().get(3)));
        situation.getRequestList().add(new Request
                (situation.getEndpointList().get(1),1000, situation.getVideoList().get(0)));
        situation.getRequestList().add(new Request
                (situation.getEndpointList().get(0),500, situation.getVideoList().get(4)));
        situation.getRequestList().add(new Request
                (situation.getEndpointList().get(0),1000, situation.getVideoList().get(1)));

        Map<Cache, List<Video>> result = algo.trouveMeilleurSolution(situation);

        FileWriter fw = new FileWriter();
        fw.ecrireFichierResultat(BASE_PATH_OUT + "sample.out",result);
    }

    private static void fichierReel(IAlgo algo, String fileName)
            throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        //IParseInputFile parseInputFile = new ParseInputFile();
        IParseInputFile parseInputFile = new ParserFromScratch();
        //String path = classLoader.getResource("kittens.in").toString();
        String path = BASE_PATH_IN + fileName;

        Situation situation =  parseInputFile.parseFile(path);

        System.out.println("Calcul de la meilleure solution...");
        Map<Cache, List<Video>> result = algo.trouveMeilleurSolution(situation);

        FileWriter fw = new FileWriter();
        fw.ecrireFichierResultat(BASE_PATH_OUT + fileName,result);
    }
}
