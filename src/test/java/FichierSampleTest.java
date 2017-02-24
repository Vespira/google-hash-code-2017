import com.company.*;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mikael on 24/02/2017.
 */
public class FichierSampleTest {

    @Test
    public void testCalculScoreSample() {
        // GIVEN
        Situation sample = Main.creerSituationSample();
        Map<Cache, Set<Video>> mapResultat = new HashMap<>();
        mapResultat.put(sample.getCacheList().get(0), Sets.newHashSet(sample.getVideoList().get(2)));
        mapResultat.put(sample.getCacheList().get(1), Sets.newHashSet(sample.getVideoList().get(3), sample.getVideoList().get(1)));
        mapResultat.put(sample.getCacheList().get(2), Sets.newHashSet(sample.getVideoList().get(0), sample.getVideoList().get(1)));

        // WHEN
        int resultat = ResultEvaluator.evaluateResult(sample, mapResultat);

        // THEN
        Assertions.assertEquals(462500, resultat);
    }



}
