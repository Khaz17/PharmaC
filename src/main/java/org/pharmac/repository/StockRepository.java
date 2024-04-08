package org.pharmac.repository;

import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
	List<Stock> getStocksByProduit(Produit produit);
	List<Stock> getStocksByFournisseur(Fournisseur fournisseur);

	@Query("SELECT p FROM Stock s JOIN Produit p ON s.produit.codeP = p.codeP WHERE s.idStk = :idStk")
	Produit getStockProduit(@Param("idStk") Long idStk);

	@Query("SELECT f FROM Stock s JOIN Fournisseur f ON s.fournisseur.idF = f.idF WHERE s.idStk = :idStk")
	Fournisseur getStockFournisseur(@Param("idStk") Long idStk);

	@Query("SELECT s, p, f FROM Stock s JOIN s.produit p JOIN s.fournisseur f WHERE s.idStk = :idStk")
	List<Object[]> findStockWithProduitAndFournisseur(@Param("idStk") Long idStk);

	@Query("SELECT s FROM Stock s WHERE quantiteStk = 0")
	List<Stock> findAllEmptyStocks();
}
