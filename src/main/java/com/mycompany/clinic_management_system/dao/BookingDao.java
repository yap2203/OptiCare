/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.*;
import com.mycompany.clinic_management_system.entity.Booking;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author huany
 */
public class BookingDao {

    private String fileName = "data/booking.dat";

    public void saveFile(ListInterface<Booking> booking) {
        File file = new File(fileName);
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(booking);
            ooStream.close();
        } catch (IOException e) {
            System.out.println("Cannot save to file" + e.getMessage());
        }
    }

    public ListInterface<Booking> loadFromFile() {
        File file = new File(fileName);
        ListInterface<Booking> booking = new ArrayList<>();
        try {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
            booking = (ListInterface<Booking>) (oiStream.readObject());
            oiStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("No such file");

        } catch (IOException ex) {
            System.out.println("Cannot read from file");
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
        } finally {
            return booking;
        }
    }

}
