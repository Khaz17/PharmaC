package org.pharmac.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Fournisseur implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idF;

	@Column(nullable = false, unique = true)
	private String nomF;

	@Column(nullable = false, unique = true)
	private String emailF;

	@Column(nullable = false, unique = true)
	private String telF;

	private String adresseF;

//	private boolean active;
}
