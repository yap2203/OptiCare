/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.utility;

import com.mycompany.clinic_management_system.adt.ArrayStack;
import com.mycompany.clinic_management_system.adt.StackInterface;

/**
 *
 * @author huany
 */
public class Navigation {

    public static StackInterface<Runnable> stack = new ArrayStack<>();

    public static void goBack() {
        if (!stack.isEmpty()) {
            stack.pop();
            if (!stack.isEmpty()) {
                stack.peek().run();
            } else {
                System.out.println("No previous menu.");
            }
        }
    }
}
