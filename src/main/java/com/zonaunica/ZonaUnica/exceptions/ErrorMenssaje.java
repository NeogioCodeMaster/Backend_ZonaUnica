package com.zonaunica.ZonaUnica.exceptions;

public class ErrorMenssaje {
    
    private String type;
    private String menssage;

    public ErrorMenssaje(String type, String menssage) {
        this.type = type;
        this.menssage = menssage;
    }

    public ErrorMenssaje(){
        this.type ="";
        this.menssage ="";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenssage() {
        return menssage;
    }

    public void setMenssage(String menssage) {
        this.menssage = menssage;
    }
}
