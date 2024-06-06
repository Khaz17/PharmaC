package org.pharmac.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class Stock implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idStk;

	@Column(nullable = false)
	private Date datePeremption;

	@Column(nullable = false)
	private int quantiteStk;

	private LocalDateTime dateAjout;

	@JoinColumn(name = "codep", nullable = false, foreignKey = @ForeignKey(name = "FK_Produit"))
	@ManyToOne
	private Produit produit;

	@JoinColumn(name = "idf", nullable = false, foreignKey = @ForeignKey(name = "FK_Fournisseur"))
	@ManyToOne
	private Fournisseur fournisseur;
}
