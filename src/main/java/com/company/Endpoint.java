package com.company;

import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Created by mfreche on 23/02/17.
 */
@AllArgsConstructor
public class Endpoint {

    int id;

    /**
     * Id cache -> latence
     */
    Map<Cache, Integer> cacheConnexions;

    Integer latencyToDataCenter;

}
