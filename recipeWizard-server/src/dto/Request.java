/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto;

import java.io.Serializable;

/**
 *
 * @author Agnes
 */
public class Request implements  Serializable{
    private int code;
    private static final long serialVersionUID = 1L;
    public static final int REQUEST_SIGN_IN=1;
    public static final int REQUEST_SIGN_UP=2;
    public static final int REQUEST_GET_INFO=3;
    public static final int REQUEST_GET_USERS=4;
  
    

    
    public Request(){
        
    }
    public Request(int code) {
        this.code = code;
    }
    

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
    
    
}
