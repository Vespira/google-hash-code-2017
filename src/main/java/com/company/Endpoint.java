package com.company;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mfreche on 23/02/17.
 */
@AllArgsConstructor
@Data
public class Endpoint {

    int id;

    /**
     * Id cache -> latence
     */
    Map<Cache, Integer> cacheConnexions = new HashMap<>();

    Integer latencyToDataCenter;

}
