/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import java.util.Calendar;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;

/**
 *
 * @author Niklas
 */
@Local(QueueSessionBeanLocal.class)
@Remote(QueueSessionBeanRemote.class)
@Singleton
public class QueueSessionBean implements QueueSessionBeanRemote, QueueSessionBeanLocal {

    private int lastIssuedQueueNumber; 
    private Calendar today;

    public QueueSessionBean() {
        if (!areSameDate(today, Calendar.getInstance())){resetQueue();}
    }

    /**
     * 
     * @return A new incremental queue number that resets at midnight. 
     */
    @Override
    public int getNewQueueNumber() {
        if (!areSameDate(today, Calendar.getInstance())){
            resetQueue();
            return getNewQueueNumber();
        }

        this.lastIssuedQueueNumber++;
        return this.lastIssuedQueueNumber;
    }

    private void resetQueue() {
        lastIssuedQueueNumber = 0;
        today = Calendar.getInstance();
    }

    private boolean areSameDate(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null){
            return false;
        }
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
            return false;
        }
        if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) {
            return false;
        }
        return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }


}
