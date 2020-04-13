/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekiosk;

import entity.PatientEntity;
import java.util.Scanner;

/**
 *
 * @author StudentStudent
 */
public class MainModule {

    Scanner sc;
    PatientEntity patient;

    public MainModule() {
    }

    public MainModule(Scanner sc, PatientEntity patient) {
        this.sc = sc;
        this.patient = patient;
        
        
        
    }

}
