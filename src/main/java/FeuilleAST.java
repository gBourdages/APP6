/** @author Ahmed Khoumsi */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

/**Constructeur pour l'initialisation d'attribut(s)
 */
  public FeuilleAST(Terminal terminal) {
      _terminal = terminal;
      _nombreDePasse = 0;
  }


  /** Evaluation de feuille d'AST
   */
  public int EvalAST() throws IOException {
      if (_terminal._type == Terminal.IDENTIFICATEUR) {
          System.out.print("Entrer une valeur pour l'identificateur " + _terminal._chaine + " : ");
          BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
          _terminal._valeur = Integer.parseInt(reader.readLine());
      }
      return _terminal._valeur;
  }


 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {
    return _terminal._chaine;
  }
}
