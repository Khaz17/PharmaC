package org.pharmac.services;

import lombok.RequiredArgsConstructor;
import org.pharmac.models.Utilisateur;
import org.pharmac.repository.GestionUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {

	@Autowired
	private final GestionUtilisateurRepository utilisateurRepository;
	private final PasswordEncoder passwordEncoder;

}
