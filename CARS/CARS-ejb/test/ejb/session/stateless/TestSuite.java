/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * NOTE THAT YOU MUST DROP AND RE-CREATE THE DATABASE BEFORE RUNNING THE TESTS IN ORDER TO GET ACCURATE RESULTS.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
    DoctorSessionBeanTest.class,
    PatientSessionBeanTest.class})
public class TestSuite {
    @BeforeClass
    public static void setUpClass() throws Exception{
        System.err.println("********** IS2103 CARS.setUpClass()");
    }
    
    @AfterClass
        public static void tearDownClass() throws Exception{
        System.err.println("********** IS2103 CARS.tearDownClass()");
    }
    

    @Before
    public void setUp() throws Exception 
    {
        System.out.println("********** Is2103 CARS.setUp()");
    }

    @After
    public void tearDown() throws Exception 
    {
        System.out.println("********** Is2103 CARS.tearDown()");
    }    

    
}
