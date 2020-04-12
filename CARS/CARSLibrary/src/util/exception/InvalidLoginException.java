/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;


public class InvalidLoginException extends Exception {
    private String msg;


    public InvalidLoginException() {
        this.msg = null;
    }

    public InvalidLoginException(String msg) {
        super();
        this.msg = msg;
    }
    
    public String getMsg(){
        return this.msg;
    }
}
