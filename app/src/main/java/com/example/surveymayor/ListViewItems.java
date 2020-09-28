package com.example.surveymayor;

public class ListViewItems {
    private String titulo1;
    private String titulo2;
    private String titulo3;
    private String titulo4;

    public ListViewItems(String titulo1, String titulo2, String titulo3, String titulo4) {
        this.titulo1 = titulo1;
        this.titulo2 = titulo2;
        this.titulo3 = titulo3;
        this.titulo4 = titulo4;
    }

    public String getTitulo4() {
        return titulo4;
    }

    public void setTitulo4(String titulo4) {
        this.titulo4 = titulo4;
    }



    public ListViewItems(String titulo1, String titulo2, String titulo3) {
        this.titulo1 = titulo1;
        this.titulo2 = titulo2;
        this.titulo3 = titulo3;
    }

    public ListViewItems(String titulo1) {
        this.titulo1 = titulo1;
    }

    public ListViewItems(String titulo1, String titulo2) {
        this.titulo1 = titulo1;
        this.titulo2 = titulo2;
    }

    public ListViewItems() {
    }

    public String getTitulo1() {
        return titulo1;
    }

    public void setTitulo1(String titulo1) {
        this.titulo1 = titulo1;
    }

    public String getTitulo2() {
        return titulo2;
    }

    public void setTitulo2(String titulo2) {
        this.titulo2 = titulo2;
    }

    public String getTitulo3() {
        return titulo3;
    }

    public void setTitulo3(String titulo3) {
        this.titulo3 = titulo3;
    }

}
