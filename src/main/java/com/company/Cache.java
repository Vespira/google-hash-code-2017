package com.company;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by mfreche on 23/02/17.
 */
@AllArgsConstructor
@Data
public class Cache {

    int id;
    int size;


    public Set<Video> randomSet(List<Video> fullList) {
        Set<Video> result = new HashSet<>();
        Set<Integer> setDejaTire = new HashSet<>();
        int i = 0                ;
        while ( setDejaTire.size() < fullList.size() ) {
            int index = ThreadLocalRandom.current().nextInt(fullList.size());
            if (!setDejaTire.contains(index)) {
                setDejaTire.add(index);
                Video v = fullList.get(index);
                if (i + v.getSize() <= size) {
                    result.add(v);
                    i += v.getSize();
                } else {
                    if (i > 0) { // Si on était à zéro autant tenter de prendre le suivant quand même non ?
                        return result;
                    }
                }
            }
        }
        return result;
    }

}
