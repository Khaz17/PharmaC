package org.pharmac.repository;

import org.pharmac.models.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface VenteRepository extends JpaRepository<Vente, Long> {
	@Query(value = "SELECT v FROM Vente v ORDER BY v.idV DESC")
	List<Vente> findAllVentesIdVDesc();

	default Vente findLastVente() {
		return findAllVentesIdVDesc().stream().findFirst().get();
	}
}