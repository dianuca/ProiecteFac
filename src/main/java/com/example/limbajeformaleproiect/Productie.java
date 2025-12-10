package com.example.limbajeformaleproiect;

public class Productie {

    private final char st;   // partea stanga a productiei (neterminal)
    private final String dr; // partea dreapta a productiei

    //constructor
    public Productie(char st, String dr) {
        this.st = st;
        this.dr = dr;
    }

    //metode get pt stanga si dreapta productiei
    public char getSt() {
        return st;
    }

    public String getDr() {
        return dr;
    }

    @Override
    //afisare productie
    public String toString() {
        // afisam landa in loc de @
        String dreaptaReplace = dr.replace("@", "λ");
        return st + " -> " + dreaptaReplace;
    }
}
