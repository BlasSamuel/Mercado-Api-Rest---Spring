package com.morelos.mercado.domain;

import java.util.ArrayList;

public class JsonResponse {
    public boolean success;
    public ArrayList data;
    public String error;

    public JsonResponse(boolean success, ArrayList data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;


    }

}
