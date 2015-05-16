/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agnesss
 */
public class Main {

     private static ServerSocket ss;
    
    public static void main(String[] args) {
        try {
           
            ss = new ServerSocket(4321);
            System.out.println("Waiting for client...");
            while(true){
            
            new ServerThread(ss.accept()).start();
            
            System.out.println("Got connection");
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
