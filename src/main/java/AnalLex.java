/**
 * @author Ahmed Khoumsi
 */

/**Cette classe effectue l'analyse lexicale*/
public class AnalLex {
    /**Chaine à analiser*/
    String _aAnaliser;
    /**Longueur de la chaine à analiser*/
    int _longueur;
    /**État de l'AEF*/
    int _etat;
    /**caractère en cours de lecture*/
    int _positionLecture;

    /**Constructeur pour l'initialisation d'attribut(s) */
    public AnalLex(String aAnaliser) {
        _aAnaliser = aAnaliser;
        if (aAnaliser != null) {
            _aAnaliser = _aAnaliser.replace(" ", "");
            _longueur = _aAnaliser.length();
        } else
            _aAnaliser = "";
        _etat = 0;
        _positionLecture = 0;
    }

    /**Vérifie si il reste des caractère à analiser*/
    public boolean resteTerminal() {
        return _longueur != _positionLecture;
    }

    /**Retourne une sous chaine jusqu'à la position de lecture
     * pour la génération d'erreur*/
    public String stringErreur() {
        return _aAnaliser.substring(0, _positionLecture);
    }

    /**Retourne une chaine avec une flèche pointant la position de lecture
     * pour la génération d'erreur*/
    public String stringFleche() {
        String toReturn = "\n";
        for (int i = 0; i < _positionLecture - 1; ++i) {
            toReturn += " ";
        }
        toReturn += "^";
        return toReturn;
    }

    /**Génération d'exceptions (erreurs)*/
    private void throwError(String message) {
        _positionLecture = _longueur;
        throw new IllegalArgumentException(message);
    }

    /**prochainTerminal() retourne le prochain terminal
     * Cette methode est une implementation d'un AEF */
    public Terminal prochainTerminal() {
        int positionInit = _positionLecture;
        while (_positionLecture != _longueur) {
            char caractere = _aAnaliser.charAt(_positionLecture);
            switch (caractere) {
                case '(':
                    return caseOpperateur('(', positionInit);
                case ')':
                    return caseOpperateur(')', positionInit);
                case '+':
                    return caseOpperateur('+', positionInit);
                case '-':
                    return caseOpperateur('-', positionInit);
                case '*':
                    return caseOpperateur('*', positionInit);
                case '/':
                    return caseOpperateur('/', positionInit);
                case '_':
                    caseSoulignement();
                    break;
                default:
                    if (Character.isDigit(caractere)) {
                        Terminal t = caseChiffre(positionInit);
                        if (t != null)
                            return t;
                    }
                    else if (Character.isUpperCase(caractere)) {
                        Terminal t = caseMajuscule(positionInit);
                        if (t != null)
                            return t;
                    }
                    else if (Character.isLowerCase(caractere)) {
                        Terminal t = caseMinuscule(positionInit);
                        if (t != null)
                            return t;
                    }
                    else {
                        _positionLecture++;
                        throwError("\nCharactere invalide : \n" + stringErreur() + stringFleche());
                    }
            }
        }
        _positionLecture = _longueur;
        throw new IllegalArgumentException("Aucun Charactere");
    }

    /**Cas ou une lettre minuscule est lu*/
    private Terminal caseMinuscule(int positionInit) {
        switch (_etat) {
            case 0:
                _positionLecture++;
                throwError("\nIdentificateur ne peuvent commencer \npar une minuscule : \n"
                        + stringErreur() + stringFleche());
            case 1:
                _positionLecture++;
                throwError("\nChiffre ou opperateur attendu : \n"
                        + stringErreur() + stringFleche());
                break;
            case 2:
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                            Terminal.IDENTIFICATEUR);
                }
                break;
            case 3:
                _etat = 2;
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                            Terminal.IDENTIFICATEUR);
                }
                break;
        }
        return null;
    }

    /**Cas ou une lettre majuscule est lu*/
    private Terminal caseMajuscule(int positionInit) {
        switch (_etat) {
            case 0:
                _etat = 2;
                if (++_positionLecture == _longueur)
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                            Terminal.IDENTIFICATEUR);
                break;
            case 1:
                _positionLecture++;
                throwError("\nChiffre ou opperateur attendu : \n"
                        + stringErreur() + stringFleche());
            case 2:
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                            Terminal.IDENTIFICATEUR);
                }
                break;
            case 3:
                _etat = 2;
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                            Terminal.IDENTIFICATEUR);
                }
                break;
        }
        return null;
    }

    /**Cas ou un chiffre est lu*/
    private Terminal caseChiffre(int positionInit) {
        switch (_etat) {
            case 0:
                _etat = 1;
                if (++_positionLecture == _longueur)
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                            Terminal.NOMBRE);
                break;
            case 1:
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                            Terminal.NOMBRE);
                }
                break;
            case 2:
                _positionLecture++;
                throwError("\nIdentificateurs ne peuvent contenir \ndes chiffres : \n"
                        + stringErreur() + stringFleche());
            case 3:
                _positionLecture++;
                throwError("\nIdentificateurs ne peuvent contenir \ndes chiffres : \n"
                        + stringErreur() + stringFleche());
        }
        return null;
    }

    /**Cas ou une barre de soulignement est lu*/
    private void caseSoulignement() {
        switch (_etat) {
            case 0:
                _positionLecture++;
                throwError("\nIdentificateurs ne peuvent commencer \npar une barre de soulignement : \n"
                        + stringErreur() + stringFleche());
            case 1:
                _positionLecture++;
                throwError("\nChiffre ou opperateur attendu : \n"
                        + stringErreur() + stringFleche());
            case 2:
                _etat = 3;
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    throwError("\nIdentificateurs ne peuvent se terminer \npar une barre de soulignement : \n"
                            + stringErreur() + stringFleche());
                }
                break;
            case 3:
                _positionLecture++;
                throwError("\nIdentificateur ne peuvent contenir 2 \nbarres de soulignement d'affile : \n"
                        + stringErreur() + stringFleche());
        }
    }

    /**Cas ou un oppérateur est lu*/
    private Terminal caseOpperateur(char opperateur, int positionInit) {
        switch (_etat) {
            case 0:
                _positionLecture++;
                int typeTerminal = Terminal.OPPERATEUR;
                if(opperateur == '(' | opperateur == ')')
                    typeTerminal = Terminal.PARENTHESE;
                return new Terminal(opperateur + "", typeTerminal);
            case 1:
                _etat = 0;
                return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                        Terminal.NOMBRE);
            case 2:
                _etat = 0;
                return new Terminal(_aAnaliser.substring(positionInit, _positionLecture),
                        Terminal.IDENTIFICATEUR);
            case 3:
                throwError("\nIdendificateurs ne peuvent se terminer par une barre de soulignement : \n"
                        + _aAnaliser.substring(0, ++_positionLecture));
        }
        return null;
    }

    //Methode principale a lancer pour tester l'analyseur lexical
    public static void main(String[] args) {
        String toWrite = "";
        System.out.println("Debut d'analyse lexicale");
        if (args.length == 0) {
            args = new String[2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
        }
        Reader r = new Reader(args[0]);

        AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

        // Execution de l'analyseur lexical
        Terminal t = null;
        while (lexical.resteTerminal()) {
            try {
                t = lexical.prochainTerminal();
                if (t._type == Terminal.OPPERATEUR)
                    toWrite += "Operateur : ";
                else if (t._type == Terminal.NOMBRE)
                    toWrite += "Nombre : ";
                else if (t._type == Terminal.IDENTIFICATEUR)
                    toWrite += "Identificateur : ";
                else if (t._type == Terminal.PARENTHESE)
                    toWrite += "Parenthese : ";
                toWrite += t._chaine + "\n";  // toWrite contient le resultat
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.exit(51);
            }
        }                   //    d'analyse lexicale
        System.out.println(toWrite);    // Ecriture de toWrite sur la console
        Writer w = new Writer(args[1], toWrite); // Ecriture de toWrite dans fichier args[1]
        System.out.println("Fin d'analyse lexicale");
    }
}
