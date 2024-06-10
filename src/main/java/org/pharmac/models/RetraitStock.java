package org.pharmac.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class RetraitStock implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "codep", foreignKey = @ForeignKey(name = "FK_ProduitRetraitStock"))
	private Produit produit;

	private int quantiteRetiree;

	private LocalDateTime dateRetrait;

}
