package in.cyberwalker.alliance.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeSet;

public class DateUtils {
    public static final long millisInDay = 86400000;
    public static final long millisInHour = 3600000;
    public static final String DATE_REFERENCE_TODAY = "today";
    public static final String DATE_REFERENCE_IMMEDIATELY = "immediately";
    public static final String DATE_REFERENCE_TOMORROW = "tomorrow";
    private static final String TAG = "DateUtils";

    public static HashMap<String, SimpleDateFormat> dateFormatMap = new HashMap<>();
    public static HashMap<String, SimpleDateFormat> dateParseMap = new HashMap<>();

    private static final String simpleDateFormat = "dd MMM yy";
    private static final String monthYearFormat = "MMM yyyy";

    public static final String MONTH_DAY_FORMAT = "dd MMM";
    public static final String DISPLAY_DATE_FORMAT = "dd MMM, yyyy";
    public static final String DISPLAY_DAY_DATE_FORMAT = "EE, dd MMM, yyyy";
    public static final String DISPLAY_DATE_FORMAT_SLASH = "dd/MM/yyyy";
    public static final String DISPLAY_DATE_FORMAT_FULL_YEAR = "dd MMM yyyy";

    public static final String DATE_TIME_FORMAT = "dd MMM, yyyy hh:mm a";

    public static String dateFormat = simpleDateFormat;
    public static String shortDateFormat = "dd/MM";
    public static String newShortDateFormat = MONTH_DAY_FORMAT;
    public static String shortTimeFormat = "HH:mm";
    public static String hourFormat = "HH";
    public static String minuteFormat = "mm";
    public static String timeFormat = "hh:mm aa";

    private static TreeSet<String> monthsSet = new TreeSet<>();

    static {
        monthsSet.add("jan");
        monthsSet.add("feb");
        monthsSet.add("mar");
        monthsSet.add("apr");
        monthsSet.add("may");
        monthsSet.add("jun");
        monthsSet.add("jul");
        monthsSet.add("aug");
        monthsSet.add("sep");
        monthsSet.add("oct");
        monthsSet.add("nov");
        monthsSet.add("dec");
    }

    static {
        // todo fix this
        //DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        //symbols.setAmPmStrings(new String[] { "AM", "PM" });
        //DATE_TIME_FORMAT.setDateFormatSymbols(symbols);
    }

    public static final String DAY_FORMAT = "dd";
    public static final String MONTH_FORMAT = "MMM";
    public static final String YEAR_FORMAT = "yyyy";

    public static void updateLocale() {
        dateFormatMap.clear();
    }


    /**
     * Returns a Date set to the last possible millisecond of the day, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfDay(Date day) {
        return getEndOfDay(day, Calendar.getInstance());
    }

    public static Date getEndOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * Returns a Date set to the last possible millisecond of the last day of the month, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfMonth(Date day) {
        return getEndOfMonth(day, Calendar.getInstance());
    }

    public static Date getEndOfMonth(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * Returns a Date set to the last possible millisecond of the last day of the week, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfWeek(Date day) {
        return getEndOfWeek(day, Calendar.getInstance());
    }

    public static Date getEndOfWeek(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int maxDayOfWeek = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
        if (maxDayOfWeek > dayOfWeek) {
            cal.add(Calendar.DATE, (maxDayOfWeek - dayOfWeek));
        }

        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a Date set to the first possible millisecond of the day, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfDay(Date day) {
        return getStartOfDay(day, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the first possible millisecond of the day, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * Returns a Date set to the first possible millisecond of the first day of the month, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfMonth(Date day) {
        return getStartOfMonth(day, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the first possible millisecond of the first day of the month, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfMonth(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * Returns a Date set to the first possible millisecond of the first day of the week, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfWeek(Date day) {
        return getStartOfWeek(day, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the first possible millisecond of the first day of the week, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfWeek(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int minDayOfWeek = cal.getActualMinimum(Calendar.DAY_OF_WEEK);
        if (minDayOfWeek < dayOfWeek) {
            cal.add(Calendar.DATE, (minDayOfWeek - dayOfWeek));
        }

        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }


    /**
     * Returns a Date set just to Noon, to the closest possible millisecond
     * of the day. If a null day is passed in, a new Date is created.
     * nnoon (00m 12h 00s)
     */
    public static Date getNoonOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a java.sql.Timestamp equal to the current time
     */
    public static java.sql.Timestamp now() {
        return new java.sql.Timestamp(new java.util.Date().getTime());
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a string the represents the passed-in date parsed
     * according to the passed-in format.  Returns an empty string
     * if the date or the format is null.
     */
    public static String format(Date aDate, SimpleDateFormat aFormat) {
        if (aDate == null || aFormat == null) {
            return "";
        }
        synchronized (aFormat) {
            return aFormat.format(aDate);
        }
    }

    //-----------------------------------------------------------------------

    //-----------------------------------------------------------------------

    /**
     * Returns a Date using the passed-in string and format.  Returns null if the string
     * is null or empty or if the format is null.  The string must match the format.
     */
    public static Date parse(String aValue, SimpleDateFormat aFormat) throws ParseException {
        if (TextUtils.isEmpty(aValue) || aFormat == null) {
            return null;
        }

        return aFormat.parse(aValue);
    }

    public static String displayDate(Date date) {
        return format(date, DISPLAY_DATE_FORMAT);
    }

    public static String displayDayDate(Date date) {
        return format(date, DISPLAY_DAY_DATE_FORMAT);
    }

    public static Date getDate(int year) {
        if (year > 0) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, c.getMinimum(Calendar.MONTH));
            c.set(Calendar.DAY_OF_MONTH, c.getMinimum(Calendar.DAY_OF_MONTH));
            return c.getTime();
        } else {
            return null;
        }
    }

    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }


    public static int getMinuteOfHour(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);

    }

    public static int getHourOfDay(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);

    }


    public static int getDayOfMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);

    }

    public static int getDayOfWeek(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);

    }

    public static int getWeekOfMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_MONTH);
    }

    // checks if  date1 is after date2

    public static boolean isAfter(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);

        if (c1.after(c2)) {
            return true;
        } else {
            return false;
        }
    }

    // checks if  date1 is before date2

    public static boolean isBefore(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);

        if (c1.before(c2)) {
            return true;
        } else {
            return false;
        }
    }

    //checks if date 1 and date2 are in the same day

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }


    // Returns the no of days in the month passed
    public static int getDaysInMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static String format(Date date, String pattern) {
        return getFormattedDateString(date, pattern);
    }

    public static String getFormattedDateString(Date date, String format) {
        String result = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                synchronized (sdf) {
                    result = sdf.format(date);
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static Date getDate(String day, String month, String year) {
        Date date = null;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);
        int calDay = Integer.parseInt(day);
        int calMonth = Integer.parseInt(month);
        int calYear = Integer.parseInt(year);
        if (calDay > -1 && calMonth > -1 && calYear > -1) {
            cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            date = cal.getTime();
        }
        return date;
    }

    public static Date getDate(int day, int month, int year) {
        Date date = null;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);

        if (day > -1 && month > -1 && year > -1) {
            cal.set(year, month, day);
            date = cal.getTime();
        }
        return date;
    }

    public static Date getDate(int day, int month, int year, int hour, int minute) {
        Date date = null;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);

        if (day > -1 && month > -1 && year > -1 && hour > -1 && minute > -1) {
            cal.set(year, month, day, hour, minute);
            date = cal.getTime();
        }
        return date;
    }

    public static Date getDate(int hour, int minute) {
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minute);
        return date;
    }

    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date getStartOfYesterday() {
        return getStartOfDay(getYesterday());
    }

    public static Date getDateFromString(String date) throws ParseException {
        SimpleDateFormat pars = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date dateObj = null;
        if (!TextUtils.isEmpty(date)) {
            dateObj = pars.parse(date);
        }
        return dateObj;
    }

    public static Date getDateFromString(String date, String dateFormat) throws ParseException {
        SimpleDateFormat pars = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date dateObj = null;
        if (!TextUtils.isEmpty(date)) {
            dateObj = pars.parse(date);
        }
        return dateObj;
    }

    public static String parseToString(Date date) {
        SimpleDateFormat pars = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String parseDate = null;
        if (date != null) {
            parseDate = pars.format(date);
        }
        return parseDate;
    }

    /**
     * This returns days in between start date and end date
     * eg: returns 1 for days between yesterday and today
     * Note: excludes the last day
     *
     * @return
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        if (startDate.getTime() < endDate.getTime()) {
            return (int) ((getStartOfDay(endDate).getTime() - startDate.getTime()) / millisInDay);
        } else {
            return (int) ((getStartOfDay(startDate).getTime() - endDate.getTime()) / millisInDay);
        }
    }

    /**
     * Only Sunday is considered as holiday :)
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getWorkingDaysBetween(Date startDate, Date endDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int totaldays = (int) ((endDate.getTime() - startDate.getTime()) / 86400000);
        int workingdays = totaldays - ((totaldays + (5 + cal.get(Calendar.DAY_OF_WEEK)) % 7) / 7);
        return workingdays;
    }

    public static int getAge(Date dob) {
        if (dob == null || dob.getTime() > now().getTime()) {
            return 0;
        }
        Calendar now = Calendar.getInstance();
        Calendar dobCal = Calendar.getInstance();
        dobCal.setTime(dob);
        if ((now.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR))) {
            return now.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR) - 1;
        } else {
            return now.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
        }
    }

    public static boolean isMinor(Date dob) {
        Date now = new Date();
        Date date18 = DateUtils.addYears(DateUtils.getStartOfDay(now), -18);
        return DateUtils.isAfter(dob, date18);
    }

    public static Date addYears(Date date, int numberOfYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, numberOfYears);
        return calendar.getTime();
    }

    public static Date addDays(int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }

    public static Date getDateBeforeRequiredDays(int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -numberOfDays);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }

    public static Date addMonths(Date date, int numberOfMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, numberOfMonths);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int numberOfMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, numberOfMinutes);
        return calendar.getTime();
    }

    public static Date addHours(Date date, int numberOfHours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, numberOfHours);
        return calendar.getTime();
    }

    public static Date getDateBeforeRequiredDaysAfterAnyDate(Date date, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -numberOfDays);
        return calendar.getTime();
    }

    public static Boolean isGivenDateBetweenDates(Date givenDate, Date from, Date to) {
        return givenDate.getTime() >= from.getTime() && givenDate.getTime() <= to.getTime();
    }

    public static Date getStartOfYear(Date year) {
        return getStartOfYear(year, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the first possible millisecond of the first day of the year, just
     * after midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getStartOfYear(Date year, Calendar cal) {
        if (year == null)
            year = new Date();

        cal.setTime(year);
        cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }


    public static Date getEndOfYear(Date year) {
        return getEndOfYear(year, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the last possible millisecond of the last day of the month, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfYear(Date year, Calendar cal) {
        if (year == null)
            year = new Date();

        cal.setTime(year);
        cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public static Date getStartOfQuarter(Date day) {
        return getStartOfMonth(day, Calendar.getInstance());
    }

    /**
     * Returns a Date set to the last possible millisecond of the last day of the month, just
     * before midnight. If a null day is passed in, a new Date is created.
     * midnight (00m 00h 00s)
     */
    public static Date getEndOfQuarter(Date day) {
        return getEndOfQuarter(day, Calendar.getInstance());
    }

    public static Date getEndOfQuarter(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.add(Calendar.MONTH, 3);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * crude logic to see if the pattern matches the date value
     * we do validation for common fields and that may not always be correct
     *
     * @param dateFormat
     * @param dateStr
     * @return
     */
    public static boolean isMatchingDateFormat(String dateFormat, String dateStr) {
        dateFormat = dateFormat.trim();
        int delta = dateFormat.length() - dateStr.length();
        if (delta < 0) {
            delta = -1 * delta;
        }

        if (delta <= 2) {
            // break into parts
            StringBuilder separators = new StringBuilder();
            separators.append("[");
            if (dateFormat.contains("-"))
                separators.append("-");
            if (dateFormat.contains("/"))
                separators.append("/");
            if (dateFormat.contains("."))
                separators.append("\\.");
            if (dateFormat.contains(" "))
                separators.append(" ");
            if (dateFormat.contains(":"))
                separators.append(":");

            if (separators.length() == 1) {
                // didn't find any separators
                // can't really do anything for now
                // todo - fix it in next version
                return true;
            }
            separators.append("]+");

            String[] formatParts = dateFormat.split(separators.toString());
            String[] dateParts = dateStr.split(separators.toString());

            // firstly the number of parts should match
            if (formatParts.length == dateParts.length) {
                // compare each part
                for (int p = 0; p < formatParts.length; p++) {
                    String formatPart = formatParts[p];
                    String datePart = dateParts[p];

                    if (formatPart.equals("dd") || formatPart.equals("d")) {
                        if (!isValidDay(datePart))
                            return false;
                    } else if (formatPart.equals("MM") || formatPart.equals("M")) {
                        if (!isValidMonth(datePart))
                            return false;
                    } else if (formatPart.equals("MMM")) {
                        if (!isValidMonthStr(datePart))
                            return false;
                    } else if (formatPart.equals("yyyy") || formatPart.equals("yy") || formatPart.equals("y")) {
                        if (!isValidYear(datePart))
                            return false;
                    } else if (formatPart.equals("hh") || formatPart.equals("h")) {
                        if (!isValidHour(datePart))
                            return false;
                    } else if (formatPart.equals("HH") || formatPart.equals("H")) {
                        if (!isValidHour24(datePart))
                            return false;
                    } else if (formatPart.equals("mm") || formatPart.equals("m")) {
                        if (!isValidMinuteOrSecond(datePart))
                            return false;
                    } else if (formatPart.equals("ss") || formatPart.equals("s")) {
                        if (!isValidMinuteOrSecond(datePart))
                            return false;
                    } else {
                        // ignore this part
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private static boolean isValidDay(String day) {
        try {
            int i = Integer.parseInt(day);
            return i >= 1 && i <= 31;
        } catch (Exception ex) {
            // oops
        }
        return false;
    }

    private static boolean isValidMonth(String month) {
        try {
            int i = Integer.parseInt(month);
            return i >= 1 && i <= 12;
        } catch (Exception ex) {
            // oops
        }
        return false;
    }

    private static boolean isValidMonthStr(String month) {
        try {
            if (monthsSet.contains(month.toLowerCase()))
                return true;
        } catch (Exception ex) {
            // oops
        }
        return false;
    }

    private static boolean isValidYear(String year) {
        try {
            int i = Integer.parseInt(year);
            Calendar c = Calendar.getInstance();
            int currentYear = c.get(Calendar.YEAR);

            return (i >= (currentYear % 100) - 5 && i <= (currentYear % 100) + 1) || (i >= currentYear - 5 && i <= currentYear + 1);
        } catch (Exception ex) {
            // oops
        }
        return false;
    }

    private static boolean isValidHour(String hour) {
        try {
            int i = Integer.parseInt(hour);
            return i >= 0 && i <= 12;
        } catch (Exception ex) {
            // oops
        }
        return false;
    }

    private static boolean isValidHour24(String hour) {
        try {
            int i = Integer.parseInt(hour);
            return i >= 0 && i <= 23;
        } catch (Exception ex) {
            // oops
        }
        return false;
    }

    private static boolean isValidMinuteOrSecond(String v) {
        try {
            int i = Integer.parseInt(v);
            return i >= 0 && i <= 59;
        } catch (Exception ex) {
            // oops
        }
        return false;
    }

    public static void main(String args[]) {
        Date d = null;
        try {
            //d = getDate("1", "1", "1985");
            //d = parse(null, "25-08-14", "dd-MM-yyyy");
            //System.out.println(d.toString());

            String s = "2##f";
            String[] a = s.split("##");
            System.out.println("1. " + a[0] + "   2. " + a[1]);

            System.out.println("monthsBetween: " + monthsBetween(new Date(), getDate(1, 1, 2016)));

        } catch (Exception e) {
        }
    }

    public static String getMonthName(Date date) {
        return format(date, "MMM");
    }

    public static String getMonthYear(Date date) {
        return format(date, monthYearFormat);
    }

    public static String getMonthDay(Date date) {
        return format(date, MONTH_DAY_FORMAT);
    }

    public static String toDateString(Date date) {
        if (date == null)
            return "";

        return format(date, simpleDateFormat);
    }


    public static String toDateTimeString(Date date) {
        if (date == null)
            return "";

        return format(date, DATE_TIME_FORMAT);
    }

    public static int daysTillNow(Date date) {
        return daysBetween(new Date(), date);
    }

    public static int daysFromNow(Date date) {
        return daysBetween(date, new Date());
    }

    public static int daysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return -1;

        return (int) ((getStartOfDay(date1).getTime() - getStartOfDay(date2).getTime()) / (1000 * 60 * 60 * 24));
    }

    public static int monthsBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return -1;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);

        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);

        int yearsBetween = year1 - year2;
        int monthsBetween = month1 - month2;

        return (yearsBetween * 12) + monthsBetween;
    }

    public static int hoursBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return -1;

        return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60));
    }

    public static Date getPreviousYear(Date day) {
        return getPreviousYear(day, Calendar.getInstance());
    }

    public static Date getPreviousYear(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public static Date getDateForDayOfMonth(Date firstDayOfMonth, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDayOfMonth);
        if (dayOfMonth > 1) {
            calendar.add(Calendar.DAY_OF_MONTH, (dayOfMonth - 1));
        }
        return calendar.getTime();
    }
}