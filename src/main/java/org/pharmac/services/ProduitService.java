package org.pharmac.services;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Categorie;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.repository.ProduitRepository;
import org.pharmac.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {
	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private StockRepository stockRepository;

	public List<Produit> getProduits() {
		return produitRepository.findAll();
	}

	public List<Produit> getProduitsDisponibles() {
		List<Produit> availableProducts = getProduits();
		availableProducts.removeIf(p -> getProduitStockTotal(p.getCodeP()) < 1);
		return availableProducts;
	}

	public Optional<Produit> getProduit(Long id) {
		return produitRepository.findById(id);
	}

	public Optional<Categorie> getProduitCategorie(Long id) {
		Optional<Produit> produit = produitRepository.findById(id);
		return Optional.ofNullable(produit.get().getCategorieP());
	}

	public List<Stock> getProduitStocks(Long id) {
		Optional<Produit> produit = produitRepository.findById(id);
		List<Stock> produitStocks = Collections.emptyList();
		if (produit.isPresent()) {
			produitStocks = stockRepository.findAllStocksByProduit(produit.get().getCodeP());
		}
		return produitStocks;
	}

	public int getProduitStockTotal(Long id) {
		int stockTotal = 0;
		Optional<Produit> produit = produitRepository.findById(id);
		if (produit.isPresent()) {
			List<Stock> stocks = stockRepository.findAllStocksByProduit(produit.get().getCodeP());
			if (!stocks.isEmpty()) {
				for (Stock s : stocks) {
					stockTotal = stockTotal + s.getQuantiteStk();
				}
			}
		}
		return stockTotal;
	}

	public Produit createOrUpdateProduit(Produit produit) {
		return produitRepository.save(produit);
	}

	public void removeProduit(Long id) {
		Optional<Produit> produitToDelete = getProduit(id);
		produitToDelete.ifPresent(produitRepository::delete);
	}
}
