/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

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

    private int lastIssuedQueueNumber; //TODO reset every day

    public QueueSessionBean() {
        resetQueue();
    }

    public int getNewQueueNumber() {
        this.lastIssuedQueueNumber++;
        return this.lastIssuedQueueNumber;
    }
    
    public void resetQueue(){
        lastIssuedQueueNumber = 0;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
