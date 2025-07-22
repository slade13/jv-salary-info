package core.basesyntax;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class SalaryInfo {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String getSalaryInfo(String[] names, String[] data, String dateFrom, String dateTo) {
        List<String> nameList = Arrays.asList(names);
        LocalDate from = LocalDate.parse(dateFrom, formatter);
        LocalDate to = LocalDate.parse(dateTo, formatter);

        final int[] calculatedSalaries = new int[names.length];
        Arrays.fill(calculatedSalaries, 0);

        Arrays.stream(data)
                .map(line -> line.split(" "))
                //check if name from `data` is inside `names` array
                .filter(splitData -> nameList.contains(splitData[1]))
                .filter(splitData -> {
                    // check if data is inclusively inside a date range
                    LocalDate actualDate = LocalDate.parse(splitData[0], formatter);
                    boolean isDateAfterInclusive = actualDate.isEqual(from)
                            || actualDate.isAfter(from);
                    boolean isDateBeforeInclusive = actualDate.isEqual(to)
                            || actualDate.isBefore(to);
                    return isDateAfterInclusive && isDateBeforeInclusive;
                })
                // calculate salary for a specific name (splitData[1])
                // by multiplying hours worked (splitData[2])
                // and earnings per hour (splitData[3]))
                .forEach(splitData -> calculatedSalaries[nameList.indexOf(splitData[1])]
                        += Integer.parseInt(splitData[2]) * Integer.parseInt(splitData[3]));

        StringBuilder result = new StringBuilder("Report for period ")
                .append(dateFrom)
                .append(" - ")
                .append(dateTo)
                .append(System.lineSeparator());
        for (int i = 0; i < names.length; i++) {
            result.append(names[i]).append(" - ").append(calculatedSalaries[i]);
            if (i < names.length - 1) {
                result.append(System.lineSeparator());
            }
        }
        return result.toString();
    }

}
