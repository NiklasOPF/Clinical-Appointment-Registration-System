/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import ejb.session.singleton.QueueSessionBeanRemote;
import ejb.session.stateless.AppointmentSessionBeanRemote;
import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanRemote;
import ejb.session.stateless.StaffSessionBeanRemote;
import entity.DoctorEntity;
import entity.DoctorsLeaveEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.exception.ClashWithAppointmentException;
import util.exception.DoubleLeaveRequestException;
import util.exception.LeaveToCloseInTimeException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author Niklas
 */
public class AdministrationModule {

    private PatientSessionBeanRemote patientSessionBeanRemote;
    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private AppointmentSessionBeanRemote appointmentSessionBeanRemote;
    StaffEntity staffEntity;
    Scanner sc;
    int response;

    public AdministrationModule() {
    }

    public AdministrationModule(StaffEntity staff, PatientSessionBeanRemote patientSessionBeanRemote, DoctorSessionBeanRemote doctorSessionBeanRemote, AppointmentSessionBeanRemote appointmentSessionBeanRemote) {
        this.patientSessionBeanRemote = patientSessionBeanRemote;
        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.appointmentSessionBeanRemote = appointmentSessionBeanRemote;
        sc = new Scanner(System.in);
        staffEntity = staff;
        while (true) {
            System.out.println("*** CARS :: Administraion operation **** \n ");
            System.out.println("1: Patient Management");
            System.out.println("2: Doctor Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back \n");
            System.out.print("> ");
            try {
                response = sc.nextInt();
                sc.nextLine();

                switch (response) {
                    case 1:
                        patientManagement();
                        break;
                    case 2:
                        doctorManagement();
                        break;
                    case 3:
                        staffManagement();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid input");
                }
            } catch (Exception e) {
                System.out.println("Incorrect input. Expected an integer.\n\n");
                this.sc = new Scanner(System.in);
            }
        }
    }

    private void staffManagement() {
        while (true) {
            StaffSessionBeanRemote staffSessionBeanRemote = lookupStaffSessionBeanRemote();
            System.out.println("*** CARS :: Administraion operation :: Staff Management **** \n ");
            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staff");
            System.out.println("6: Back \n");
            System.out.print("> ");
            try {
                response = sc.nextInt();
                sc.nextLine();

                switch (response) {
                    case 1:
                        registerNewStaff(staffSessionBeanRemote);
                        break;
                    case 2:
                        viewStaffDetails(staffSessionBeanRemote);
                        break;
                    case 3:
                        updateStaff(staffSessionBeanRemote);
                        break;
                    case 4:
                        deleteStaff(staffSessionBeanRemote);
                        break;
                    case 5:
                        viewAllStaff(staffSessionBeanRemote);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid input");
                }
            } catch (Exception e) {
                System.out.println("Incorrect input. Expected an integer.\n\n");
                this.sc = new Scanner(System.in);
            }
        }

    }

    private void doctorManagement() {
        while (true) {
            System.out.println("*** CARS :: Administraion operation :: Doctor Management **** \n ");
            System.out.println("1: Add Doctor");
            System.out.println("2: View Doctor Details");
            System.out.println("3: Update Doctor");
            System.out.println("4: Delete Doctor");
            System.out.println("5: View All Doctors");
            System.out.println("6: Leave Management");
            System.out.println("7: Back \n");
            System.out.print("> ");
            try {
                response = sc.nextInt();
                sc.nextLine();

                switch (response) {
                    case 1:
                        addDoctor();
                        break;
                    case 2:
                        viewDoctorDetails();
                        break;
                    case 3:
                        updateDoctor();
                        break;
                    case 4:
                        deleteDoctor();
                        break;
                    case 5:
                        viewAllDoctors();
                        break;
                    case 6:
                        leaveManagment();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid input");
                }
            } catch (Exception e) {
                System.out.println("Incorrect input. Expected an integer. \n\n");
                this.sc = new Scanner(System.in);
            }
        }
    }

    private void patientManagement() {
        while (true) {
            System.out.println("*** CARS :: Administraion operation :: Patient Management **** \n ");
            System.out.println("1: Add Patient");
            System.out.println("2: View Patient Details");
            System.out.println("3: Update Patient");
            System.out.println("4: Delete Patient");
            System.out.println("5: View All Patients");
            System.out.println("6: Back \n");
            System.out.print("> ");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    viewPatientDetails();
                    break;
                case 3:
                    updatePatient();
                    break;
                case 4:
                    deletePatient();
                    break;
                case 5:
                    viewAllPatients();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid input");
                    this.sc = new Scanner(System.in);
            }
        }
    }

    private void registerNewStaff(StaffSessionBeanRemote staffSessionBeanRemote) {

        System.out.println("*** CARS :: Registration operation :: Register new Staff**** \n ");
        System.out.print("Enter First Name> ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name> ");
        String lastName = sc.nextLine();
        System.out.print("Enter User Name> ");
        String userName = sc.nextLine();
        System.out.print("Enter Password> ");
        staffSessionBeanRemote.createStaffEntity(new StaffEntity(firstName, lastName, userName, sc.nextLine()));
    }

    private void viewStaffDetails(StaffSessionBeanRemote staffSessionBeanRemote) {

        System.out.println("*** CARS :: Registration operation :: View Staff Details**** \n ");
        System.out.print("Enter user name> ");
        StaffEntity staff = staffSessionBeanRemote.retrieveStaffEntityByUserName(sc.nextLine());

        System.out.println(staff.toString() + "\n");

        System.out.println(" Press enter to go back \n");
        System.out.print("> ");
        sc.nextLine();
    }

    private void updateStaff(StaffSessionBeanRemote staffSessionBeanRemote) {
        System.out.println("*** CARS :: Registration operation :: Update Staff**** \n ");
        System.out.print("Enter user name> ");
        StaffEntity staff = staffSessionBeanRemote.retrieveStaffEntityByUserName(sc.nextLine());
        if (staff.getUserName().equals(this.staffEntity.getUserName())) {
            System.out.println("You can't update the staff that is currently logged in!");
            return;
        }

        System.out.print("Enter New First Name> ");
        staff.setFirstName(sc.nextLine());
        System.out.print("Enter New Last Name> ");
        staff.setLastName(sc.nextLine());
        System.out.print("Enter New User Name> ");
        staff.setUserName(sc.nextLine());
        System.out.print("Enter New Password> ");
        staff.setPassword(sc.nextLine());

        staffSessionBeanRemote.updateStaffEntity(staff);
    }

    private void deleteStaff(StaffSessionBeanRemote staffSessionBeanRemote) {
        System.out.println("*** CARS :: Registration operation :: Delete Staff**** \n ");
        System.out.print("Enter user name> ");
        StaffEntity staff = staffSessionBeanRemote.retrieveStaffEntityByUserName(sc.nextLine());
        if (staff.getStaffId().equals(staffEntity.getStaffId())) {
            System.out.println("You can't remove the same profile as you are logged into!!");
            return;
        }
        staffSessionBeanRemote.deleteStaffEntity(staff.getStaffId());
    }

    private void viewAllStaff(StaffSessionBeanRemote staffSessionBeanRemote) {
        System.out.println("*** CARS :: Registration operation :: View All Staff**** \n ");

        List staffList = staffSessionBeanRemote.retrieveAllStaff();

        System.out.println("First Name | Last Name | Username | Password");
        for (Object obj : staffList) {
            StaffEntity my_staff = (StaffEntity) obj;
            System.out.println(my_staff.toString());
        }
        System.out.println("\n");
    }

    private void addDoctor() {
        System.out.println("*** CARS :: Administraion operation :: Doctor Management :: Add Doctor **** \n ");

        System.out.print("Enter First Name> ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name> ");
        String lastName = sc.nextLine();
        System.out.print("Enter Registration> ");
        String registration = sc.nextLine();
        System.out.print("Enter Qualifications> ");
        doctorSessionBeanRemote.createDoctorEntity(new DoctorEntity(firstName, lastName, registration, sc.nextLine()));
    }

    private void viewDoctorDetails() {
        System.out.println("*** CARS :: Administraion operation :: Doctor Management :: View Doctor Details **** \n ");

        System.out.print("Enter registration> ");
        DoctorEntity doctorEntity = doctorSessionBeanRemote.retrieveDoctorEntityByRegistration(sc.nextLine());

        System.out.println("First Name | Last Name | Registration | Qualifications");
        System.out.println(doctorEntity.toString() + "\n");

        System.out.println("Press enter to go back \n");
        System.out.print("> ");
        sc.nextLine();
    }

    private void updateDoctor() {
        System.out.println("*** CARS :: Administraion operation :: Doctor Management :: Update Doctor **** \n ");

        System.out.println("Enter Registration> ");
        DoctorEntity doc = doctorSessionBeanRemote.retrieveDoctorEntityByRegistration(sc.nextLine());

        System.out.print("Enter New First Name> ");
        doc.setFirstName(sc.nextLine());
        System.out.print("Enter New Last Name> ");
        doc.setLastName(sc.nextLine());
        System.out.print("Enter New Qualifications> ");
        doc.setQualifications(sc.nextLine());
        System.out.print("Enter New Registration> ");
        doc.setRegistration(sc.nextLine());
        doctorSessionBeanRemote.updateDoctorEntity(doc);
    }

    private void deleteDoctor() {
        System.out.println("*** CARS :: Administraion operation :: Doctor Management :: Delete Doctor **** \n ");
        System.out.println("Enter Registration> ");
        DoctorEntity doc;
        try {
            doc = doctorSessionBeanRemote.retrieveDoctorEntityByRegistration(sc.nextLine());
        } catch (Exception e) {
            System.out.println("The registration entered was incorrect");
            return;
        }
        if (appointmentSessionBeanRemote.retrieveDoctorAppointments(doc).size() > 0) {
            System.out.println("This doctor has associated apppointments. Remove these before removing the doctor!");
            return;
        }
        try {
            for (Object obj : doctorSessionBeanRemote.getLeavesForDoctor(doc)) {
                DoctorsLeaveEntity leave = (DoctorsLeaveEntity) obj;
                doctorSessionBeanRemote.deleteDoctorsLeaveEntity(leave.getDoctorsLeaveId());
            }
            doctorSessionBeanRemote.deleteDoctorEntity(doc.getDoctorId());
        } catch (Exception e) {
            System.out.println("Could not delete doctor");
            return;
        }
    }

    private void viewAllDoctors() {
        System.out.println("*** CARS :: Administraion operation :: Doctor Management :: View All Doctors **** \n ");

        List docs = doctorSessionBeanRemote.retrieveAllDoctors();
        System.out.println("First Name | Last Name | Registration | Qualifications");
        for (Object doctor : docs) {
            DoctorEntity my_doctor = (DoctorEntity) doctor;
            System.out.println(my_doctor.toString());
        }
        System.out.println("\n");

    }

    private void leaveManagment() {
        System.out.println("*** CARS :: Administraion operation :: Doctor Management :: Leave Management **** \n ");
        System.out.println("Enter Registration> ");
        DoctorEntity doc;
        try {
            doc = doctorSessionBeanRemote.retrieveDoctorEntityByRegistration(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Could not find a doctor with thet registration. \n\n");
            return;
        }
        System.out.println("Enter Requested Leave date in the format: yyyy-MM-dd> ");

        Date date;
        try {
            date = java.sql.Date.valueOf(sc.nextLine());
            doctorSessionBeanRemote.requestDoctorsLeave(date, doc.getDoctorId());
        } catch (LeaveToCloseInTimeException ex) {
            System.out.println("To close in time");
        } catch (DoubleLeaveRequestException | ClashWithAppointmentException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Incorrectly formatted date. \n\n");
        }
    }

    private void viewAllPatients() {
        System.out.println("*** CARS :: Administraion operation :: Patient Management :: View All Patients **** \n ");

        List pats = patientSessionBeanRemote.retrieveAllPatients();
        System.out.println("Identity Numberr | First Name | Last Name | Gender | Age | Phone | Address | Password");
        for (Object obj : pats) {
            PatientEntity my_patient = (PatientEntity) obj;
            System.out.println(my_patient.toString());
        }
        System.out.println("\n");

    }

    private void deletePatient() {
        try {
            System.out.println("*** CARS :: Administraion operation :: Patient Management :: Delete Patient **** \n ");
            System.out.println("Enter Identity Number> ");
            PatientEntity patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
            patientSessionBeanRemote.deletePatientEntity(patientEntity.getPatientId());
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void updatePatient() {
        PatientEntity patientEntity = null;

        System.out.println("*** CARS :: Administraion operation :: Patient Management :: Update Patient **** \n ");
        System.out.println("Enter Identity Number> ");
        try {
            patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
            System.out.print("Enter New Identity Number> ");
            patientEntity.setIdentityNumber(sc.nextLine());
            System.out.print("Enter New First Name> ");
            patientEntity.setFirstName(sc.nextLine());
            System.out.print("Enter Last Name> ");
            patientEntity.setLastName(sc.nextLine());
            System.out.print("Enter Gender> ");
            String gender = sc.nextLine();
            switch (gender) {
                case "M":
                case "m":
                    patientEntity.setGender(util.Enum.Gender.M);
                    break;
                case "F":
                case "f":
                    patientEntity.setGender(util.Enum.Gender.F);
                    break;
                default:
                    System.out.println("wrong gender input, if should either be 'M' of 'F'!");
                    return;
            }
            System.out.print("Enter New Age> ");
            patientEntity.setAge(sc.nextInt());
            sc.nextLine();
            System.out.print("Enter New Phone> ");
            patientEntity.setPhone(sc.nextLine());
            System.out.print("Enter Address> ");
            patientEntity.setAddress(sc.nextLine());
            System.out.print("Enter Password> ");
            patientEntity.setPassword(sc.nextLine());

            patientSessionBeanRemote.updatePatientEntity(patientEntity);
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void viewPatientDetails() {
        System.out.println("*** CARS :: Administraion operation :: Patient Management :: View Patient Details **** \n ");
        System.out.println("Enter Identity Number> ");
        PatientEntity patientEntity;
        try {
            patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
            System.out.println(patientEntity.toString());
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void addPatient() {

        String identityNumber;
        String firstName;
        String lastName;
        String gender;
        int age;
        String phone;
        String address;
        String password;

        System.out.println("*** CARS :: Administraion operation :: Patient Management :: Add Patient **** \n ");
        try {
            System.out.print("Enter Identity Number> ");
            identityNumber = sc.nextLine();
            System.out.print("Enter First Name> ");
            firstName = sc.nextLine();
            System.out.print("Enter Last Name> ");
            lastName = sc.nextLine();
            System.out.print("Enter Gender> ");
            gender = sc.nextLine();
            System.out.print("Enter Age> ");
            age = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Phone> ");
            phone = sc.nextLine();
            System.out.print("Enter Address> ");
            address = sc.nextLine();
            while (true) {
                System.out.print("Enter Password> ");
                password = sc.nextLine();
                if (password.matches("[0-9]+") && password.length() == 6) {
                    break;
                } else {
                    System.out.println("wrong password input, password has to be in 6 digits");
                }
            }
            if (gender.equals("M") || gender.equals("m")) {
                patientSessionBeanRemote.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.M, age, phone, address, password));
            } else if (gender.equals("F") || gender.equals("f")) {
                patientSessionBeanRemote.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.F, age, phone, address, password));
            } else {
                System.out.println("wrong gender input, if should either be 'M' of 'F'!");
            }
        } catch (Exception ex) {
            System.out.println("Please enter the particulars");
        }
    }

    private StaffSessionBeanRemote lookupStaffSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StaffSessionBeanRemote) c.lookup("java:comp/env/StaffSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private DoctorSessionBeanRemote lookupDoctorSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (DoctorSessionBeanRemote) c.lookup("java:comp/env/DoctorSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private PatientSessionBeanRemote lookupPatientSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (PatientSessionBeanRemote) c.lookup("java:comp/env/PatientSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
