package org.pharmac.views.Ventes;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.services.ProduitService;
import org.pharmac.services.StockService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionPanel extends Panel {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private StockService stockService;

	private List<Produit> selectedProduits = new ArrayList<>();

	private String searchQuery = "";

	public SelectionPanel(String id) {
		super(id);

		LoadableDetachableModel<List<Produit>> loadableProduitsList = new LoadableDetachableModel<List<Produit>>() {
			@Override
			protected List<Produit> load() {
				if (searchQuery.isEmpty()) {
					return produitService.getProduitsDisponibles();
				} else {
					return produitService.searchDisponiblesProduits(searchQuery);
				}
			}
		};

		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		PageableListView<Produit> listView = new PageableListView<Produit>("listView", loadableProduitsList, 5) {
			@Override
			protected void populateItem(ListItem<Produit> item) {
				Produit produitRow = item.getModelObject();
				item.add(new Label("nomCommercial", produitRow.getNomCommercial()));

				Stock stock = produitService.getProduitStocks(produitRow.getCodeP()).get(0);
				SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
				String datePeremption = formatter.format(stock.getDatePeremption());
				item.add(new Label("datePeremption", datePeremption));

				item.add(new Label("prixUnitaire", produitRow.getPrixUnitaire()));
				item.add(new Label("quantiteStk", produitService.getProduitStockTotal(produitRow.getCodeP())));
				item.add(new Label("categorie", produitRow.getCategorieP().getLibelleCtg()));

				AjaxLink<Void> selectButton = new AjaxLink<Void>("selectButton") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						selectedProduits.add(produitRow);
						this.setVisible(false);
						target.add(this);
						AjaxLink<Void> deselectButton = (AjaxLink<Void>) item.get("deselectButton");
						deselectButton.setVisible(true);
						target.add(deselectButton);
					}
				};
				item.add(selectButton.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

				AjaxLink<Void> deselectButton = new AjaxLink<Void>("deselectButton") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						selectedProduits.remove(produitRow);
						this.setVisible(false);
						target.add(this);
						selectButton.setVisible(true);
						target.add(selectButton);
					}
				};
				deselectButton.setVisible(false);
				item.add(deselectButton.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

			}
		};
		listView.setOutputMarkupId(true);
		container.add(listView);

		add(new AjaxPagingNavigator("pagingNavigator", listView));

		Form<?> searchForm = new Form<>("searchForm", new CompoundPropertyModel<>(this));
		TextField<String> searchField = new TextField<>("searchQuery");
		searchForm.add(searchField);
		searchForm.add(new AjaxButton("searchButton", searchForm) {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(container);
			}
		});
		add(searchForm);

		add(new AjaxLink<Void>("send-selection") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ConfirmationPanel confirmationPanel = new ConfirmationPanel("panel", selectedProduits);
				confirmationPanel.setOutputMarkupId(true);
				SelectionPanel.this.replaceWith(confirmationPanel);
				target.add(confirmationPanel);
			}
		});

	}

}
