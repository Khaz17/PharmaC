package org.pharmac.services;

import org.pharmac.config.Role;
import org.pharmac.models.Utilisateur;
import org.pharmac.repository.GestionUtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UtilisateurService {

	@Autowired
	private GestionUtilisateurRepository utilisateurRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

//	UserDetails logged = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	public List<Utilisateur> getAllUtilisateurs() {
		List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
		return utilisateurs;
	}

	public List<Utilisateur> getActiveUtilisateurs(){
		List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
		utilisateurs.removeIf(utilisateur -> !utilisateur.isActif());
		return utilisateurs;
	}

	public Utilisateur createUtilisateur(Utilisateur utilisateur) {
		boolean usernameAlreadyUsed = false, emailAlreadyUsed = false;
		List<Utilisateur> utilisateurs = getAllUtilisateurs();
		for (Utilisateur u : utilisateurs) {
			if (utilisateur.getUsername().equals(u.getUsername())) {
				usernameAlreadyUsed = true;
				break;
			}

			if (utilisateur.getEmailU().equals(u.getEmailU())) {
				emailAlreadyUsed = true;
				break;
			}
		}
		if (usernameAlreadyUsed || emailAlreadyUsed) {
			return null;
		} else {
			String encodedPassword = passwordEncoder.encode(utilisateur.getPassword());
			utilisateur.setPassword(encodedPassword);
			return utilisateurRepository.save(utilisateur);
		}
	}

	public String getUtilisateurRolesToString(Utilisateur utilisateur) {
		List<Role> roles = utilisateur.getRoles();
		StringBuilder result = new StringBuilder();
		int i;
		for(i = 0; i < roles.size(); i++) {
			if (i == roles.size() - 1) {
				result.append(roles.get(i).getRoleName());
			} else {
				result.append(roles.get(i).getRoleName()).append(", \n");
			}
		}
		return result.toString();
	}

	public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
		// Find the existing utilisateur
		Utilisateur existingUtilisateur = utilisateurRepository.findById(utilisateur.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur" + utilisateur.getId() + "not found"));

		// Update the existing utilisateur's fields
		existingUtilisateur.setUsername(utilisateur.getUsername());
		existingUtilisateur.setPrenomU(utilisateur.getPrenomU());
		existingUtilisateur.setNomU(utilisateur.getNomU());
		existingUtilisateur.setPassword(utilisateur.getPassword());
		existingUtilisateur.setTelU(utilisateur.getTelU());
		existingUtilisateur.setEmailU(utilisateur.getEmailU());
		existingUtilisateur.setAdresseU(utilisateur.getAdresseU());
		existingUtilisateur.setActif(utilisateur.isActif());
		existingUtilisateur.setRoles(utilisateur.getRoles());
		existingUtilisateur.setVentes(utilisateur.getVentes());

		// Save and return the updated utilisateur
		return utilisateurRepository.save(existingUtilisateur);
	}

	public void affecterDroits(Utilisateur utilisateur, List<Role> roles) {
		if (roles.size() >= 1) {
			for (Role r : utilisateur.getRoles()) {
				utilisateur.getRoles().remove(r);
			}
			utilisateur.setRoles(roles);
		} else {
			utilisateur.setRoles(new ArrayList<>());
		}
		utilisateurRepository.save(utilisateur);
	}

	public Utilisateur changePassword(Utilisateur utilisateur, String password) {
		if (passwordEncoder == null) {
			throw new IllegalStateException("PasswordEncoder n'est pas initialisé");
		}

		Utilisateur existingUtilisateur = utilisateurRepository.findById(utilisateur.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur" + utilisateur.getId() + "not found"));
		String encodedPassword = passwordEncoder.encode(password);
		if (encodedPassword.equals(existingUtilisateur.getPassword())) {
			return null;
		} else {
			existingUtilisateur.setPassword(encodedPassword);
			utilisateurRepository.save(existingUtilisateur);
			return existingUtilisateur;
		}

	}
	// not used yet
	public void resetPassword(Utilisateur utilisateur) {
		Utilisateur existingUtilisateur = utilisateurRepository.findById(utilisateur.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur" + utilisateur.getId() + "not found"));
		existingUtilisateur.setPassword("");
	}

	public boolean isUserAdmin(Utilisateur utilisateur){
		return utilisateur.getRoles().contains(roleService.getRoleByName("ADMIN"));
	}

	public boolean hasAtLeastARole(Utilisateur utilisateur) {
		return utilisateur.getRoles().size() > 0;
	}

	// not used yet
	public void deleteUtilisateur(Utilisateur utilisateur) {
		Optional<Utilisateur> utilisateurToDelete = utilisateurRepository.findById(utilisateur.getId());
		if (utilisateurToDelete.isPresent()) {
			if (hasAtLeastARole(utilisateurToDelete.get())) {
//				logger.warn("L'utilisateur {} a tenté de supprimer l'utilisateur {} alors qu'il a un rôle. Échec.", logged.getUsername(), utilisateur.getUsername());
			} else {
				utilisateurRepository.delete(utilisateurToDelete.get());
//				logger.info("L'utilisateur {} a supprimé l'utilisateur {} avec succès.", logged.getUsername(), utilisateur.getUsername());
			}
		}

	}

	public Optional<Utilisateur> getUtilisateurByUsername(String username) {
		Optional<Utilisateur> utilisateur = utilisateurRepository.findByUsername(username);
		return utilisateur;
	}

	public Optional<Utilisateur> getUtilisateurById(Long id) {
		Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
		return utilisateur;
	}

}
