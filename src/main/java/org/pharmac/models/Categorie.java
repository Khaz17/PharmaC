package org.pharmac.models;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Categorie implements Serializable {
	@Id @GeneratedValue
	private Long numCtg;

	@Column(nullable = false, unique = true)
	private String libelleCtg;

	private String descriptionCtg;
}
