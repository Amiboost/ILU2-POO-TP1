package histoire;

import villagegaulois.Etal;
import personnages.Gaulois;

public class ScenarioCasDegrade {
	public static void main(String[] args) {
		Etal etal = new Etal();
		etal.libererEtal();
		
		Gaulois test = new Gaulois("Bonnemine", 42);
		try {
			etal.acheterProduit(0, test);
		} catch (IllegalArgumentException e) {
			System.out.println("Le produit n'a pas été acheté");
		}
		try {
			etal.acheterProduit(2, test);
		} catch (IllegalStateException e) {
			System.out.println("L'étal n'a rien vendu");
		}
		System.out.println("Fin du test");
		}
}
