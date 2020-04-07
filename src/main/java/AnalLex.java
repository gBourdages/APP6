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
        _aAnaliser = _aAnaliser.replace(" ", "");
        _longueur = _aAnaliser.length();
        _etat = 0;
        _positionLecture = 0;
    }


    /**
     * resteTerminal() retourne :
     * false  si tous les terminaux de l'expression arithmetique ont ete retournes
     * true s'il reste encore au moins un terminal qui n'a pas ete retourne
     */
    public boolean resteTerminal() {
        //
    }


    /**
     * prochainTerminal() retourne le prochain terminal
     * Cette methode est une implementation d'un AEF
     */
    public Terminal prochainTerminal() {
        int positionInit = _positionLecture;
        while (_positionLecture != _longueur) {
            switch (_aAnaliser.charAt(_positionLecture)) {
                case '+':
                  return opperateurCase('+', positionInit);
                case '-':
                  return opperateurCase('-', positionInit);
                case '*':
                  return opperateurCase('*', positionInit);
                case '/':
                  return opperateurCase('/', positionInit);
                case '=':
                  return opperateurCase('=', positionInit);
                default:
                    char courant = _aAnaliser.charAt(_positionLecture);
                    if (courant == ('0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9')) {
                        switch (_etat) {
                            case 0:
                                _etat = 1;
                                _positionLecture++;
                                break;
                            case 1:
                                if (_positionLecture == _longueur - 1) {
                                    _etat = 0;
                                    return new Terminal(_aAnaliser.substring(positionInit, _positionLecture), false);
                                } else
                                    _positionLecture++;
                                break;
                        }
                    } else
                        throw new IllegalArgumentException(_aAnaliser.substring(positionInit, _positionLecture));
            }
        }
      throw new IllegalArgumentException("Aucun terminal");
    }

  private Terminal opperateurCase(char opperateur, int posInit) {
    switch (_etat) {
        case 0:
            _positionLecture++;
            return new Terminal(opperateur + "", true);
        case 1:
            _etat = 0;
            return new Terminal(_aAnaliser.substring(posInit, _positionLecture), false);
    }
    return null;
  }

  /**
     * ErreurLex() envoie un message d'erreur lexicale
     */
    public void ErreurLex(String s) {
        throw new IllegalArgumentException(s);
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
          } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
          }

            toWrite += t._chaine + "\n";  // toWrite contient le resultat
        }                   //    d'analyse lexicale
        System.out.println(toWrite);    // Ecriture de toWrite sur la console
        Writer w = new Writer(args[1], toWrite); // Ecriture de toWrite dans fichier args[1]
        System.out.println("Fin d'analyse lexicale");
    }
}
