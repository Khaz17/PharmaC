package org.pharmac.repository;

import org.pharmac.models.DetailVente;
import org.pharmac.models.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailVenteRepository extends JpaRepository<DetailVente, Long> {
//
//	@Query("SELECT * FROM DetailVente dv WHERE dv.vente.idV = :id")
//	public List<DetailVente> findDetailsVenteByVente(@Param("id") Long id);

	public List<DetailVente> findDetailVentesByVente(Vente vente);

}