//2022-11-07T00:00:00 -> 07/11/2022
export default function formatDateTimeToDate(rawDateTime: string) {
  if (rawDateTime == null) return "";

  return rawDateTime
    .substring(8, 10)
    .concat("/", rawDateTime.substring(5, 7), "/", rawDateTime.substring(0, 4));
}
