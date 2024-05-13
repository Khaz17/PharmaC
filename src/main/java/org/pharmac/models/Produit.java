package org.pharmac.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Produit implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeP;

	@Column(nullable = false, unique = true)
	private String nomCommercial;

	private String dci;

	private String voieAdministration;

	@Column(nullable = false)
	private double prixUnitaire;

	private String descriptionP;

	@ManyToOne
	@JoinColumn(name = "num_ctg", nullable = false, foreignKey = @ForeignKey(name = "FK_Categorie"))
	private Categorie categorieP;
}
