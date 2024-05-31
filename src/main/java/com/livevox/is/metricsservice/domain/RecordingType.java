package com.livevox.is.metricsservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RecordingType implements Serializable {

    private String callRecording;
}