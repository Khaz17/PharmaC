package org.pharmac.repository;

import org.pharmac.models.Utilisateur;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<Utilisateur, Long> {
	Optional<Utilisateur> findByUsername(String username);
	Boolean existsByUsername(String username);
}
