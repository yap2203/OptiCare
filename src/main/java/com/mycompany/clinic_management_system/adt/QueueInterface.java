/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clinic_management_system.adt;

/**
 *
 * @author huany
 */
import java.util.Iterator;

public interface QueueInterface<T> {

    public Iterator<T> getIterator();

    public void enqueue(T newEntry);

    public T dequeue();

    public T getFront();

    public boolean isEmpty();

    public void clear();
    public Boolean remove(T queueDeleted);
}
