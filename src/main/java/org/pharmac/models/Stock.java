package org.pharmac.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Stock implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idStk;

	@Column(nullable = false)
	private String dosage;

	private LocalDate datePeremption;

	@Column(nullable = false)
	private int quantiteStk;

	private String voieAdministration;

	@Column(nullable = false)
	private double prixUnitaire;

	private LocalDateTime dateAjout;

	@JoinColumn(name = "codep", nullable = false, foreignKey = @ForeignKey(name = "FK_Produit"))
	@ManyToOne
	private Produit produit;

	@JoinColumn(name = "idf", nullable = false, foreignKey = @ForeignKey(name = "FK_Fournisseur"))
	@ManyToOne
	private Fournisseur fournisseur;
}
