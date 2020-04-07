/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

  Terminal _terminal;
/**Constructeur pour l'initialisation d'attribut(s)
 */
  public FeuilleAST(Terminal terminal) {
      _terminal = terminal;
  }


  /** Evaluation de feuille d'AST
   */
  public int EvalAST( ) {
      return _terminal._valeur;
  }


 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {
    return _terminal._chaine;
  }
}
