package com.G2T7.OurGardenStory.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtil {
  /**
   * Converts string in the format MM-dd-yyyy to a LocalDate object.
   * 
   * @param date
   * @return convertedLocalDate object
   */
  public static LocalDate convertStringToLocalDate(String date) {
    String dateComponents[] = date.split("-");

    int year = Integer.parseInt(dateComponents[2]);
    int month = Integer.parseInt(dateComponents[0]);
    int day = Integer.parseInt(dateComponents[1]);

    LocalDate convertedDate = LocalDate.of(year, month, day);
    return convertedDate;
  }

  public static String convertLocalDateToString(LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
  }

  // Obtains a Period from a text string such as PnYnMnD. Year, Month, Day
  public static Period convertStringToPeriod(String periodInput) {
    if (periodInput.contains("minute")) {
      return Period.of(0, 0, 0);
    }
    Period convertedPeriod = Period.parse(periodInput);
    return convertedPeriod;
  }

  // duration for PnDTnHnMn.nS. Day, Hour, Min, Sec
  public static Duration convertStringToDuration(String durationInput) {

    Duration convertedDuration = Duration.parse(durationInput);
    System.out.println("ok?");
    return convertedDuration;
  }

  public static LocalDate getWindowEndDateFromStartDateAndDuration(String startDateString, String timeFrameString) {
    LocalDate winStartDate = convertStringToLocalDate(startDateString);

    if (timeFrameString.contains("PT")) {
      return winStartDate.plus(DateUtil.convertStringToDuration(timeFrameString));
    } else if (timeFrameString.contains("P")) {
      return winStartDate.plus(DateUtil.convertStringToPeriod(timeFrameString));
    } else {
      throw new IllegalArgumentException("Invalid window duration " + timeFrameString);
    }
  }
}
