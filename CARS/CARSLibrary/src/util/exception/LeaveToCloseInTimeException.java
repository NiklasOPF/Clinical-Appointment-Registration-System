/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Niklas
 */
public class LeaveToCloseInTimeException extends Exception {

    public LeaveToCloseInTimeException() {
    }

    public LeaveToCloseInTimeException(String msg) {
        super(msg);
    }

}
