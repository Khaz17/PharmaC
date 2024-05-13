package org.pharmac.repository;

import org.pharmac.models.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

	@Query("SELECT DISTINCT f FROM Fournisseur f JOIN Stock s JOIN Produit p WHERE f.idF = s.fournisseur.idF AND s.produit.codeP = p.codeP AND s.produit.codeP = :codeP")
	List<Fournisseur> findFournisseursByProduit(@Param("codeP") Long codeP);
}
