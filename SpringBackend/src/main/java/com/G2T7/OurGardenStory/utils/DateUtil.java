package com.G2T7.OurGardenStory.utils;

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

  // Obtains a Period from a text string such as PnYnMnD
  public static Period convertStringToPeriod(String periodInput) {
    Period convertedPeriod = Period.parse(periodInput);
    return convertedPeriod;
  }
}
