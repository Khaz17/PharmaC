package org.pharmac.repository;

import org.pharmac.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GestionUtilisateurRepository extends JpaRepository<Utilisateur, Long> {
	Optional<Utilisateur> findByUsername(String username);

//	@Query("DELETE FROM t ur WHERE ur.utilisateur_id = :id")
//	void effacerDroitsPourUtilisateur(@Param("id") Long id);
}
