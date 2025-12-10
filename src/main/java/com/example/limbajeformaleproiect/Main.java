package com.example.limbajeformaleproiect;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {

    private TextArea zonaIntrare; //zona de introducere a datelor
    private TextArea zonaIesire; //zona de iesire a datelor

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage fereastraPrincipala) {
        fereastraPrincipala.setTitle("Citire gramatica"); //titlu fereastra

        // titlu si reguli
        Label labelTitlu = new Label("Citirea unei gramatici independente de context (sir de caractere)");
        labelTitlu.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        String textReguli =
                "Reguli de citire:\n" +
                        "1. Primul simbol din prima producție reprezintă axioma (simbolul de start) S.\n" +
                        "2. Simbolul de separare dintre producții este '$'.\n" +
                        "3. Simbolurile neterminale sunt litere mari (A..Z).\n" +
                        "4. Simbolurile terminale sunt litere mici (a..z).\n" +
                        "5. Secvența vidă este '@' (afișată ca λ).\n" +
                        "6. Simbolul care marchează sfârșitul gramaticii este '&'.\n\n";

        TextArea zonaReguli = new TextArea(textReguli);
        zonaReguli.setEditable(false); //este read only
        zonaReguli.setWrapText(true);
        zonaReguli.setStyle("-fx-control-inner-background: #f4f4f4;");
        zonaReguli.setPrefRowCount(7); //dimensiunea aproximativa

        // zona de introducere a datelor
        Label labelIntrare = new Label("Introduceti sirul gramaticii independente de context (sir de caractere): ");
        zonaIntrare = new TextArea();
        zonaIntrare.setPromptText("Ex: SAB$AaA$A@$Ba&");
        zonaIntrare.setWrapText(true);
        zonaIntrare.setPrefRowCount(3);

        // Butoane
        Button btnIncarcare = new Button("Incarca din fisier...");
        btnIncarcare.setOnAction(e -> incarcareFisier(fereastraPrincipala));

        Button btnTransformare = new Button("Transforma gramatica");
        btnTransformare.setOnAction(e -> transformareGramatica());

        //o sa asezam intr un box orizontal butoanele
        HBox zonaButoane = new HBox(10, btnIncarcare, btnTransformare);
        zonaButoane.setPadding(new Insets(5, 0, 5, 0));

        // Zona de iesire a datelor
        Label etichetaIesire = new Label("Rezultate (V_N, V_T, S, P):");
        zonaIesire = new TextArea();
        zonaIesire.setEditable(false);
        zonaIesire.setWrapText(true);
        zonaIesire.setPrefRowCount(8);
        zonaIesire.setStyle("-fx-control-inner-background: #eef5ff;");

        //asezam toate elementele intr un container (mai putin zona de iesire)
        VBox container = new VBox(10, labelTitlu, zonaReguli, labelIntrare, zonaIntrare, zonaButoane, etichetaIesire);
        container.setPadding(new Insets(10));

        BorderPane radacina = new BorderPane();
        radacina.setTop(container); //adaugam deasupra containerul
        radacina.setCenter(zonaIesire); //adaugam zona de iesire central
        BorderPane.setMargin(zonaIesire, new Insets(0, 10, 10, 10));
        VBox.setVgrow(zonaIntrare, Priority.ALWAYS);
        VBox.setVgrow(zonaIesire, Priority.ALWAYS);

        Scene scena = new Scene(radacina, 800, 600);
        fereastraPrincipala.setScene(scena);
        fereastraPrincipala.show();
    }

    //functie pentru incarcarea fisierelor
    private void incarcareFisier(Stage fereastra) {
        FileChooser selector = new FileChooser();
        selector.setTitle("Alege fisierul cu gramatica");
        //specificam extensile fisierelor pe care le vrem
        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files", "*.txt", "*.*"));
        File fisierSelectat = selector.showOpenDialog(fereastra); //creem un obiect
        if (fisierSelectat == null) { //daca nu am selectat un fisier, iesim din fereastra de a alege unul
            return;
        }

        //construim sirul de carcatere colectand datele din fisier
        StringBuilder sir = new StringBuilder();
        //creem un cititor de date pentru fisierul ales
        try (BufferedReader cititor = new BufferedReader(new FileReader(fisierSelectat))) {
            String linie; //variabila tmp in care punem fiecare linie citita din fisier
            boolean primaLinie = true;
            //citim datele din fisier, linie cu linie
            while ((linie = cititor.readLine()) != null) {
                if (!primaLinie) sir.append(" "); //adaugam " " doar intre linii, nu inainte de prima linie
                sir.append(linie.trim());
                primaLinie = false;
            }
        } catch (IOException exceptieIO) {
            afisareErori("Eroare la citirea fișierului: " + exceptieIO.getMessage());
            return;
        }
        String continutFisier = sir.toString();
        if (continutFisier.trim().isEmpty()) {
            afisareErori("Eroare: fișierul citit este gol.");
            zonaIntrare.clear();
            return;
        }
        zonaIntrare.setText(continutFisier);
    }

    private void transformareGramatica() {
        String textGramatica = zonaIntrare.getText();
        if (textGramatica == null || textGramatica.trim().isEmpty()) {
            afisareErori("Introduceti sirul gramaticii!.");
            return;
        }
        try {
            //creem un obiect gramatica, asupra caruia aplicam functia de transformare
            Gramatica gramatica = Gramatica.transformare(textGramatica);
            zonaIesire.setText(gramatica.Print());
        } catch (IllegalArgumentException exceptii) {
            afisareErori("Eroare la transformare: " + exceptii.getMessage());
        }
    }

    private void afisareErori(String mesaj) {
        Alert fereastraAlerta = new Alert(Alert.AlertType.ERROR);
        fereastraAlerta.setTitle("Eroare");
        fereastraAlerta.setHeaderText(null);
        fereastraAlerta.setContentText(mesaj);
        fereastraAlerta.showAndWait();
    }

}
