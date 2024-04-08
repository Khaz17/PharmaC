package org.pharmac.services;

import org.apache.wicket.util.lang.Objects;
import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {
	@Autowired
	private StockRepository stockRepository;

	public List<Stock> getStocks() {
		return stockRepository.findAll();
	}

	public Optional<Stock> getStock(Long id) {
		return stockRepository.findById(id);
	}

	public Stock createOrUpdateStock(Stock Stock) {
		return stockRepository.save(Stock);
	}

	public void removeStock(Long id) {
		Optional<Stock> stockToDelete = getStock(id);
		stockToDelete.ifPresent(stockRepository::delete);
	}

	public Produit getStockProduit(Long id) {
		Optional<Stock> stock = getStock(id);
		return stock.map(Stock::getProduit).orElse(null);
	}

	public Fournisseur getStockFournisseur(Long id) {
		Optional<Stock> stock = getStock(id);
		if (stock.isPresent()){
			return stockRepository.getStockFournisseur(id);
		}
		return null;
	}

//	public List<Object> getStockDetails(Long id) {
//
//	}
}
