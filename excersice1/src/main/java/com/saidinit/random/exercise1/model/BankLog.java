package com.saidinit.random.exercise1.model;

public enum BankLog {
	START("*********Starting scan********"), SCAN_DONE("Scan done. Image loaded in memory"),
	SAVING_TIF("Saving sample TIF image in share disc ..."), LOADING_IMG("Loading image... "),
	IMG_SHOW("Image showed in applet");

	private String logValue;

	BankLog(String logValue) {
		this.logValue = logValue;
	}

	public String getLogValue() {
		return logValue;
	}

}
