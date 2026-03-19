package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;
import villagegaulois.Etal;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;
	private int nbEtals;

	private static class Marche {
		private Etal[] etals;
		private int nbEtals;

		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i=0; i < nbEtals; i++)
				etals[i] = new Etal();
			this.nbEtals = nbEtals;
		}

		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			if (indiceEtal >= nbEtals || indiceEtal < 0)
				System.out.println("Indice invalide");
			else
				etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}

		private int trouverEtalLibre() {
			for (int i=0; i < nbEtals; i++) {
				if (!etals[i].isEtalOccupe())
					return i;
			}
			return -1;
		}

		private Etal[] trouverEtals(String produit) {
			int tabSize = 0;
			for (int i=0; i < nbEtals; i++) {
				if (etals[i].contientProduit(produit))
					tabSize++;
			}
			Etal[] result = new Etal[tabSize];
			int curIndex = 0;
			for (int i=0; i < nbEtals && curIndex < tabSize; i++) {
				if (etals[i].contientProduit(produit)) {
					result[curIndex] = etals[i];
					curIndex++;
				}
			}
			return result;
		}

		private Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < nbEtals; i++) {
				if (etals[i].getVendeur() == gaulois)
					return etals[i];
			}
			return null;
		}

		private String afficherMarche() {
			int nbEtalVide = 0;
			String result = "";
			for (int i=0; i < nbEtals; i++) {
				if (etals[i].isEtalOccupe()) {
					result = result + etals[i].afficherEtal();
				} else
					nbEtalVide++;
			}
			if (nbEtalVide != 0)
				result = result + "Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n";

			return result;
		}
	}

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.nbEtals = nbEtals;
		marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		if (chef == null) {
			throw new VillageSansChefException("Le village n'a pas de chef");
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}

	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder result = new StringBuilder();
		result.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		int iEtal = marche.trouverEtalLibre();
		if (iEtal == -1)
			result.append("Aucun étal n'est disponible.\n");
		else {
			marche.utiliserEtal(iEtal, vendeur, produit, nbProduit);
			result.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + iEtal + ".\n");
		}
		
		return result.toString();
	}

	public String rechercherVendeursProduit(String produit) {
		Etal[] tab = marche.trouverEtals(produit);
		int longueur = tab.length;
		StringBuilder result = new StringBuilder();
		if (longueur == 0)
			result.append("Il n'y a pas de vendeur qui propose des " + produit + " au marché.\n");
		else if (longueur == 1)
			result.append("Seul le vendeur " + tab[0].getVendeur().getNom() + " propose des " + produit + " au marché.\n");
		else {
			result.append("Les vendeurs qui proposent des " + produit + " sont :\n");
			for (int i=0; i < longueur; i++)
				result.append("- " + tab[i].getVendeur().getNom() +"\n");
		}

		return result.toString();
	}

	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}

	public String partirVendeur(Gaulois vendeur) {
		return rechercherEtal(vendeur).libererEtal();
	}

	public String afficherMarche() {
		return marche.afficherMarche();
	}
}