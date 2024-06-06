package org.pharmac.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "pharmacy_settings")
public class PharmacySettings implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	private String nomPharma;

	private String adressePharma;

	private String telPharma;

	private String emailPharma;

	private String descriptionPharma;

	private String theme;

//	private String accessUrl;

	public PharmacySettings(){
		this.theme = "Clair";
	}

	public PharmacySettings(String nomPharma, String adressePharma, String telPharma, String emailPharma, String descriptionPharma, String theme) {
		this.nomPharma = nomPharma;
		this.adressePharma = adressePharma;
		this.telPharma = telPharma;
		this.emailPharma = emailPharma;
		this.descriptionPharma = descriptionPharma;
		this.theme = theme;
	}
}