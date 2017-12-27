package com.apps.fajar.clothpickerapp.Model;

/**
 * Created by Fajar Khan on 28-Sep-17.
 */

public class model_cloths {

    //cloths
    private String Pants;
    private String Shirts;

    //constructor
    public model_cloths(String _Shirts, String _Pants) {
        this.Pants = _Shirts;
        this.Shirts = _Pants;
    }

    //getter
    public String getPants() {
        return Pants;
    }

    public String getShirts() {
        return Shirts;
    }
}
