/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;


import dto.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelDB.UseriDB;


/**
 *
 * @author Agnes
 */
public class MainController {
    
    private static MainController singleton;
    private EntityManagerFactory emf;
    private CategoryDBJpaController categoryDBJpaController;
    private IngredientDBJpaController ingredientDBJpaController;
    private QuantityDBJpaController quantityDBJpaController;
    private RecipeDBJpaController recipeDBJpaController;
    private ShoppinglistDBJpaController shoppinglistDBJpaController;
    private UseriDBJpaController useriDBJpaController;
    
    
    
     private MainController(){
        emf = Persistence.createEntityManagerFactory("recipeWizardPU");
        categoryDBJpaController = new CategoryDBJpaController(emf);
        ingredientDBJpaController = new IngredientDBJpaController(emf);
        quantityDBJpaController = new QuantityDBJpaController(emf);
        recipeDBJpaController = new RecipeDBJpaController(emf);
        shoppinglistDBJpaController = new ShoppinglistDBJpaController(emf);
        useriDBJpaController = new UseriDBJpaController(emf);
       
       
    }
    
    
    
      public static MainController getInstance(){
        if(singleton == null)
            singleton= new MainController();
        return singleton;
    }
    
    
    public  boolean singUp(   String username, String password, String emailAddress){
       
       UseriDB   userDB = useriDBJpaController.getUserByUsername(username);
        
        if(userDB!=null){  //daca  exista deja un user inregistrat cu acest username
           System.out.println("User deja inregistrat");
           return false;
       } else
       {
        
        
            System.out.println("User neinregistrat");
             userDB = new UseriDB();
        
       
        userDB.setUsername(username);
        userDB.setPassword(password);
        userDB.setEmail(emailAddress);
        
        useriDBJpaController.create(userDB);
        return true;
        }
        
        //return false; //inseamna ca nu s-a putut efectua inregistrarea noului user   
    }
    
    
    
    
    //trebuie schimbat in User, nu UserDB, pentru ca Userul este folosit pentru a realiza transferul de informatie
    
    public User signIn(String username, String password){
        
        UseriDB userDB = useriDBJpaController.getUserByUsername(username);
        if(userDB!=null){
            
            //if(userDB.getPassword().equals(password)){
            System.out.println("din main controller:"+userDB.getUsername()+" "+userDB.getPassword());
            User u = new User();
            u.setUsername(userDB.getUsername());
            u.setId(userDB.getId());
            u.setPassword(userDB.getPassword());
            u.setEmail(userDB.getEmail());
                return u;
           
        }
       return null;
    }
    
   
    public ArrayList<User> getAllUsers(){
        User u = new User();
        List<UseriDB> listaDB = useriDBJpaController.findUseriDBEntities();
        ArrayList<User> lista = new ArrayList<>();
        
        for(UseriDB userDB : listaDB)
        {
            u.setId(userDB.getId());
            u.setUsername(userDB.getUsername());
            u.setPassword(userDB.getPassword());
            u.setEmail(userDB.getEmail());
            lista.add(u);
        }
        
        return lista;
                
    }
    
}
