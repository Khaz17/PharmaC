package org.pharmac.models;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Categorie implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long numCtg;

	@Column(nullable = false, unique = true)
	private String libelleCtg;

	private String descriptionCtg;
}
