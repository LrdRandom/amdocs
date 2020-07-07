package com.saidinit.random.exercise1.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogLines {
	private String officeName;
	private String userName;
	private int monthDay;

	private Instant time;
	private BankLog log;
}
