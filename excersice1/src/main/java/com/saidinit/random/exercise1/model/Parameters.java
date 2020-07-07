package com.saidinit.random.exercise1.model;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class Parameters {

	private String officeName;
	private String userName;
	private String monthDay;
	private String timeInMills;
	private String log;
	private List<File> logString;

}
