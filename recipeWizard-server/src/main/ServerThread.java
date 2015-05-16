/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import controllers.MainController;
import dto.Request;
import dto.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agnes
 */
public class ServerThread  extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
   
    
    
    public ServerThread(Socket socket){
        try {
            this.socket=socket;
            out= new ObjectOutputStream(socket.getOutputStream());
            in= new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        
        try{
        while(true){
            
            //String response = (String) in.readObject();
            Request request= (Request) in.readObject();
            
            switch(request.getCode()){
                
                //aici vor  fi toate cererile
                case Request.REQUEST_SIGN_UP:{
                    User user = (User) in.readObject();
                    
                    if(!user.equals(null)){  //daca a primit ceva prin flux
                    System.out.println("S-a primit userul");
                    System.out.println(user);
                    
                    boolean resp= MainController.getInstance().singUp( user.getEmail(),user.getUsername(), user.getPassword());
                    
                    if(resp)
                    {
                        System.out.println("S-a facut inregistrarea in baza de date!! :DD");
                        out.writeObject(new String("ResponseSucces"));
                    }
                    else
                        out.writeObject( new String("ResponseFail"));
                    }
                    else
                        System.out.println("User null");
                } break;
                
                case Request.REQUEST_SIGN_IN:
                {
                    User user = (User) in.readObject();
                    if(user!=null) System.out.println("userul  primit de la android este: "+user.toString());
                    User user2 = MainController.getInstance().signIn(user.getUsername(), user.getPassword());
                    out.writeObject(user2);
                    System.out.println("User2 trimis  este: "+user2);
                    System.out.println("S-a trimis userul  cerut la login( poate nul?!)") ;
                }break;
                    
               
                    
                case Request.REQUEST_GET_USERS:{
                     System.out.println("Se trimit userii ...");
                     out.writeObject(MainController.getInstance().getAllUsers());
                } break;  
                
               
         }
        }
       }catch(Exception ex){
           ex.printStackTrace();;
       }
    }
    
}
