package org.pharmac.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Role implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roleName;

	private String roleLibelle;

	private String roleDescription;

//	public Role(String roleName, String roleLibelle, String roleDescription) {
//		this.roleName = roleName;
//		this.roleLibelle = roleLibelle;
//		this.roleDescription = roleDescription;
//	}
}
