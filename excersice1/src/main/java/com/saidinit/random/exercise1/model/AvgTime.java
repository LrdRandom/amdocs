package com.saidinit.random.exercise1.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvgTime {

	private long scanDoc;
	private long saveDisc;
	private long imageShow;
	private int docsScan;

}
