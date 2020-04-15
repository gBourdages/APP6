/** @author Ahmed Khoumsi */

public class Terminal {
  String _chaine;
  int _type;
  int _valeur;

  public static final int OPPERATEUR = 0;
  public static final int NOMBRE = 1;
  public static final int IDENTIFICATEUR = 2;
  public static final int PARENTHESE = 3;

  public Terminal(String chaine, int type) {   // arguments possibles
     _chaine = chaine;
    _type = type;
    if (_type == NOMBRE)
      _valeur = Integer.parseInt(_chaine);
    else
      _valeur = 0;
  }
}
