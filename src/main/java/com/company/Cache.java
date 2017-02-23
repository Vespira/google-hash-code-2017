package com.company;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 * Created by mfreche on 23/02/17.
 */
@AllArgsConstructor
@Data
public class Cache {

    int id;
    int size;

    public List<Video> randomSet(List<Video> fullList) {
        List<Video> result = new ArrayList<>();
        List<Video> cloneList = new ArrayList<>(fullList);
        Collections.shuffle(cloneList);
        int i = 0;
        for ( Video v : cloneList ) {
            if ( i + v.getSize() <= size ) {
                result.add(v);
                i+=v.getSize();
            } else {
                if ( i > 0 ) { // Si on était à zéro autant tenter de prendre le suivant quand même non ?
                    return result;
                }
            }
        }
        return result;
    }

}
