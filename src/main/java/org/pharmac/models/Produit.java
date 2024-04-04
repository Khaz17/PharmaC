package org.pharmac.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Produit {
	@Id @GeneratedValue
	private Long codeP;

	@Column(nullable = false, unique = true)
	private String nomP;

	private String descriptionP;

	@ManyToOne
	private Categorie categorieP;
}
