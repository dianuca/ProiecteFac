package com.example.limbajeformaleproiect;

public class Gramatica {

    // multimea neterminalelor (fara simbolul de start S)
    private char[] neterminale = new char[50];
    private int nrNeterminale = 0;

    // multimea terminalelor
    private char[] terminale = new char[50];
    private int nrTerminale = 0;

    // lista productiilor
    private Productie[] productii = new Productie[100];
    private int nrProductii = 0;

    // simbolul de start (axioma)
    private char simbolStart;

    //constructor cu parametrii
    public Gramatica(char simbolStart, char[] neterminale, int nrNeterminale,
                   char[] terminale,   int nrTerminale,
                   Productie[] productii, int nrProductii) {

        this.simbolStart = simbolStart;
        this.neterminale = neterminale;
        this.nrNeterminale = nrNeterminale;
        this.terminale = terminale;
        this.nrTerminale = nrTerminale;
        this.productii = productii;
        this.nrProductii = nrProductii;
    }

    public static Gramatica transformare(String textInitial) {

        if (textInitial == null) {
            throw new IllegalArgumentException("Sirul gramaticii este null.");
        }

        // eliminam spatiile
        String text= textInitial.replaceAll("\\s+", "");
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Sirul gramaticii este gol.");
        }

        //tratam exceptiile
        int pozitieAmp = GramaticaExceptii.verificareAmp(text);
        String textFaraAmp = text.substring(0, pozitieAmp);
        if (textFaraAmp.isEmpty()) {
            throw new IllegalArgumentException("Nu exista productii inainte de simbolul '&'.");
        }

        //Sirul trebuie sa inceapa cu 'S'
        GramaticaExceptii.verificareAxioma(text);

        // Caractere permise: litere + $, @, &
        GramaticaExceptii.verificareCaractere(text);

        // separam productiile inainte de caracterul $
        String[] vectorProductii = textFaraAmp.split("\\$",-1);
        // -1 => tine cont si de productii goale (pentru exceptia 2)

        if (vectorProductii.length == 0) {
            throw new IllegalArgumentException("Nu s-au putut separa productiile cu '$'.");
        }

        //productie goala intre doi $
        GramaticaExceptii.verificareProductieGoala(vectorProductii);

        // Prima productie determina simbolul de start
        char simbolStart = vectorProductii[0].charAt(0);

        if (!Character.isUpperCase(simbolStart)) {
            throw new IllegalArgumentException(
                    "Prima productie nu incepe cu un neterminal valid (litera mare).");
        }

        char[] neterminale = new char[50]; //vector char in care stocam neterminalele
        int nrNeterminale = 0;
        char[] terminale = new char[50]; //vector char in care stocam terminalele
        int nrTerminale = 0;
        //intitalizam obictele de tip productie (un vector in care imi vor fi stocare toare productiile)
        Productie[] productii = new Productie[100];
        int nrProductii = 0;
        // Parcurgem toate productiile
        for (int i = 0; i < vectorProductii.length; i++) {
            String productieText = vectorProductii[i]; //luam productiile pe rand
            // partea stanga a productiei
            char simbolStanga = productieText.charAt(0);
            if (!Character.isUpperCase(simbolStanga)) { //verificam daca este caracter mare (terminal)
                throw new IllegalArgumentException(
                        "Eroare la productia " + (i + 1) + ": dupa '$' trebuie sa urmeze un neterminal (litera mare).");
            }
            // partea dreapta a productiei
            String parteDreapta = productieText.substring(1); //substragem sirul de la pozitia 1 (dupa terminal)
            if (parteDreapta.isEmpty()) {
                throw new IllegalArgumentException(
                        "Exisa o productie formata doar din neterminal.");
            }
            // verificam daca multimea vida este singura
            if (parteDreapta.contains("@") && !parteDreapta.equals("@")) {
                throw new IllegalArgumentException(
                        "Multimea vida '@' nu poate fi combinata cu alte simboluri in productia " + simbolStanga + ".");
            }
            //verificam daca exista productii identice
            if (i > 0){
                for (int j = 0; j < i; j++) {
                    if (simbolStanga == productii[j].getSt() && parteDreapta.equals(productii[j].getDr())){
                        throw new IllegalArgumentException("Exista doua productii identice!");
                    }
                }
            }
            // adaugam productia formatata in vectorul de tip productie initializat la inceput de for
            productii[nrProductii++] = new Productie(simbolStanga, parteDreapta);

            // adaugam neterminalul din stanga daca nu este simbolul de start
            if (simbolStanga != simbolStart) {
                if (!contineCaracter(neterminale, nrNeterminale, simbolStanga)) { //verificam daca caracterul mai exista in vector
                    neterminale[nrNeterminale++] = simbolStanga;
                }
            }

            // parcurgem partea dreapta a productiei
            for (int j = 0; j < parteDreapta.length(); j++) {
                char simbol = parteDreapta.charAt(j);
                if (simbol == '@' && !contineCaracter(terminale, nrTerminale, simbol)) {// multimea vida, nu se adauga nicaieri
                    terminale[nrTerminale++] = simbol;
                }
                //verificam daca caracterul mai exista in vectorul de neterminale, respectiv terminale
                if (Character.isUpperCase(simbol)) { // neterminal in partea dreapta
                    if (simbol != simbolStart &&
                            !contineCaracter(neterminale, nrNeterminale, simbol)) {
                        neterminale[nrNeterminale++] = simbol;
                    }
                } else if (Character.isLowerCase(simbol)) { // terminal in partea dreapta
                    if (!contineCaracter(terminale, nrTerminale, simbol)) {
                        terminale[nrTerminale++] = simbol;
                    }
                }
            }
        }

        // construim obiectul Gramatica pe baza datelor colectate
        return new Gramatica(simbolStart,
                neterminale, nrNeterminale,
                terminale,   nrTerminale,
                productii,   nrProductii);
    }

    //Afisarea gramaticii
    public String Print() {
        StringBuilder sb = new StringBuilder();

        sb.append("Simbol de start = ").append(simbolStart).append("\n\n");

        // V_N
        sb.append("V_N = {");
        sb.append(simbolStart);
        sb.append(", ");
        for (int i = 0; i < nrNeterminale; i++) {
            sb.append(neterminale[i]);
            if (i < nrNeterminale - 1) sb.append(", ");
        }
        sb.append("}\n");

        // V_T
        sb.append("V_T = {");
        for (int i = 0; i < nrTerminale; i++) {
            if (terminale[i] == '@') {
                sb.append("λ");
            }
            else
                sb.append(terminale[i]);
            if (i < nrTerminale - 1) sb.append(", ");
        }
        sb.append("}\n\n");

        // P
        sb.append("P = {");
        for (int i = 0; i < nrProductii; i++) {
            sb.append("   ").append(productii[i]); //facand append, se apeleaza automat functia de afisare a productiei i
            if (i < nrProductii - 1) sb.append(" ;");
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String toString() {
        return Print();
    }

    private static boolean contineCaracter(char[] vector, int lungime, char c) {
        for (int i = 0; i < lungime; i++) {
            if (vector[i] == c) {
                return true;
            }
        }
        return false;
    }
}
