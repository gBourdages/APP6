/** @author Ahmed Khoumsi */

import java.io.IOException;

/** Classe Abstraite dont heriteront les classes FeuilleAST et NoeudAST
 */
public abstract class ElemAST {

  Terminal _terminal;
  public int _nombreDePasse;
  /** Evaluation d'AST
   */
  public abstract int EvalAST() throws IOException;


  /** Lecture d'AST
   */
  public abstract String LectAST();


/** ErreurEvalAST() envoie un message d'erreur lors de la construction d'AST
 */  
  public void ErreurEvalAST(String s) {	
    // 
  }

}
