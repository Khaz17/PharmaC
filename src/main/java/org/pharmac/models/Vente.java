package org.pharmac.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Vente implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idV;

	@Column(nullable = false)
	private LocalDateTime dateVente;

	private double remise;

	@Column(nullable = false)
	private double total;

	private double sommeRendue;

	private String nomClient;

	@OneToMany(mappedBy = "vente")
	private List<DetailVente> detailVenteList;
}
