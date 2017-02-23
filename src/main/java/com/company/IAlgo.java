package com.company;

import java.util.List;
import java.util.Map;

/**
 * Created by mfreche on 23/02/17.
 */
public interface IAlgo {

    Map<Cache, List<Video>> trouveMeilleurSolution(Situation situation);

}
