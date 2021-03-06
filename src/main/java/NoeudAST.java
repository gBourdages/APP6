/**
 * @author Ahmed Khoumsi
 */

import java.io.IOException;

/**Classe representant un noeud d'AST*/
public class NoeudAST extends ElemAST {
    /**Enfant de gauche du noeud*/
    ElemAST _enfantG;
    /**Enfant de droite du noeud*/
    ElemAST _enfantD;

    /**Constructeur pour l'initialisation d'attributs*/
    public NoeudAST(Terminal terminal, ElemAST enfantG, ElemAST enfantD) {
        _terminal = terminal;
        _enfantG = enfantG;
        _enfantD = enfantD;
        _nombreDePasse = 0;
    }

    /**Evaluation du noeud d'AST*/
    public int EvalAST() throws IOException {
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

    /**Lecture de noeud d'AST*/
    public String LectAST() {
        return "(" + _enfantG.LectAST() + _terminal._chaine + _enfantD.LectAST() + ")";
    }
}


