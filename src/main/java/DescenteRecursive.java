/**
 * @author Ahmed Khoumsi
 */

/**
 * Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {
    /**Instance de l'analiseur lexical*/
    private AnalLex _lexical;
    /**Symbole terminal en cours d'analyse*/
    private Terminal _terminal;
    /**Tableau ou les terminaux sont sauvegardé*/
    private Terminal[] _terminaux;
    /**Index du tableau précédent*/
    private int _terminalIndex;

    /**Constructeur :
     * -    Lecture du fichier texte
     * -    initialisation des attributs*/
    public DescenteRecursive(String in) {
        Reader r = new Reader(in);
        _lexical = new AnalLex(r.toString());
        _terminalIndex = 0;
        _terminaux = new Terminal[256];
    }

    /**Lecture du prochain terminal
     * et sauvegarde de celui-ci dans le tableau _terminaux */
    private Terminal prochainTerminal() {
        _terminaux[_terminalIndex] = _lexical.prochainTerminal();
        return _terminaux[_terminalIndex++];
    }

    /**Démarage de l'analyse syntaxique
     * retournant le noeud racine de l'arbre AST généré */
    public ElemAST AnalSynt() {
        if (_lexical.resteTerminal()) {
            return E();
        } else
            throw new IllegalArgumentException("Ligne vide");
    }

    /**Symbole E de la grammaire,
     * représente la règle E -> T [ + E | - E ]
     * retournant l'élément AST racine du sous arbre généré*/
    private ElemAST E() {
        ElemAST enfantG = T();
        if (_lexical.resteTerminal()) {
            if (_terminal._chaine.equals("+") | _terminal._chaine.equals("-"))
                return new NoeudAST(_terminal, enfantG, E());
            else
                return enfantG;
        } else
            return enfantG;
    }

    /**Symbole T de la grammaire,
     * représente la règle T -> U [ * T | / T ]
     * retournant l'élément AST racine du sous arbre généré*/
    private ElemAST T() {
        ElemAST enfantG = U();
        if (_lexical.resteTerminal()) {
            _terminal = prochainTerminal();
            if (_terminal._chaine.equals("*") | _terminal._chaine.equals("/"))
                return new NoeudAST(_terminal, enfantG, T());
            else
                return enfantG;
        } else
            return enfantG;
    }

    /**Symbole U de la grammaire,
     * représente la règle U -> a | b | (E)
     * retournant l'élément AST racine du sous arbre généré*/
    private ElemAST U() {
        ElemAST toReturn;
        if (_lexical.resteTerminal()) {
            _terminal = prochainTerminal();
            if (_terminalIndex > 1) {
                if (_terminal._chaine.equals(")") & _terminaux[_terminalIndex - 2]._type == Terminal.OPPERATEUR)
                    throw new IllegalArgumentException("\nErreur de syntaxe, opperateur avant \nparenthese fermante :\n"
                            + _lexical.stringErreur() + _lexical.stringFleche());
            }
            if (_terminal._chaine.equals("(")) {
                toReturn = E();
                if (!_terminal._chaine.equals(")"))
                    throw new IllegalArgumentException("\nErreur de syntaxe, manque la parenthese \nfermante :\n"
                            + _lexical.stringErreur() + _lexical.stringFleche());
                else
                    return toReturn;
            } else {
                return new FeuilleAST(_terminal);
            }
        }
        else
            throw new IllegalArgumentException("\nErreur de syntaxe, expression attendue :\n"
                    + _lexical.stringErreur() + _lexical.stringFleche());
    }

    /**Génération de l'expression post fix sous la forme d'une chaine de caractère,
     * fait de facon récursive*/
    public String postFix(NoeudAST racine) {
        String toReturn = "";
        toReturn += verifNoeud(racine);
        if (racine._enfantG != null) {
            if (racine._enfantG._terminal._type == Terminal.OPPERATEUR) {
                toReturn += postFix((NoeudAST) racine._enfantG) + " ";
            }
            else {
                toReturn += racine._enfantG._terminal._chaine + " ";
            }
        }
        toReturn += verifNoeud(racine);
        if (racine._enfantD != null) {
            if (racine._enfantD._terminal._type == Terminal.OPPERATEUR) {
                toReturn += postFix((NoeudAST) racine._enfantD) + " ";
            }
            else {
                toReturn += racine._enfantD._terminal._chaine + " ";
            }
        }
        toReturn += verifNoeud(racine);
        return toReturn;
    }

    /**Vérification du noeud donné,
     * utilisé pour la génération de l'expression post fix*/
    private String verifNoeud(NoeudAST racine) {
        racine._nombreDePasse++;
        if(racine._nombreDePasse == 3) {
            racine._nombreDePasse = 0;
            return racine._terminal._chaine + " ";
        }
        else
            return "";
    }

    public static void main(String[] args) {
        String toWriteLect = "";
        String toWriteEval = "";

        System.out.println("Debut d'analyse syntaxique\n");
        if (args.length == 0) {
            args = new String[2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatSyntaxique.txt";
        }
        DescenteRecursive dr = new DescenteRecursive(args[0]);
        try {
            ElemAST RacineAST = dr.AnalSynt();
            System.out.println("Expression postFix : " + dr.postFix((NoeudAST) RacineAST) + "\n");
            toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
            System.out.println(toWriteLect);
            toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
            System.out.println(toWriteEval);
            Writer w = new Writer(args[1], toWriteLect + toWriteEval); // Ecriture de toWrite
            // dans fichier args[1]
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(51);
        }
        System.out.println("Analyse syntaxique terminee");
    }

}

