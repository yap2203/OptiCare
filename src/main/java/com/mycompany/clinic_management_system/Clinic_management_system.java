package com.mycompany.clinic_management_system;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


import com.mycompany.clinic_management_system.boudary.MainUi;
import com.mycompany.clinic_management_system.dao.DoctorScheduleDao;

/**
 *
 * @author huany
 */
public class Clinic_management_system {

    public static void main(String[] args) {
        DoctorScheduleDao scheduleDao = new DoctorScheduleDao();
        scheduleDao.validateScheduleWeek();  
        scheduleDao.updateScheduleHeaderDate(); 
        MainUi.mainUI();
    }
}
