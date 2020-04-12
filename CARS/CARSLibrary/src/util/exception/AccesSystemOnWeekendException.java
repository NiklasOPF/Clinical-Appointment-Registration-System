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
public class AccesSystemOnWeekendException extends Exception{
    private String msg;

    public AccesSystemOnWeekendException() {
    }

    public AccesSystemOnWeekendException(String msg) {
        this();
        this.msg = msg;
    }
    
    public String getMessage(){
        return msg;
    }

}
