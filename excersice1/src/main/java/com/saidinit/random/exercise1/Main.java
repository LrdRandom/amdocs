package com.saidinit.random.exercise1;

import com.saidinit.random.exercise1.model.Parameters;

public class Main {

	public static void main(String[] args) {

		Parameters params = convertToDataList(args);

		Exercise1 exc1 = new Exercise1();

		exc1.processLogLine(params.getOfficeName(), params.getUserName(), params.getMonthDay(), params.getTimeInMills(),
				params.getLog());

		exc1.processLogLine();

	}

	private static Parameters convertToDataList(String[] args) {
		// TODO create a method that transforms the args into something usable
		// assumed we have it
		// assumed it also loads all files files in folders
		return new Parameters();
	}

}
