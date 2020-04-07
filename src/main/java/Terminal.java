/** @author Ahmed Khoumsi */

public class Terminal {
  String _chaine;
  Boolean _opperateur;
  int _valeur;

  public Terminal(String chaine, Boolean opperateur) {   // arguments possibles
     _chaine = chaine;
    _opperateur = opperateur;
    if (!_opperateur)
      _valeur = Integer.parseInt(_chaine);
    else
      _valeur = 0;
  }
}
