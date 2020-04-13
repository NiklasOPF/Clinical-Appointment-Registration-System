/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import entity.DoctorEntity;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import util.exception.AccesSystemOnWeekendException;

/**
 *
 * @author Niklas
 */
public class TimeFiltrator {

    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public static ArrayList<Calendar> getAllTimesToday() throws AccesSystemOnWeekendException {
        // Idea, start with the current time and iterate every 30 mins to get the relevant times
        ArrayList<Calendar> times = new ArrayList<>();
        Calendar current = Calendar.getInstance();
        Calendar lower = Calendar.getInstance();
        lower.set(Calendar.HOUR_OF_DAY, 8);
        lower.set(Calendar.MINUTE, 30);
        lower.set(Calendar.SECOND, 0);
        Calendar upper = Calendar.getInstance();
        // Note that last consultation is 30 mins before closing
        if (new HashSet<>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY)).contains(current.get(Calendar.DAY_OF_WEEK))) {
            upper.set(Calendar.HOUR_OF_DAY, 17);
            upper.set(Calendar.MINUTE, 30);

        } else if (current.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 16);
            upper.set(Calendar.MINUTE, 30);

        } else if (current.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 17); 
            upper.set(Calendar.MINUTE, 00);

        } else { //TODO handle this like an exception
            throw new AccesSystemOnWeekendException("The system is not open during weekends. Please come back on a weekday!");
            //System.err.println("The system is not open during weekends. Please come back on a weekday!");
            //return new ArrayList<Calendar>();
        }

        if (current.compareTo(lower) < 1) { // If earlier than opening time
            current.setTimeInMillis(lower.getTimeInMillis());
        } else {
            lower.setTimeInMillis(current.getTimeInMillis());
            if (current.get(Calendar.SECOND) != 0) { // round to next minute
                current.add(Calendar.MINUTE, 1);
                current.set(Calendar.SECOND, 0);
            }
            // round to next half an hour
            if (!new HashSet<>(Arrays.asList(0, 30)).contains(current.get(Calendar.MINUTE))) {
                current.add(Calendar.MINUTE, 30 - (current.get(Calendar.MINUTE) % 30));
            }
        }
        Calendar calendarCopy;

        lower.add(Calendar.HOUR_OF_DAY, 3);
        while (current.compareTo(upper) < 1 && current.compareTo(lower) < 1) {

            String tmp = timeFormatter.format(current.getTime());
            if (!"12:30".equals(tmp) && !"13:00".equals(tmp) && !"13:30".equals(tmp)) {
                //times.add(tmp);
                calendarCopy = Calendar.getInstance();
                calendarCopy.setTimeInMillis(current.getTimeInMillis());
                times.add(calendarCopy);
            }
            current.add(Calendar.MINUTE, 30);
        }

        return times;
    }

    public static ArrayList<Calendar> getAllTimesOfDay() throws AccesSystemOnWeekendException {
        // Idea, start with the current time and iterate every 30 mins to get the relevant times
        ArrayList<Calendar> times = new ArrayList<>();
        Calendar current = Calendar.getInstance();
        Calendar lower = Calendar.getInstance();
        lower.set(Calendar.HOUR_OF_DAY, 8);
        lower.set(Calendar.MINUTE, 30);
        lower.set(Calendar.SECOND, 0);
        Calendar upper = Calendar.getInstance();
        // Note that last consultation is 30 mins before closing
        if (new HashSet<>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY)).contains(current.get(Calendar.DAY_OF_WEEK))) {
            upper.set(Calendar.HOUR_OF_DAY, 17);
            upper.set(Calendar.MINUTE, 30);

        } else if (current.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 16);
            upper.set(Calendar.MINUTE, 30);

        } else if (current.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 17);
            upper.set(Calendar.MINUTE, 00);

        } else { //TODO handle this like an exception
            throw new AccesSystemOnWeekendException("The system is not open during weekends. Please come back on a weekday!");
            //System.err.println("The system is not open during weekends. Please come back on a weekday!");
            //return new ArrayList<Calendar>();
        }

        if (current.compareTo(lower) < 1) { // If earlier than opening time
            current.setTimeInMillis(lower.getTimeInMillis());
        } else {
            lower.setTimeInMillis(current.getTimeInMillis());
            if (current.get(Calendar.SECOND) != 0) { // round to next minute
                current.add(Calendar.MINUTE, 1);
                current.set(Calendar.SECOND, 0);
            }
            // round to next half an hour
            if (!new HashSet<>(Arrays.asList(0, 30)).contains(current.get(Calendar.MINUTE))) {
                current.add(Calendar.MINUTE, 30 - (current.get(Calendar.MINUTE) % 30));
            }
        }
        Calendar calendarCopy;

        lower.add(Calendar.HOUR_OF_DAY, 3);
        while (current.compareTo(upper) < 1 && current.compareTo(lower) < 1) {

            String tmp = timeFormatter.format(current.getTime());
            if (!"12:30".equals(tmp) && !"13:00".equals(tmp) && !"13:30".equals(tmp)) {
                //times.add(tmp);
                calendarCopy = Calendar.getInstance();
                calendarCopy.setTimeInMillis(current.getTimeInMillis());
                times.add(calendarCopy);
            }
            current.add(Calendar.MINUTE, 30);
        }

        return times;
    }


    
    
    
    
    
    /** The input represents the date of interest, 
     * the return is a list containing all possible consultation times of that day*/
    public static ArrayList<Calendar> getAllTimesOfDate(Calendar date){
        // Idea, start with the current time and iterate every 30 mins to get the relevant times
        ArrayList<Calendar> times = new ArrayList<>();
        
        Calendar lower = Calendar.getInstance();
        lower.setTime(date.getTime());
        lower.set(Calendar.HOUR_OF_DAY, 8);
        lower.set(Calendar.MINUTE, 30);
        lower.set(Calendar.SECOND, 0);
        
        Calendar upper = getClosingTime(date);
        if (upper==null){
             return times;
        }

        // increment the lower bound by 30 mins at a time until we reach the last time before closing
        // note that the tiem upper is closing time & is therefore not allowed
        Calendar calendarCopy;
        while (lower.compareTo(upper) < 0 ) {
            String tmp = timeFormatter.format(lower.getTime());
            if (!"12:30".equals(tmp) && !"13:00".equals(tmp) && !"13:30".equals(tmp)) {
                //times.add(tmp);
                calendarCopy = Calendar.getInstance();
                calendarCopy.setTimeInMillis(lower.getTimeInMillis());
                times.add(calendarCopy);
            }
            lower.add(Calendar.MINUTE, 30);
        }

        return times;
        
    }
    
    /** Returns the closing time for the clinic on the given date. Returns null if the date is on a weekend */
    private static Calendar getClosingTime(Calendar date){
        Calendar upper = Calendar.getInstance();
        upper.setTime(date.getTime());
        // Note that last consultation is 30 mins before closing
        if (new HashSet<>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY)).contains(date.get(Calendar.DAY_OF_WEEK))) {
            upper.set(Calendar.HOUR_OF_DAY, 17);
            upper.set(Calendar.MINUTE, 30);
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 16);
            upper.set(Calendar.MINUTE, 30);
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 17); 
            upper.set(Calendar.MINUTE, 00);
        } else {
            return null;
        }
        return upper;
    }
}
