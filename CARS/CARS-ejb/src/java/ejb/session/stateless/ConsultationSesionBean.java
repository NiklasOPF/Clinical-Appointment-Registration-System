/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import util.exception.LeaveToCloseInTimeException;

/**
 *
 * @author StudentStudent
 */
public class ConsultationSesionBean {

    public ConsultationSesionBean() {
    }

    public HashMap<LocalTime,Boolean> walkIn() throws Exception, LeaveToCloseInTimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime now = LocalTime.now();
        String nowString = now.format(formatter);
        now = LocalTime.parse(nowString);
        ArrayList<LocalTime> openingHours = checkOpen();
        LocalTime opening = openingHours.get(0);
        LocalTime closing = openingHours.get(1);
        if (!(now.isAfter(opening) && now.isBefore(closing.minusMinutes(30)))) {
            throw new LeaveToCloseInTimeException("Unable to book a consultation");
        } else {
            //take out min from current
            Integer currMin = now.getMinute();
            Integer minsToAdd;
            if (currMin < 30) {
                minsToAdd = 30 - currMin;
            } else {
                minsToAdd = 60 - currMin;
            }
            LocalTime nextConsultTime = now.plusMinutes(minsToAdd); 
            HashMap<LocalTime,Boolean> consultationMap = new HashMap<>();
            
            return consultationMap;
        }
    }

    private ArrayList<LocalTime> checkOpen() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Integer day = calendar.get(Calendar.DAY_OF_WEEK);
        ArrayList<LocalTime> openCloseDate = new ArrayList<>(2);//0 will be open date 1 will be close
        String openTime = "";
        String closeTime = "";
        switch (day) {
            case Calendar.MONDAY:
            case Calendar.TUESDAY:
            case Calendar.WEDNESDAY:
                //0830 - 1800
                openTime = "08:00";
                closeTime = "18:00";
                break;
            case Calendar.THURSDAY:
                //0830 - 1700
                openTime = "08:00";
                closeTime = "17:00";
                break;
            case Calendar.FRIDAY:
                //0830 - 1730
                openTime = "08:00";
                closeTime = "17:30";
                break;
            default:
                throw new Exception("It is closed");
        }
        LocalTime opening = LocalTime.parse(openTime);
        LocalTime closing = LocalTime.parse(closeTime);
        openCloseDate.add(0, opening);
        openCloseDate.add(1, closing);
        return openCloseDate;
    }
}
