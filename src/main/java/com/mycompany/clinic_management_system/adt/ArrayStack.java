/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.adt;

/**
 *
 * @author huany
 * @param <T>
 */
public class ArrayStack<T> implements StackInterface<T> {

    private T[] array;
    private int topIndex; // index of top entry
    private static final int DEFAULT_CAPACITY = 50;

    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayStack(int initialCapacity) {
        array = (T[]) new Object[initialCapacity];
        topIndex = -1;
    }

    @Override
    public void push(T newEntry) {
        topIndex++;

        if (topIndex < array.length) {
            array[topIndex] = newEntry;
        }
    }

    @Override
    public T peek() {
        T top = null;

        if (!isEmpty()) {
            top = array[topIndex];
        }

        return top;
    }

    @Override
    public T pop() {
        T top = null;
        if (!isEmpty()) {
            top = array[topIndex];
            array[topIndex] = null;
            topIndex--;

        } // end if

        return top;
    }

    @Override
    public boolean isEmpty() {
        return topIndex < 0;
    }

    @Override
    public void clear() {
        topIndex = -1;
    }
}
