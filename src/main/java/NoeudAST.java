/**
 * @author Ahmed Khoumsi
 */

/**
 * Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

    Terminal _terminal;
    ElemAST _enfantG;
    ElemAST _enfantD;

    /**
     * Constructeur pour l'initialisation d'attributs
     */
    public NoeudAST(Terminal terminal, ElemAST enfantG, ElemAST enfantD) {
        _terminal = terminal;
        _enfantG = enfantG;
        _enfantD = enfantD;
    }

    /**
     * Evaluation de noeud d'AST
     */
    public int EvalAST() {
        switch (_terminal._chaine) {
            case "+":
                return _enfantG.EvalAST() + _enfantD.EvalAST();
            case "-":
                return _enfantG.EvalAST() - _enfantD.EvalAST();
            case "*":
                return _enfantG.EvalAST() * _enfantD.EvalAST();
            case "/":
                return _enfantG.EvalAST() / _enfantD.EvalAST();
            default:
                return 0;
        }
    }

    /** Lecture de noeud d'AST
     */
  public String LectAST( ) {
    return "(" + _enfantG.LectAST() + _terminal._chaine + _enfantD.LectAST() + ")";
  }
}


