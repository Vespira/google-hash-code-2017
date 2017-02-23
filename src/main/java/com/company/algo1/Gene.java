package com.company.algo1;

import com.company.Cache;
import com.company.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * Created by mfreche on 23/02/17.
 */
@AllArgsConstructor
@Data
public class Gene {

    Cache cache;
    List<Video> videoList;
}
