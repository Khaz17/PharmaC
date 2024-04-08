package org.pharmac.services;

import org.pharmac.models.Categorie;
import org.pharmac.models.Produit;
import org.pharmac.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {
	@Autowired
	private ProduitRepository produitRepository;

	public List<Produit> getProduits() {
		return produitRepository.findAll();
	}

	public Optional<Produit> getProduit(Long id) {
		return produitRepository.findById(id);
	}

	public Optional<Categorie> getProduitCategorie(Long id) {
		Optional<Produit> produit = produitRepository.findById(id);
		return Optional.ofNullable(produit.get().getCategorieP());
	}

	public Produit createOrUpdateProduit(Produit produit) {
		return produitRepository.save(produit);
	}

	public void removeProduit(Long id) {
		Optional<Produit> produitToDelete = getProduit(id);
		produitToDelete.ifPresent(produitRepository::delete);
	}
}
