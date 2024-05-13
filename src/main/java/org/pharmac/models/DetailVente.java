package org.pharmac.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class DetailVente implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDv;

	@Column(nullable = false)
	private int quantiteVendue;

	@Column(nullable = false)
	private double prixUnitaire;

	@ManyToOne
	@JoinColumn(name = "codep", nullable = false, foreignKey = @ForeignKey(name = "FK_ProduitVente"))
	private Produit produit;

	@ManyToOne
	@JoinColumn(name = "idV", nullable = false, foreignKey = @ForeignKey(name = "FK_Vente"))
	private Vente vente;
}
