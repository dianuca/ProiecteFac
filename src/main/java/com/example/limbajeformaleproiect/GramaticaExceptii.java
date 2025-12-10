package com.example.limbajeformaleproiect;

public class GramaticaExceptii {

    public static int verificareAmp(String text) {

        int primaPozitie = text.indexOf('&');
        int ultimaPozitie = text.lastIndexOf('&');

        // 1. lipseste simbolul de terminare
        if (primaPozitie == -1) {
            throw new IllegalArgumentException("Lipseste simbolul de terminare '&'.");
        }

        // 2. apare de mai multe ori
        if (primaPozitie != ultimaPozitie) {
            throw new IllegalArgumentException("Simbolul '&' poate aparea o singura data.");
        }

        // 3. '&' trebuie sa fie ultimul caracter
        if (primaPozitie != text.length() - 1) {
            throw new IllegalArgumentException("Simbolul '&' trebuie sa fie ultimul caracter din sir.");
        }

        return primaPozitie; // pozitia unde se afla '&'
    }

    public static void verificareAxioma(String text){
        if (text.charAt(0) != 'S') {
            throw new IllegalArgumentException("Sirul trebuie sa inceapa cu axioma 'S'.");
        }
    }

    public static void verificareCaractere(String text){
        // Caractere permise: litere + $, @, &
        for (char c : text.toCharArray()) {
            if (!Character.isLetter(c) && c != '$' && c != '@' && c != '&') {
                throw new IllegalArgumentException(
                        "Caracter nepermis in sir: '" + c + "'.");
            }
        }
    }

    public static void verificareProductieGoala(String[] vectorProductii ){
        for (int i = 0; i < vectorProductii.length; i++) {
            if (vectorProductii[i].isEmpty()) {
                throw new IllegalArgumentException(
                        "Eroare: exista o productie vida intre doi operatori '$' (pozitia " + i + ").");
            }
        }
    }
}
