package com.saidinit.random.exercise1;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.saidinit.random.exercise1.model.AvgTime;
import com.saidinit.random.exercise1.model.BankLog;
import com.saidinit.random.exercise1.model.DataSet;
import com.saidinit.random.exercise1.model.LogLines;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class Exercise1 {

	private String officeName;
	private String userName;
	private Integer dayOfMonth;
	private Integer hourOfDay;

	private List<DataSet> dataList = new ArrayList<>();

	public void processLogLine(String officeName, String userName, String monthDay, String timeInMills,
			String logString) {

		LogLines line = LogLines.builder().log(BankLog.valueOf(logString))
				.time(Instant.ofEpochSecond(convertToMills(timeInMills))).build();

		// its a new line
		if (logString.equals(BankLog.values()[0].getLogValue())) {
			List<LogLines> lines = new ArrayList<>();
			lines.add(line);
			DataSet data = DataSet.builder().officeName(officeName).userName(userName)
					.monthDay(Integer.valueOf(monthDay)).hourOfDay(Integer.valueOf(timeInMills.substring(0, 2)))
					.logs(lines).build();
			dataList.add(data);
		}
		// its the same but a new entry on the loglines
		else {
			dataList.get(dataList.size() - 1).getLogs().add(line);
		}

	}

	public void processLogLine() {

		// we filter (or not) based on optionals
		List<DataSet> filteredList = dataList.stream()
				.filter(ds -> officeName == null ? true : ds.getOfficeName().equals(officeName))
				.filter(ds -> userName == null ? true : ds.getUserName().equals(userName))
				.filter(ds -> dayOfMonth == null ? true : ds.getMonthDay().equals(dayOfMonth))
				.filter(ds -> hourOfDay == null ? true : ds.getHourOfDay().equals(hourOfDay))
				.collect(Collectors.toList());

		// first we calculate the average of time of each specific tracelog
		List<AvgTime> avgTimes = filteredList.stream().map(ds -> calculateTimes(ds.getLogs()))
				.collect(Collectors.toList());

		// then we calculate the avg of all of them
		// TODO: do not go through the stream three times you should be able to get all
		// this data in on loop
		double scanDoc = avgTimes.stream().map(AvgTime::getScanDoc).mapToLong(val -> val).average().orElse(0.0);

		double saveDisc = avgTimes.stream().map(AvgTime::getSaveDisc).mapToLong(val -> val).average().orElse(0.0);

		double imageShow = avgTimes.stream().map(AvgTime::getImageShow).mapToLong(val -> val).average().orElse(0.0);

		log.info("some stuff htat includes the avgTimes, the doubles and the dataset");
	}

	private long convertToMills(String timeInMills) {
		// TODO parse the timeInMills (hh:mm:ss.mmm) to milliseconds
		return 0;
	}

	private AvgTime calculateTimes(List<LogLines> logString) {

		// since all logs are a-ok and everything works fine we know that logString
		// contains all 5 types of BankLog exactly once
		// we didn't need an enum, but I feel confy with it
		return AvgTime.builder()
				.scanDoc(logString.get(0).getTime().until(logString.get(1).getTime(), ChronoUnit.MILLIS))
				.saveDisc(logString.get(2).getTime().until(logString.get(3).getTime(), ChronoUnit.MILLIS))
				.imageShow(logString.get(4).getTime().until(logString.get(5).getTime(), ChronoUnit.MILLIS)).build();
	}

	/**
	 * This method is an example of how to get the number of documents processed for
	 * one filter, you can do the same for different filters but I am not writing
	 * them
	 */
	private long numberOfDocumentsTreatedByOffice(String officeName) {
		// TODO: this needs to be a filtered list of the dataset not all dataset
		return dataList.stream().filter(ds -> ds.getOfficeName().equals(officeName)).count();
	}

}
