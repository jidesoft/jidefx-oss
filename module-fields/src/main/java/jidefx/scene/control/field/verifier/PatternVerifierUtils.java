package jidefx.scene.control.field.verifier;

import javafx.collections.ObservableMap;
import javafx.util.Callback;

import java.text.DateFormatSymbols;
import java.time.temporal.*;
import java.util.*;

/**
 * A util class which has several methods to initialize predefined pattern verifiers.
 */
public final class PatternVerifierUtils {
    /**
     * Initializes the PatternVerifiers for the pattern used by the DateFormat if the data type is Calendar.
     *
     * @param verifiers the verifier map.
     */
    public static void initializePatternVerifiersForDateFormatUsingCalendar(ObservableMap<String, Callback<String, Boolean>> verifiers) {

        // Era designator. AD or BC
        StringCalendarFieldPatternVerifier eraGroupVerifier = new StringCalendarFieldPatternVerifier(Calendar.ERA, new DateFormatSymbols().getEras());
        verifiers.put("GGG", eraGroupVerifier); //NON-NLS
        verifiers.put("GG", eraGroupVerifier); //NON-NLS
        verifiers.put("G", eraGroupVerifier); //NON-NLS

        // Year. 01996; 1996; 96
        verifiers.put("yyyyy", new IntegerCalendarFieldPatternVerifier(Calendar.YEAR, 0, 99999, true)); //NON-NLS
        verifiers.put("yyyy", new IntegerCalendarFieldPatternVerifier(Calendar.YEAR, 0, 9999, true)); //NON-NLS
        verifiers.put("yy", new IntegerCalendarFieldPatternVerifier(Calendar.YEAR, 0, 99, true)); //NON-NLS
        verifiers.put("y", new IntegerCalendarFieldPatternVerifier(Calendar.YEAR, 0, 9999, false));

        // Month in year as String. July; Jul;
        String[] months = new DateFormatSymbols().getMonths();
        verifiers.put("MMMMM", new StringCalendarFieldPatternVerifier(Calendar.MONTH, months)); //NON-NLS
        verifiers.put("MMMM", new StringCalendarFieldPatternVerifier(Calendar.MONTH, months)); //NON-NLS
        String[] shortMonths = new DateFormatSymbols().getShortMonths();
        verifiers.put("MMM", new StringCalendarFieldPatternVerifier(Calendar.MONTH, shortMonths)); //NON-NLS

        // Month in year as integer. 07; 7
        verifiers.put("MM", new IntegerCalendarFieldPatternVerifier(Calendar.MONTH, 1, 12, true)); //NON-NLS
        verifiers.put("M", new IntegerCalendarFieldPatternVerifier(Calendar.MONTH, 1, 12, false)); //NON-NLS

        // Week in year. 07; 7
        verifiers.put("ww", new IntegerCalendarFieldPatternVerifier(Calendar.WEEK_OF_YEAR, true)); //NON-NLS
        verifiers.put("w", new IntegerCalendarFieldPatternVerifier(Calendar.WEEK_OF_YEAR, false)); //NON-NLS

        // Week in month. 3
        verifiers.put("W", new IntegerCalendarFieldPatternVerifier(Calendar.WEEK_OF_MONTH, true)); //NON-NLS

        // Day in year. 004; 4
        verifiers.put("DDD", new IntegerCalendarFieldPatternVerifier(Calendar.DAY_OF_YEAR, true)); //NON-NLS
        verifiers.put("D", new IntegerCalendarFieldPatternVerifier(Calendar.DAY_OF_YEAR, false)); //NON-NLS

        // Day in month. 04; 4
        verifiers.put("dd", new IntegerCalendarFieldPatternVerifier(Calendar.DAY_OF_MONTH, true)); //NON-NLS
        verifiers.put("d", new IntegerCalendarFieldPatternVerifier(Calendar.DAY_OF_MONTH, false)); //NON-NLS

        // Day of week in Month. 4
        verifiers.put("F", new IntegerCalendarFieldPatternVerifier(Calendar.DAY_OF_WEEK_IN_MONTH, true)); //NON-NLS

        // Day in week. Tuesday, Tue
        String[] weekdays = new DateFormatSymbols().getWeekdays();
        verifiers.put("EEEE", new StringCalendarFieldPatternVerifier(Calendar.DAY_OF_WEEK, weekdays)); //NON-NLS

        String[] shortWeekdays = new DateFormatSymbols().getShortWeekdays();
        StringCalendarFieldPatternVerifier dayInWeekVerifier = new StringCalendarFieldPatternVerifier(Calendar.DAY_OF_WEEK, shortWeekdays);
        verifiers.put("EEE", dayInWeekVerifier); //NON-NLS
        verifiers.put("EE", dayInWeekVerifier); //NON-NLS
        verifiers.put("E", dayInWeekVerifier); //NON-NLS

        // Am/pm marker. AM or PM
        StringCalendarFieldPatternVerifier ampmVerifier = new StringCalendarFieldPatternVerifier(Calendar.AM_PM, new DateFormatSymbols().getAmPmStrings());
        verifiers.put("aaa", ampmVerifier); //NON-NLS
        verifiers.put("aa", ampmVerifier); //NON-NLS
        verifiers.put("a", ampmVerifier);

        // Hours in day (0 - 23)
        verifiers.put("HH", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR_OF_DAY, 0, 23, true)); //NON-NLS
        verifiers.put("H", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR_OF_DAY, 0, 23, false)); //NON-NLS

        // Hours in day (1 - 24)
        verifiers.put("kk", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR_OF_DAY, 1, 24, true)); //NON-NLS
        verifiers.put("k", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR_OF_DAY, 1, 24, false)); //NON-NLS

        // Hour in am/pm (0-11)
        verifiers.put("KK", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR, 0, 11, true)); //NON-NLS
        verifiers.put("K", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR, 0, 11, false)); //NON-NLS

        // Hour in am/pm (1-12)
        verifiers.put("hh", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR, 1, 12, true));
        verifiers.put("h", new IntegerCalendarFieldPatternVerifier(Calendar.HOUR, 1, 12, false));

        // Minutes in hour
        verifiers.put("mm", new IntegerCalendarFieldPatternVerifier(Calendar.MINUTE, true));
        verifiers.put("m", new IntegerCalendarFieldPatternVerifier(Calendar.MINUTE, false));

        // Seconds in minute
        verifiers.put("ss", new IntegerCalendarFieldPatternVerifier(Calendar.SECOND, true)); //NON-NLS
        verifiers.put("s", new IntegerCalendarFieldPatternVerifier(Calendar.SECOND, false));

        // Millisecond
        verifiers.put("SSS", new IntegerCalendarFieldPatternVerifier(Calendar.MILLISECOND, true)); //NON-NLS
        verifiers.put("S", new IntegerCalendarFieldPatternVerifier(Calendar.MILLISECOND, false)); //NON-NLS

        // Time zone
        StringValuesPatternVerifier zoneShort = new StringValuesPatternVerifier<String>(getZoneStrings(false)) {
            @Override
            public String toTargetValue(String fieldValue) {
                return fieldValue;
            }

            @Override
            public String fromTargetValue(String previousFieldValue, String targetValue) {
                return targetValue;
            }
        };
        verifiers.put("zzz", zoneShort); //NON-NLS
        verifiers.put("zz", zoneShort); //NON-NLS
        verifiers.put("z", zoneShort);
    }

    private static String[] getZoneStrings(boolean longFormat) {
        String[][] zones = new DateFormatSymbols().getZoneStrings();
        List<String> z = new ArrayList<>();

        if (longFormat) {
            for (String[] zone : zones) {
                if (!z.contains(zone[1])) {
                    z.add(zone[1]);
                }
            }
            for (String[] zone : zones) {
                if (!z.contains(zone[3])) {
                    z.add(zone[3]);
                }
            }
        }
        else {
            for (String[] zone : zones) {
                if (!z.contains(zone[2])) {
                    z.add(zone[2]);
                }
            }
            for (String[] zone : zones) {
                if (!z.contains(zone[4])) {
                    z.add(zone[4]);
                }
            }
        }
        return z.toArray(new String[z.size()]);
    }

    /**
     * Initializes the PatternVerifiers for the pattern used by the DateFormat if the data type is Date.
     *
     * @param verifiers the verifier map.
     */
    public static void initializePatternVerifiersForDateFormatUsingDate(ObservableMap<String, Callback<String, Boolean>> verifiers) {
        // Era designator. AD or BC
        StringDateFieldPatternVerifier eraGroupVerifier = new StringDateFieldPatternVerifier(Calendar.ERA, new DateFormatSymbols().getEras());
        verifiers.put("GGG", eraGroupVerifier); //NON-NLS
        verifiers.put("GG", eraGroupVerifier); //NON-NLS
        verifiers.put("G", eraGroupVerifier); //NON-NLS

        // Year. 01996; 1996; 96
        verifiers.put("yyyyy", new IntegerDateFieldPatternVerifier(Calendar.YEAR, 0, 99999, true)); //NON-NLS
        verifiers.put("yyyy", new IntegerDateFieldPatternVerifier(Calendar.YEAR, 0, 9999, true)); //NON-NLS
        verifiers.put("yy", new IntegerDateFieldPatternVerifier(Calendar.YEAR, 0, 99, true)); //NON-NLS
        verifiers.put("y", new IntegerDateFieldPatternVerifier(Calendar.YEAR, 0, 9999, false));

        // Month in year as String. July; Jul;
        String[] months = new DateFormatSymbols().getMonths();
        verifiers.put("MMMMM", new StringDateFieldPatternVerifier(Calendar.MONTH, months)); //NON-NLS
        verifiers.put("MMMM", new StringDateFieldPatternVerifier(Calendar.MONTH, months)); //NON-NLS
        String[] shortMonths = new DateFormatSymbols().getShortMonths();
        verifiers.put("MMM", new StringDateFieldPatternVerifier(Calendar.MONTH, shortMonths)); //NON-NLS

        // Month in year as integer. 07; 7
        verifiers.put("MM", new IntegerDateFieldPatternVerifier(Calendar.MONTH, 1, 12, true)); //NON-NLS
        verifiers.put("M", new IntegerDateFieldPatternVerifier(Calendar.MONTH, 1, 12, false)); //NON-NLS

        // Week in year. 07; 7
        verifiers.put("ww", new IntegerDateFieldPatternVerifier(Calendar.WEEK_OF_YEAR, true)); //NON-NLS
        verifiers.put("w", new IntegerDateFieldPatternVerifier(Calendar.WEEK_OF_YEAR, false)); //NON-NLS

        // Week in month. 3
        verifiers.put("W", new IntegerDateFieldPatternVerifier(Calendar.WEEK_OF_MONTH, true)); //NON-NLS

        // Day in year. 004; 4
        verifiers.put("DDD", new IntegerDateFieldPatternVerifier(Calendar.DAY_OF_YEAR, true)); //NON-NLS
        verifiers.put("D", new IntegerDateFieldPatternVerifier(Calendar.DAY_OF_YEAR, false)); //NON-NLS

        // Day in month. 04; 4
        verifiers.put("dd", new IntegerDateFieldPatternVerifier(Calendar.DAY_OF_MONTH, true)); //NON-NLS
        verifiers.put("d", new IntegerDateFieldPatternVerifier(Calendar.DAY_OF_MONTH, false)); //NON-NLS

        // Day of week in Month. 4
        verifiers.put("F", new IntegerDateFieldPatternVerifier(Calendar.DAY_OF_WEEK_IN_MONTH, true)); //NON-NLS

        // Day in week. Tuesday, Tue
        String[] weekdays = new DateFormatSymbols().getWeekdays();
        verifiers.put("EEEE", new StringDateFieldPatternVerifier(Calendar.DAY_OF_WEEK, weekdays)); //NON-NLS

        String[] shortWeekdays = new DateFormatSymbols().getShortWeekdays();
        StringDateFieldPatternVerifier dayInWeekVerifier = new StringDateFieldPatternVerifier(Calendar.DAY_OF_WEEK, shortWeekdays);
        verifiers.put("EEE", dayInWeekVerifier); //NON-NLS
        verifiers.put("EE", dayInWeekVerifier); //NON-NLS
        verifiers.put("E", dayInWeekVerifier); //NON-NLS

        // Am/pm marker. AM or PM
        StringDateFieldPatternVerifier ampmVerifier = new StringDateFieldPatternVerifier(Calendar.AM_PM, new DateFormatSymbols().getAmPmStrings());
        verifiers.put("aaa", ampmVerifier); //NON-NLS
        verifiers.put("aa", ampmVerifier); //NON-NLS
        verifiers.put("a", ampmVerifier);

        // Hours in day (0 - 23)
        verifiers.put("HH", new IntegerDateFieldPatternVerifier(Calendar.HOUR_OF_DAY, 0, 23, true)); //NON-NLS
        verifiers.put("H", new IntegerDateFieldPatternVerifier(Calendar.HOUR_OF_DAY, 0, 23, false)); //NON-NLS

        // Hours in day (1 - 24)
        verifiers.put("kk", new IntegerDateFieldPatternVerifier(Calendar.HOUR_OF_DAY, 1, 24, true)); //NON-NLS
        verifiers.put("k", new IntegerDateFieldPatternVerifier(Calendar.HOUR_OF_DAY, 1, 24, false)); //NON-NLS

        // Hour in am/pm (0-11)
        verifiers.put("KK", new IntegerDateFieldPatternVerifier(Calendar.HOUR, 0, 11, true)); //NON-NLS
        verifiers.put("K", new IntegerDateFieldPatternVerifier(Calendar.HOUR, 0, 11, false)); //NON-NLS

        // Hour in am/pm (1-12)
        verifiers.put("hh", new IntegerDateFieldPatternVerifier(Calendar.HOUR, 1, 12, true));
        verifiers.put("h", new IntegerDateFieldPatternVerifier(Calendar.HOUR, 1, 12, false));

        // Minutes in hour
        verifiers.put("mm", new IntegerDateFieldPatternVerifier(Calendar.MINUTE, true));
        verifiers.put("m", new IntegerDateFieldPatternVerifier(Calendar.MINUTE, false));

        // Seconds in minute
        verifiers.put("ss", new IntegerDateFieldPatternVerifier(Calendar.SECOND, true)); //NON-NLS
        verifiers.put("s", new IntegerDateFieldPatternVerifier(Calendar.SECOND, false));

        // Millisecond
        verifiers.put("SSS", new IntegerDateFieldPatternVerifier(Calendar.MILLISECOND, true)); //NON-NLS
        verifiers.put("S", new IntegerDateFieldPatternVerifier(Calendar.MILLISECOND, false)); //NON-NLS

        // Time zone
        StringValuesPatternVerifier zoneShort = new StringValuesPatternVerifier<String>(getZoneStrings(false)) {
            @Override
            public String toTargetValue(String fieldValue) {
                return fieldValue;
            }

            @Override
            public String fromTargetValue(String previousFieldValue, String targetValue) {
                return targetValue;
            }
        };
        verifiers.put("zzz", zoneShort); //NON-NLS
        verifiers.put("zz", zoneShort); //NON-NLS
        verifiers.put("z", zoneShort);
    }

    /**
     * Map of letters to fields.
     */
    private static final Map<Character, TemporalField> FIELD_MAP = new HashMap<>();
    private static final Map<Character, TemporalUnit> UNIT_MAP = new HashMap<>();

    static {
        // SDF = SimpleDateFormat
        FIELD_MAP.put('G', ChronoField.ERA);                       // SDF, LDML (different to both for 1/2 chars)
        FIELD_MAP.put('y', ChronoField.YEAR);                      // SDF, LDML  // TODO: use YEAR_OF_ERA throws unsupported exception in parse
        FIELD_MAP.put('u', ChronoField.YEAR);                      // LDML (different in SDF)
        FIELD_MAP.put('Q', IsoFields.QUARTER_OF_YEAR);             // LDML (removed quarter from 310)
        FIELD_MAP.put('q', IsoFields.QUARTER_OF_YEAR);             // LDML (stand-alone)
        FIELD_MAP.put('Y', ChronoField.YEAR);                      // LDML // TODO treat as 'u' for now
        FIELD_MAP.put('w', ChronoField.ALIGNED_WEEK_OF_YEAR);      // SDF  // TODO: use aligned version for now. not adjustable
        FIELD_MAP.put('W', ChronoField.ALIGNED_WEEK_OF_MONTH);     // SDF  // TODO: use aligned version for now. not adjustable
        FIELD_MAP.put('M', ChronoField.MONTH_OF_YEAR);             // SDF, LDML
        FIELD_MAP.put('L', ChronoField.MONTH_OF_YEAR);             // SDF, LDML (stand-alone)
        FIELD_MAP.put('D', ChronoField.DAY_OF_YEAR);               // SDF, LDML
        FIELD_MAP.put('d', ChronoField.DAY_OF_MONTH);              // SDF, LDML
        FIELD_MAP.put('F', ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);  // SDF, LDML
        FIELD_MAP.put('E', ChronoField.DAY_OF_WEEK);               // SDF, LDML (different to both for 1/2 chars)
        FIELD_MAP.put('c', ChronoField.DAY_OF_WEEK);               // LDML (stand-alone)
        FIELD_MAP.put('e', ChronoField.DAY_OF_WEEK);               // LDML (needs localized week number)
        FIELD_MAP.put('a', ChronoField.AMPM_OF_DAY);               // SDF, LDML
        FIELD_MAP.put('H', ChronoField.HOUR_OF_DAY);               // SDF, LDML
        FIELD_MAP.put('k', ChronoField.CLOCK_HOUR_OF_DAY);         // SDF, LDML
        FIELD_MAP.put('K', ChronoField.HOUR_OF_AMPM);              // SDF, LDML
        FIELD_MAP.put('h', ChronoField.HOUR_OF_AMPM);        // SDF, LDML // TODO: use HOUR_OF_AMPM instead of CLOCK_. See exception below

//java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: ClockHourOfAmPm
//	at java.time.temporal.TemporalAccessor.range(TemporalAccessor.java:172)
//	at java.time.format.Parsed.range(Parsed.java)
//	at java.time.temporal.TemporalAccessor.get(TemporalAccessor.java:215)
//	at java.time.format.Parsed.get(Parsed.java)
//	at jidefx.scene.control.field.verifier.IntegerTemporalPatternVerifier.parse(IntegerTemporalPatternVerifier.java:136)
//	at jidefx.scene.control.field.verifier.IntegerTemporalPatternVerifier.parse(IntegerTemporalPatternVerifier.java:22)

        FIELD_MAP.put('m', ChronoField.MINUTE_OF_HOUR);            // SDF, LDML
        FIELD_MAP.put('s', ChronoField.SECOND_OF_MINUTE);          // SDF, LDML
        FIELD_MAP.put('S', ChronoField.NANO_OF_SECOND);            // LDML (SDF uses milli-of-second number)
        FIELD_MAP.put('A', ChronoField.MILLI_OF_DAY);              // LDML
        FIELD_MAP.put('n', ChronoField.NANO_OF_SECOND);            // 310 (proposed for LDML)
        FIELD_MAP.put('N', ChronoField.NANO_OF_DAY);               // 310 (proposed for LDML)
        // 310 - z - time-zone names, matches LDML and SimpleDateFormat 1 to 4
        // 310 - Z - matches SimpleDateFormat and LDML
        // 310 - V - time-zone id, matches LDML
        // 310 - p - prefix for padding
        // 310 - X - matches LDML, almost matches SDF for 1, exact match 2&3, extended 4&5
        // 310 - x - matches LDML
        // 310 - w, W, and Y are localized forms matching LDML
        // LDML - U - cycle year name, not supported by 310 yet
        // LDML - l - deprecated
        // LDML - j - not relevant
        // LDML - g - modified-julian-day
        // LDML - v,V - extended time-zone names

        // SDF = SimpleDateFormat
        UNIT_MAP.put('G', ChronoUnit.ERAS);             // SDF, LDML (different to both for 1/2 chars)
        UNIT_MAP.put('y', ChronoUnit.YEARS);            // SDF, LDML
        UNIT_MAP.put('u', ChronoUnit.YEARS);            // LDML (different in SDF)
        UNIT_MAP.put('Q', ChronoUnit.MONTHS);           // LDML (removed quarter from 310)   // TODO: should be QUARTER but not defined
        UNIT_MAP.put('q', ChronoUnit.MONTHS);           // LDML (stand-alone)
        UNIT_MAP.put('Y', ChronoUnit.YEARS);            // LDML // TODO treat as 'u' for now
        UNIT_MAP.put('w', ChronoUnit.WEEKS);            // LDML
        UNIT_MAP.put('W', ChronoUnit.WEEKS);            // LDML
        UNIT_MAP.put('M', ChronoUnit.MONTHS);           // SDF, LDML
        UNIT_MAP.put('L', ChronoUnit.MONTHS);           // SDF, LDML (stand-alone)
        UNIT_MAP.put('D', ChronoUnit.DAYS);             // SDF, LDML
        UNIT_MAP.put('d', ChronoUnit.DAYS);             // SDF, LDML
        UNIT_MAP.put('F', ChronoUnit.WEEKS);            // SDF, LDML
        UNIT_MAP.put('E', ChronoUnit.DAYS);             // SDF, LDML (different to both for 1/2 chars)
        UNIT_MAP.put('c', ChronoUnit.DAYS);             // LDML (stand-alone)
        UNIT_MAP.put('e', ChronoUnit.DAYS);             // LDML (needs localized week number)
        UNIT_MAP.put('a', ChronoUnit.HALF_DAYS);        // SDF, LDML
        UNIT_MAP.put('H', ChronoUnit.HOURS);            // SDF, LDML
        UNIT_MAP.put('k', ChronoUnit.HOURS);            // SDF, LDML
        UNIT_MAP.put('K', ChronoUnit.HOURS);            // SDF, LDML
        UNIT_MAP.put('h', ChronoUnit.HOURS);            // SDF, LDML
        UNIT_MAP.put('m', ChronoUnit.MINUTES);          // SDF, LDML
        UNIT_MAP.put('s', ChronoUnit.SECONDS);          // SDF, LDML
        UNIT_MAP.put('S', ChronoUnit.NANOS);            // LDML (SDF uses milli-of-second number)
        UNIT_MAP.put('A', ChronoUnit.MILLIS);           // LDML
        UNIT_MAP.put('n', ChronoUnit.NANOS);            // 310 (proposed for LDML)
        UNIT_MAP.put('N', ChronoUnit.NANOS);            // 310 (proposed for LDML)
        // 310 - z - time-zone names, matches LDML and SimpleDateFormat 1 to 4
        // 310 - Z - matches SimpleDateFormat and LDML
        // 310 - V - time-zone id, matches LDML
        // 310 - p - prefix for padding
        // 310 - X - matches LDML, almost matches SDF for 1, exact match 2&3, extended 4&5
        // 310 - x - matches LDML
        // 310 - w, W, and Y are localized forms matching LDML
        // LDML - U - cycle year name, not supported by 310 yet
        // LDML - l - deprecated
        // LDML - j - not relevant
        // LDML - g - modified-julian-day
        // LDML - v,V - extended time-zone names
    }

    /**
     * Initializes the PatternVerifiers for the pattern used by the DateTimeFormatter.
     *
     * @param verifiers the verifier map.
     */
    public static void initializePatternVerifiersForDateTimeFormatter(ObservableMap<String, Callback<String, Boolean>> verifiers) {
        // Era designator. AD or BC
        installForPatternVerifierField(verifiers, "G"); //NON-NLS

        // Year. 01996; 1996; 96
        installForPatternVerifierField(verifiers, "u"); //NON-NLS

        // Year of era // TODO: use YEAR_OF_ERA throws unsupported exception in parse
        installForPatternVerifierField(verifiers, "y");

        // Day in year. 004; 4
        installForPatternVerifierField(verifiers, "D", 3); //NON-NLS

        // Month in year as String. July; Jul;
        // Month in year as integer. 07; 7
        installForPatternVerifierField(verifiers, "M"); //NON-NLS
        installForPatternVerifierField(verifiers, "L"); //NON-NLS

        // Day in month. 04; 4
        installForPatternVerifierField(verifiers, "d", 2); //NON-NLS

//        // Quarter of year. 3; 03; Q3; 3rd quarter
        installForPatternVerifierField(verifiers, "Q"); //NON-NLS
        installForPatternVerifierField(verifiers, "q"); //NON-NLS

//        // Week-based year
        installForPatternVerifierField(verifiers, "Y"); //NON-NLS

        // Week of week-based year. 07; 7
        installForPatternVerifierField(verifiers, "w", 2, false); //NON-NLS

        // Week in month. 3
        installForPatternVerifierField(verifiers, "W", 1, false); //NON-NLS

        // Day of week
        installForPatternVerifierField(verifiers, "E"); //NON-NLS

        // Localized day in week. Tuesday, Tue
        installForPatternVerifierField(verifiers, "e"); //NON-NLS
        installForPatternVerifierField(verifiers, "c", 1);

        // Week in month. 3
        installForPatternVerifierField(verifiers, "F", 1, false); //NON-NLS

        // Am/pm marker. AM or PM
        installForPatternVerifierField(verifiers, "a", 1);

        // Hour in am/pm (1-12)
        installForPatternVerifierField(verifiers, "h", 2);

        // Hour in am/pm (0-11)
        installForPatternVerifierField(verifiers, "K", 2); //NON-NLS

        // Hours in day (1 - 24)
        installForPatternVerifierField(verifiers, "k", 2); //NON-NLS

        // Hours in day (0 - 23)
        installForPatternVerifierField(verifiers, "H", 2); //NON-NLS

        // Minutes in hour
        installForPatternVerifierField(verifiers, "m", 2);

        // Seconds in minute
        installForPatternVerifierField(verifiers, "s", 2);

        // Millisecond
        installForPatternVerifierLongField(verifiers, "S", 9, false); //NON-NLS

        // Milli in day
        installForPatternVerifierLongField(verifiers, "A", 1); //NON-NLS

        // Nano in second
        installForPatternVerifierLongField(verifiers, "n"); //NON-NLS

        // Nano in in day
        installForPatternVerifierLongField(verifiers, "N"); //NON-NLS

    }

    private static void installForPatternVerifierField(ObservableMap<String, Callback<String, Boolean>> verifiers, String letter) {
        installForPatternVerifierField(verifiers, letter, 5);
    }

    private static void installForPatternVerifierField(ObservableMap<String, Callback<String, Boolean>> verifiers, String letter, int maxCount) {
        installForPatternVerifierField(verifiers, letter, maxCount, true);
    }

    private static void installForPatternVerifierField(ObservableMap<String, Callback<String, Boolean>> verifiers, String letter, int maxCount, boolean adjustable) {
        String pattern = letter;
        for (int i = 0; i < maxCount; i++) {
            TemporalField field = FIELD_MAP.get(letter.charAt(0));
            TemporalUnit unit = UNIT_MAP.get(letter.charAt(0));
//            if (field == null || unit == null) {
//                System.out.println(letter);
//            }
            verifiers.put(pattern, adjustable ?
                    new IntegerTemporalPatternVerifier(field, unit, pattern, pattern.length() != 1) :
                    new IntegerTemporalPatternVerifierNotAdjustable(field, unit, pattern, pattern.length() != 1)
            );
            pattern = pattern + letter;
        }
    }

    private static void installForPatternVerifierLongField(ObservableMap<String, Callback<String, Boolean>> verifiers, String letter) {
        installForPatternVerifierLongField(verifiers, letter, 5);
    }

    private static void installForPatternVerifierLongField(ObservableMap<String, Callback<String, Boolean>> verifiers, String letter, int maxCount) {
        installForPatternVerifierLongField(verifiers, letter, maxCount, true);
    }

    private static void installForPatternVerifierLongField(ObservableMap<String, Callback<String, Boolean>> verifiers, String letter, int maxCount, boolean adjustable) {
        String pattern = letter;
        for (int i = 0; i < maxCount; i++) {
            TemporalField field = FIELD_MAP.get(letter.charAt(0));
            TemporalUnit unit = UNIT_MAP.get(letter.charAt(0));
//            if (field == null || unit == null) {
//                System.out.println(letter);
//            }
            verifiers.put(pattern, adjustable ? new LongTemporalPatternVerifier(field, unit, pattern, pattern.length() != 1)
                    : new LongTemporalPatternVerifierNotAdjustable(field, unit, pattern, pattern.length() != 1));
            pattern = pattern + letter;
        }
    }
}
