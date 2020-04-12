/**
 * @author Ahmed Khoumsi
 */

/**
 * Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {
    AnalLex _lexical;
    private Terminal _terminal;
    private Terminal _terminalPrecedent;

    /**
     * Constructeur de DescenteRecursive :
     * - recoit en argument le nom du fichier contenant l'expression a analyser
     * - pour l'initalisation d'attribut(s)
     */
    public DescenteRecursive(String in) {
        Reader r = new Reader(in);
        _lexical = new AnalLex(r.toString());
    }

    /**
     * AnalSynt() effectue l'analyse syntaxique et construit l'AST.
     * Elle retourne une reference sur la racine de l'AST construit
     */
    public ElemAST AnalSynt() {
        if (_lexical.resteTerminal()) {
            return E();
        } else
            throw new IllegalArgumentException("Ligne vide");

    }

    /**
     * Methode pour chaque symbole non-terminal de la grammaire retenue
     */
    private ElemAST E() {
        ElemAST enfantG = T();
        if (_lexical.resteTerminal()) {
            //_terminal = _lexical.prochainTerminal();
            if (_terminal._chaine.equals("+") | _terminal._chaine.equals("-"))
                return new NoeudAST(_terminal, enfantG, E());
            else
                return enfantG;
        } else
            return enfantG;
    }

    private ElemAST T() {
        ElemAST enfantG = U();
        if (_lexical.resteTerminal()) {
            _terminal = _lexical.prochainTerminal();
            if (_terminal._chaine.equals("*") | _terminal._chaine.equals("/"))
                return new NoeudAST(_terminal, enfantG, T());
            else
                return enfantG;
        } else
            return enfantG;
        /*if(_lexical.resteTerminal()) {
            Terminal terminal = _lexical.prochainTerminal();
            if (terminal._type != Terminal.OPPERATEUR) {
                return new FeuilleAST(terminal);
            } else
                throw new IllegalArgumentException("Erreur de syntaxe : " + terminal._chaine);
        }
        else
            throw new IllegalArgumentException("Erreur de syntaxe : " + _lexical.dernierChar());*/
    }

    private ElemAST U() {
        ElemAST toReturn;
        if (_lexical.resteTerminal()) {
            _terminalPrecedent = _terminal;
            _terminal = _lexical.prochainTerminal();
            if (_terminalPrecedent != null) {
                if (_terminal._chaine.equals(")") & _terminalPrecedent._type == Terminal.OPPERATEUR)
                    throw new IllegalArgumentException("Erreur de syntaxe : " + _terminalPrecedent._chaine +_terminal._chaine);
            }
            if (_terminal._chaine.equals("(")) {
                toReturn = E();
                //if (!_lexical.resteTerminal())
                //    throw new IllegalArgumentException("Erreur de syntaxe : " + _terminal._chaine);
                //_terminal = _lexical.prochainTerminal();
                if (!_terminal._chaine.equals(")"))
                    throw new IllegalArgumentException("Erreur de syntaxe : " + _terminal._chaine);
                else
                    return toReturn;
            } else {
                return new FeuilleAST(_terminal);
            }
        }
        else
            throw new IllegalArgumentException("Erreur de syntaxe : " + _lexical.dernierChar());
    }


    public String postFix(NoeudAST racine) {
        String toReturn = "";
        toReturn += checkNoeud(racine);
        if (racine._enfantG != null) {
            if (racine._enfantG._terminal._type == Terminal.OPPERATEUR) {
                toReturn += postFix((NoeudAST) racine._enfantG) + " ";
            }
            else {
                toReturn += racine._enfantG._terminal._chaine + " ";
            }
        }
        toReturn += checkNoeud(racine);
        if (racine._enfantD != null) {
            if (racine._enfantD._terminal._type == Terminal.OPPERATEUR) {
                toReturn += postFix((NoeudAST) racine._enfantD) + " ";
            }
            else {
                toReturn += racine._enfantD._terminal._chaine + " ";
            }
        }
        toReturn += checkNoeud(racine);
        return toReturn;
    }

    private String checkNoeud(NoeudAST racine) {
        racine._nombreDePasse++;
        if(racine._nombreDePasse == 3) {
            racine._nombreDePasse = 0;
            return racine._terminal._chaine + " ";
        }
        else
            return "";
    }

    //Methode principale a lancer pour tester l'analyseur syntaxique
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

