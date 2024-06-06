package org.pharmac.models;

import lombok.*;
import org.pharmac.config.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Utilisateur implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	private String prenomU;

	private String nomU;

	private String password;

	private String telU;

	@Column(unique = true)
	private String emailU;

	private String adresseU;

	private boolean actif;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "utilisateur_roles", joinColumns = @JoinColumn(name = "utilisateur_id", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles = new ArrayList<>();

	@OneToMany(mappedBy = "utilisateur")
	private List<Vente> ventes;

	@ManyToOne
	@JoinColumn(name = "createur_id")
	private Utilisateur createur;

	public Utilisateur(String username, String prenomU, String nomU, String password, String telU, String emailU, String adresseU, boolean actif) {
		this.username = username;
		this.prenomU = prenomU;
		this.nomU = nomU;
		this.password = password;
		this.telU = telU;
		this.emailU = emailU;
		this.adresseU = adresseU;
		this.actif = actif;
	}
}
