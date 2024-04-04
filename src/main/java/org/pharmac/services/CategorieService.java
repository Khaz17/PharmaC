package org.pharmac.services;

import org.pharmac.models.Categorie;
import org.pharmac.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {
	@Autowired
	private CategorieRepository categorieRepository;

	public List<Categorie> getCategories() {
		return categorieRepository.findAll();
	}

	public Optional<Categorie> getCategorie(Long id) {
		return categorieRepository.findById(id);
	}

	public Categorie createOrUpdateCategorie(Categorie categorie) {
		return categorieRepository.save(categorie);
	}

	public void removeCategorie(Long id) {
		Optional<Categorie> categorieToDelete = getCategorie(id);
		categorieToDelete.ifPresent(categorieRepository::delete);
	}

}
