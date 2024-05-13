package org.pharmac.services;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Objects;
import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.repository.ProduitRepository;
import org.pharmac.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private ProduitRepository produitRepository;

	public List<Stock> getStocks() {
		return stockRepository.findAll();
	}

	public Optional<Stock> getStock(Long id) {
		return stockRepository.findById(id);
	}

	public Stock getStockToEdit(Long id) {
		Optional<Stock> stockOptional = stockRepository.findById(id);
		return stockOptional.orElse(null);
	}

	public List<Object[]> getProduitStockInfo(Long id) {
		return stockRepository.findAllStocksDetailsByProduit(id);
	}

	public List<Stock> getProduitStocks(Long id) {
		Optional<Produit> produit = produitRepository.findById(id);
		if (produit.isPresent()) {
			return stockRepository.findAllStocksByProduit(produit.get().getCodeP());
		} else {
			return Collections.emptyList();
		}
	}

	public Stock createOrUpdateStock(Stock stock) {
		if (stock.getIdStk() != null) {
			Optional<Stock> stockToUpdate = stockRepository.findById(stock.getIdStk());

			if (stockToUpdate.isPresent()) {
				Stock newStock = stockToUpdate.get();
				newStock.setDatePeremption(stock.getDatePeremption());
				newStock.setQuantiteStk(stock.getQuantiteStk());
				newStock.setFournisseur(stock.getFournisseur());
				return stockRepository.save(newStock);
			}
		}
		return stockRepository.save(stock);
	}

//	public Product createOrUpdateProduct(Product product) {
//		if (product.getId() != null) {
//			Optional<Product> optionalProduct = productRepository.findById(product.getId());
//
//			if (optionalProduct.isPresent()) {
//				Product newProduct = optionalProduct.get();
//				newProduct.setName(product.getName());
//				newProduct.setPrice(product.getPrice());
//				// set other fields...
//
//				newProduct = productRepository.save(newProduct);
//
//				return newProduct;
//			}
//		}
//
//		product = productRepository.save(product);
//
//		return product;
//	}

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
			return stockRepository.findFournisseurByStock(id);
		}
		return null;
	}

//	public List<Object> getStockDetails(Long id) {
//
//	}
}
