/**
 * @author Ahmed Khoumsi
 */

/**
 * Cette classe effectue l'analyse lexicale
 */
public class AnalLex {
    String _aAnaliser;
    int _longueur;
    int _etat;
    int _positionLecture;

    /**
     * Constructeur pour l'initialisation d'attribut(s)
     */
    public AnalLex(String aAnaliser) {  // arguments possibles
        _aAnaliser = aAnaliser;
        if (aAnaliser != null) {
            _aAnaliser = _aAnaliser.replace(" ", "");
            _longueur = _aAnaliser.length();
        } else
            _aAnaliser = "";
        _etat = 0;
        _positionLecture = 0;
    }


    /**
     * resteTerminal() retourne :
     * false  si tous les terminaux de l'expression arithmetique ont ete retournes
     * true s'il reste encore au moins un terminal qui n'a pas ete retourne
     */
    public boolean resteTerminal() {
        return _longueur != _positionLecture;
    }

    public char dernierChar() {
        return _aAnaliser.charAt(_longueur - 1);
    }

    /**
     * prochainTerminal() retourne le prochain terminal
     * Cette methode est une implementation d'un AEF
     */
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
                    caseSoulignement(positionInit);
                    break;
                default:
                    if (Character.isDigit(caractere)) {
                        Terminal t = caseNombres(positionInit);
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
                        throwError(_aAnaliser.substring(positionInit, _positionLecture));
                    }
            }
        }
        _positionLecture = _longueur;
        throw new IllegalArgumentException("Aucun terminal");
    }

    private void throwError(String message) {
        _positionLecture = _longueur;
        throw new IllegalArgumentException(message);
    }

    private Terminal caseMinuscule(int positionInit) {
        switch (_etat) {
            case 2:
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.IDENTIFICATEUR);
                }
                break;
            case 3:
                _etat = 2;
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.IDENTIFICATEUR);
                }
                break;
            default:
                throwError(_aAnaliser.substring(positionInit, ++_positionLecture));
        }
        return null;
    }

    private Terminal caseMajuscule(int positionInit) {
        switch (_etat) {
            case 0:
                _etat = 2;
                if (++_positionLecture == _longueur)
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.IDENTIFICATEUR);
                break;
            case 2:
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.IDENTIFICATEUR);
                }
                break;
            case 3:
                _etat = 2;
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.IDENTIFICATEUR);
                }
                break;
            default:
                throwError(_aAnaliser.substring(positionInit, ++_positionLecture));
        }
        return null;
    }

    private Terminal caseNombres(int positionInit) {
        switch (_etat) {
            case 0:
                _etat = 1;
                if (++_positionLecture == _longueur)
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.NOMBRE);
                break;
            case 1:
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.NOMBRE);
                }
                break;
            default:
                throwError(_aAnaliser.substring(positionInit, ++_positionLecture));
        }
        return null;
    }

    private void caseSoulignement(int positionInit) {
        switch (_etat) {
            case 2:
                _etat = 3;
                if (++_positionLecture == _longueur) {
                    _etat = 0;
                    throwError(_aAnaliser.substring(positionInit, _positionLecture));
                }
                break;
            default:
                throwError(_aAnaliser.substring(positionInit, ++_positionLecture));
        }
    }

    private Terminal caseOpperateur(char opperateur, int positionInit) {
        switch (_etat) {
            case 0:
                _positionLecture++;
                return new Terminal(opperateur + "", Terminal.OPPERATEUR);
            case 1:
                _etat = 0;
                return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.NOMBRE);
            case 2:
                _etat = 0;
                return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), Terminal.IDENTIFICATEUR);
            case 3:
                throwError(_aAnaliser.substring(positionInit, ++_positionLecture));
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
