package com.company;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by mfreche on 23/02/17.
 */
@AllArgsConstructor
@Getter
@Deprecated
public class Request {

    Endpoint from;
    int nbRequests;
    Video vid;
}
