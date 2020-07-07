package com.saidinit.random.exercise1.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DataSet {
	private String officeName;
	private String userName;
	private Integer monthDay;
	private Integer hourOfDay;

	private List<LogLines> logs;
	private AvgTime time;
}
