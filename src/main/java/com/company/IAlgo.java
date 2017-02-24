package com.company;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by mfreche on 23/02/17.
 */
public interface IAlgo {

    Map<Cache, ? extends Collection<Video>> trouveMeilleurSolution(Situation situation);

}
