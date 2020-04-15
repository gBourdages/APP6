/** @author Ahmed Khoumsi */

import java.io.IOException;

/** Classe Abstraite dont heriteront les classes FeuilleAST et NoeudAST*/
public abstract class ElemAST {
  /**Symbole terminal contenu dans l'arbre*/
  Terminal _terminal;
  /**Nombre de passe sur l'élément
   * (pour l'expression post fix)*/
  public int _nombreDePasse;

  /** Evaluation d'AST*/
  public abstract int EvalAST() throws IOException;

  /** Lecture d'AST*/
  public abstract String LectAST();
}
