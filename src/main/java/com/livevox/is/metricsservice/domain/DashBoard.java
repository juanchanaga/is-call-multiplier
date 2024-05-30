package com.livevox.is.metricsservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashBoard {
    private List<CallCenter> callCenters;
    private int id;
}
