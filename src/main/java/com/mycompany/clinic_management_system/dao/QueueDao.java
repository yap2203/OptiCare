/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.*;
import com.mycompany.clinic_management_system.entity.AppointmentQueue;
import java.io.*;
import java.util.Iterator;

/**
 *
 * @author huany
 */
public class QueueDao {

    private String fileName = "data/queue.dat";

    public void saveFile(QueueInterface<AppointmentQueue> queue) {
        File file = new File(fileName);
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(queue);
            ooStream.close();
        } catch (IOException e) {
            System.out.println("Cannot save to file" + e.getMessage());
        }
    }

    public QueueInterface<AppointmentQueue> loadFromFile() {
        File file = new File(fileName);
        QueueInterface<AppointmentQueue> queue = new LinkedQueue<>();
        try {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
            queue = (LinkedQueue<AppointmentQueue>) (oiStream.readObject());
            oiStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("No such file");

        } catch (IOException ex) {
            System.out.println("Cannot read from file");
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
        } finally {
            return queue;
        }
    }

}
