package org.pharmac.repository;

import org.pharmac.models.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
//	Optional<Produit> findByCodeP(Long codeP);
	@Query("SELECT p FROM Produit p WHERE LOWER(p.nomCommercial) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.categorieP.libelleCtg) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.dci) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.voieAdministration) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.formeGalenique) LIKE LOWER(CONCAT('%', :query, '%')) "
	)
	List<Produit> searchProduits(@Param("query") String query);

	@Query("SELECT p FROM Produit p WHERE LOWER(p.nomCommercial) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.categorieP.libelleCtg) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.dci) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.voieAdministration) LIKE LOWER(CONCAT('%', :query, '%')) " +
			"OR LOWER(p.formeGalenique) LIKE LOWER(CONCAT('%', :query, '%')) "
	)
	List<Produit> searchAmongAvailableProduits(@Param("query") String query);
}
