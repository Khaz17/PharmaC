package org.pharmac.services;

import org.pharmac.models.Fournisseur;
import org.pharmac.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FournisseurService {
	@Autowired
	private FournisseurRepository repository;

	public Optional<Fournisseur> getFournisseur(Long id) {
		return repository.findById(id);
	}

	public List<Fournisseur> getFournisseurs(){
		return repository.findAll();
	}

	public Fournisseur createOrUpdateFournisseur(Fournisseur fournisseur) {
		return repository.save(fournisseur);
	}

	public void removeFournisseur(Long id) {
		Optional<Fournisseur> fournisseur = getFournisseur(id);
		fournisseur.ifPresent(repository::delete);
	}

}
