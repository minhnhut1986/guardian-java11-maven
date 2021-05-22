package core;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static Integer DAY = 3;

    public static String printCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static String printCurrentDate(String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date = new Date(System.currentTimeMillis());
        Log.debug("Current date : " + formatter.format(date));
        return formatter.format(date);
    }

    public static String printDateTimeAfterCurrentDay(String dateFormat, int addDays) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(System.currentTimeMillis());
        String currentDate = sdf.format(date);

        Calendar c = Calendar.getInstance();

        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(currentDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, addDays);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        //Displaying the new Date after addition of Days
        Log.debug("Date after Addition: "+newDate);

        return newDate;
    }

    public static long getTodayStartMillis() {
//        ZonedDateTime startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
//        long todayMillis = startOfToday.toEpochSecond() * 1000;
//        Log.debug("Today millisecond : " + todayMillis);

        long todayMillis;

        DateTimeZone timeZone = DateTimeZone.forID( "Asia/Ho_Chi_Minh" );

        DateTime now = DateTime.now( timeZone );

        DateTime todayStart = now.withTimeAtStartOfDay();

        todayMillis = todayStart.getMillis();

        return todayMillis;
    }

    public static long getTomorrowStartMillis() {
        long todayMillis;

        DateTimeZone timeZone = DateTimeZone.forID( "Asia/Ho_Chi_Minh" );

        DateTime now = DateTime.now( timeZone );

        DateTime tomorrowStart = now.plusDays( 1 ).withTimeAtStartOfDay();

        todayMillis = tomorrowStart.getMillis();

        return todayMillis;
    }

    public static int getDayOfMillis(String millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(millis));

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isMonthAndYearSameCurrentDate(String date) {
        Date currentDate= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        Log.debug("Get current month " + currentMonth + ", current year " + currentYear);

        String[] temp = date.split("/");
        int expectedMonth = Integer.valueOf(temp[1]);
        int expectedYear = Integer.valueOf(temp[2]);
        Log.debug("Get expected month " + expectedMonth + ", expected year " + expectedYear);

        if (currentMonth == expectedMonth && currentYear == expectedYear) return true;
        else return false;
    }

    public static int getExtraMonthAndYearWithCurrentDate(String date) {

        Date currentDate= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        Log.debug("Get current month " + currentMonth + ", current year " + currentYear);

        String[] temp = date.split("/");
        int expectedMonth = Integer.valueOf(temp[1]);
        int expectedYear = Integer.valueOf(temp[2]);
        Log.debug("Get expected month " + expectedMonth + ", expected year " + expectedYear);

        if (expectedYear < currentYear) {
            Log.debug("Please input date >= current date !");
            return 0;
        }

        if (expectedMonth < currentMonth && expectedYear == currentYear) {
            Log.debug("Please input date >= current date !");
            return 0;
        }

        int extraMonth = 0;

        if (expectedYear > currentYear) {

            extraMonth = (expectedMonth - currentMonth) + ((expectedYear - currentYear) * 12) - 1;

        } else if (expectedYear == currentYear) {

            extraMonth = expectedMonth - currentMonth;

        }

        return extraMonth;
    }
}
