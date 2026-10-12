/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.adt;

import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author huany
 */
public class LinkedQueue<T> implements QueueInterface<T>, Serializable {

    private Node firstNode; // references node at front of queue
    private Node lastNode;  // references node at back of queue

    public LinkedQueue() {
        firstNode = null;
        lastNode = null;
    }

    @Override
    public T getFront() {
        T front = null;

        if (!isEmpty()) {
            front = firstNode.data;
        }

        return front;
    }

    @Override
    public T dequeue() {
        T front = null;

        if (!isEmpty()) {
            front = firstNode.data;
            firstNode = firstNode.next;

            if (firstNode == null) {
                lastNode = null;
            }
        }

        return front;
    } // end dequeue

    @Override
    public boolean isEmpty() {
        return (firstNode == null) && (lastNode == null);
    }

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
    }

    @Override
    public Iterator<T> getIterator() {
        return new LinkedQueueIterator();
    }

    @Override
    public void enqueue(T newEntry) {
        Node newNode = new Node(newEntry, null);

        if (isEmpty()) {
            firstNode = newNode;

        } else {
            lastNode.next = newNode;
        }

        lastNode = newNode;
    }

    public Boolean remove(T queueDeleted) {
        if (firstNode == null) {
            return false;
        }

        if (firstNode.data.equals(queueDeleted)) {
            firstNode = firstNode.next;
            if (firstNode == null) {
                lastNode = null;
            }
            return true;
        }
        Node currentNode = firstNode;
        while (currentNode.next != null) {
            if (currentNode.next.data == queueDeleted) {
                currentNode.next = currentNode.next.next;
                if (currentNode.next == null) {
                    lastNode = currentNode;
                }
                return true;
            }
            currentNode = currentNode.next;

        }
        return false;
    }

    private class LinkedQueueIterator implements Iterator<T> {

        private Node currentNode;

        public LinkedQueueIterator() {
            currentNode = firstNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T returnData = currentNode.data;
                currentNode = currentNode.next;
                return returnData;
            } else {
                return null;
            }
        }
    }

    private class Node implements Serializable {

        private T data;
        private Node next;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }

        private Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

}
