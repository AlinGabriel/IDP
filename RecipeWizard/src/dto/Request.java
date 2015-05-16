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
    public static final int GET_PETS_BY_BREED=3;
    public static final int GET_ALL_PETS=4;
    public static final int GET_ORDER_BY_USER=5;
    public static final int RESPONSE_SUCCESS=6;
    public static final int RESPONSE_FAIL=7;
    public static final int REQUEST_GET_DOGS=8;
    public static final int REQUEST_GET_CATS=9;
    public static final int REQUEST_GET_BIRDS=10;
    public static final int REQUEST_GET=11;
    public static final int REQUEST_SEND_ORDER=12;
    public static final int PET_ORDER_SENT_SUCCESSFULLY=13;
    public static final int PET_ORDER_FAILED=14;
    public static final int REQUEST_REMOVE_SOLDOUT_PET=15;



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
