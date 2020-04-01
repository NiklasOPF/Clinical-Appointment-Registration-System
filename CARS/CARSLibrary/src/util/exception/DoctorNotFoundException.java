/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author StudentStudent
 */
public class DoctorNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>DoctorException</code> without detail
     * message.
     */
    public DoctorNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DoctorException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DoctorNotFoundException(String msg) {
        super(msg);
    }
}
