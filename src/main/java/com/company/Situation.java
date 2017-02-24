package com.company;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mfreche on 23/02/17.
 */
@Data
public class Situation {

    Map<Integer, Cache> cacheList = new HashMap<>();
    Map<Integer,Endpoint> endpointList = new HashMap<>();
    Table<Endpoint, Video, Integer> tableRequest = HashBasedTable.create();
    List<Video> videoList = new ArrayList<>();
}
