package org.pharmac.repository;

import org.pharmac.models.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
//	Optional<Produit> findByCodeP(Long codeP);
}
