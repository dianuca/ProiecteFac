
## 1. Structura proiectului

- **Main.java**
    - gestioneaza interfata,
    - citeste sirul din text sau fisier,
    - "capteaza" toate exceptiile si afiseaza mesaje.

- **Gramatica.java**
    - contine structura gramaticii,
    - implementeaza metoda `transformare(String)` care valideaza si construieste gramatica,
    - arunca exceptii pentru orice incalcare a regulilor.

- **GramaticaExceptii.java**
    - contine functii auxiliare de validare,
    - verifica simbolul &, axioma, caracterele permise, productiile goale etc.

- **Productie.java**
    - clasa pentru o productie de forma `A -> α`,
    - pastreaza partea stanga (neterminal) si partea dreapta (sir de simboluri),
    - trateaza reprezentarea multimii vide (@).

## 2. Verificari pas cu pas

1. `Main.java` citeste sirul si verifica:
    - daca fisierul este gol
    - daca exista text introdus
    - daca citirea a reusit

2. `Gramatica.transformare`:
    - elimina spatiile
    - verifica simbolul `&`
    - separa productiile cu `$`
    - verifica axioma
    - proceseaza fiecare productie

3. `GramaticaExceptii` se ocupa de:
    - lipsa lui `&`
    - mai multe simboluri `&`
    - `&` nefiind ultimul caracter
    - caractere nepermise
    - productii vide intre doua `$`
## 3. Lista completa a exceptiilor tratate

### Exceptii la nivel de fisier 
- Fisierul nu poate fi citit
- Fisierul este gol
- Sirul gramaticii nu este introdus
- Exceptii de transformare (mesajele din Gramatica.java)

### Exceptii la nivel de sir de caractere introdus de la tastatura/fisier
- Sir null
- Sir gol dupa eliminarea spatiilor
- Lipseste simbolul final `&`
- `&` apare de mai multe ori
- `&` nu este ultimul caracter
- Nu exista productii inainte de `&`
- Productiile nu pot fi separate cu `$`
- Axioma nu este `S`
- Exista caractere nepermise
- Exista productie goala intre doua `$`

### Exceptii la nivel de productie (dupa verificarea exceptiilor la nivel de sir de carcatere)
- Prima productie nu incepe cu un neterminal
- Dupa `$` nu urmeaza un neterminal
- Productie formata doar din neterminal (fara partea dreapta)
- Multimea vida `@` combinata cu alte simboluri (invalid)
- Productii identice



