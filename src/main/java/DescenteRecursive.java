/**
 * @author Ahmed Khoumsi
 */

/**Cette classe effectue l'analyse syntaxique */
public class DescenteRecursive {
    AnalLex _lexical;

    /**Constructeur de DescenteRecursive :
     * - recoit en argument le nom du fichier contenant l'expression a analyser
     * - pour l'initalisation d'attribut(s) */
    public DescenteRecursive(String in) {
        Reader r = new Reader(in);
        _lexical = new AnalLex(r.toString());
    }

    /**AnalSynt() effectue l'analyse syntaxique et construit l'AST.
     * Elle retourne une reference sur la racine de l'AST construit */
    public ElemAST AnalSynt() {
        if(_lexical.resteTerminal()){
            return E();
        }else
            throw new IllegalArgumentException("Ligne vide");

    }

    /**Methode pour chaque symbole non-terminal de la grammaire retenue */
    private ElemAST E() {
      ElemAST enfantG = T();
      String Check = enfantG.LectAST();
      if(_lexical.resteTerminal()) {
        Terminal terminal = _lexical.prochainTerminal();
        if (terminal._opperateur)
          return new NoeudAST(terminal, enfantG, E());
        else
          return enfantG;
      }
        else
            return enfantG;
    }

    private FeuilleAST T() {
        if(_lexical.resteTerminal()) {
            Terminal terminal = _lexical.prochainTerminal();
            if (!terminal._opperateur) {
                return new FeuilleAST(terminal);
            } else
                throw new IllegalArgumentException("Erreur de syntaxe : " + terminal._chaine);
        }
        else
            throw new IllegalArgumentException("Erreur de syntaxe : " + _lexical.dernierChar());
    }

    //Methode principale a lancer pour tester l'analyseur syntaxique
    public static void main(String[] args) {
        String toWriteLect = "";
        String toWriteEval = "";

        System.out.println("Debut d'analyse syntaxique");
        if (args.length == 0) {
            args = new String[2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatSyntaxique.txt";
        }
        DescenteRecursive dr = new DescenteRecursive(args[0]);
        try {
            ElemAST RacineAST = dr.AnalSynt();
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

