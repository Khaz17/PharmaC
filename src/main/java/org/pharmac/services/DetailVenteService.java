package org.pharmac.services;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.DetailVente;
import org.pharmac.models.Vente;
import org.pharmac.repository.DetailVenteRepository;
import org.pharmac.repository.VenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailVenteService {
	
	@Autowired
	private DetailVenteRepository detailVenteRepository;

	@SpringBean
	private VenteService venteService;

	public List<DetailVente> getDetailVentes() {
		return detailVenteRepository.findAll();
	}

	public Optional<DetailVente> getDetailVente(Long id) {
		return detailVenteRepository.findById(id);
	}

	public List<DetailVente> getDetailsVenteByVente(Vente vente) {
//		Optional<Vente> vente = venteService.getVente(id);
//		if(vente.isPresent()) {
//		}
		return detailVenteRepository.findDetailVentesByVente(vente);
	}

	public DetailVente createOrUpdateDetailVente(DetailVente detailVente) {
		return detailVenteRepository.save(detailVente);
	}

	public void removeDetailVente(Long id) {
		Optional<DetailVente> detailVenteToDelete = getDetailVente(id);
		detailVenteToDelete.ifPresent(detailVenteRepository::delete);
	}
}
