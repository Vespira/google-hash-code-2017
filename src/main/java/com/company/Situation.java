package com.company;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mfreche on 23/02/17.
 */
@Getter
public class Situation {

    Map<Integer, Cache> cacheList = new HashMap<>();
    Map<Integer,Endpoint> endpointList = new HashMap<>();
    List<Request> requestList = new ArrayList<>();
}