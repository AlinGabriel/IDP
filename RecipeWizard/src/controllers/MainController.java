package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import android.util.Log;
import android.widget.Toast;
import dto.Request;
import dto.User;




public class MainController {

    private static MainController singleton = null;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private MainController(){
        try {
            socket = new Socket("192.168.0.104", 4321); //se modifica de fiecare data. Verifica din nou cand prezinti:)
            out = new ObjectOutputStream(socket.getOutputStream());
            in=new ObjectInputStream(socket.getInputStream());

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static MainController getInstance(){
        if(singleton==null)
            singleton= new MainController();
        return singleton;
    }


    public boolean signUp( String username, String password, String emailAddress){
        Request request = new Request(Request.REQUEST_SIGN_UP);

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(emailAddress);


        try {
            out.writeObject(request);
            out.writeObject(user);


            String response = (String) in.readObject();
            if(response.equals("ResponseSucces")) return true;

            //if(response.getCode() == Request.RESPONSE_SUCCESS) return true;
            //adica nu s-a putut efectua inregistrarea userului in baza de date

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }


    public User SignIn(String username, String password){

        Request message = new Request(Request.REQUEST_SIGN_IN);
        try {
            out.writeObject(message);
            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            out.writeObject(u);
            //Toast.makeText(WelcomeActivity.welcomeActivity, "A ajuns in main controller 1", Toast.LENGTH_LONG).show();


            User user = (User) in.readObject();

            return user;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;

    }

}
