/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clinic_management_system.adt;

/**
 *
 * @author huany
 * @param <T>
 */
public interface ListInterface<T> {
    
    public boolean add(T newEntry);
    public boolean add(int newPosition,T newEntry);
    public T remove(int removePosition);
    public void clear();
    public boolean replace(int replacePosition,T newEntry);
    public T getEntry(int givenPosition);
    public int getNumberOfEntries();
    public boolean contains(T anEntry);
    public boolean isEmpty();
    public boolean isFull();
    public void sort(java.util.Comparator<T> comparator);
    
}
