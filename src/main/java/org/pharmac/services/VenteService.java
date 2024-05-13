package org.pharmac.services;

import org.pharmac.models.DetailVente;
import org.pharmac.models.Vente;
import org.pharmac.repository.DetailVenteRepository;
import org.pharmac.repository.VenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenteService {
	
	@Autowired
	private VenteRepository venteRepository;

	@Autowired
	private DetailVenteRepository detailVenteRepository;

	public List<Vente> getVentes() {
		return venteRepository.findAll();
	}

	public Vente getNewVente(){
		return venteRepository.findLastVente();
	}

	public Optional<Vente> getVente(Long id) {
		return venteRepository.findById(id);
	}

	public Vente saveVente(Vente vente) {
		venteRepository.save(vente);
		for (DetailVente dv: vente.getDetailVenteList()) {
			dv.setVente(vente);
			detailVenteRepository.save(dv);
		}
		return vente;
	}

	public void removeVente(Long id) {
		Optional<Vente> venteToDelete = getVente(id);
		venteToDelete.ifPresent(venteRepository::delete);
	}
}
