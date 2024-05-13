package org.pharmac.views.Ventes;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Produit;
import org.pharmac.services.ProduitService;
import org.pharmac.services.StockService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionPanel extends Panel {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private StockService stockService;

	private List<Produit> selectedProduits = new ArrayList<>();

	public SelectionPanel(String id) {
		super(id);

		LoadableDetachableModel loadableProduitsList = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				List<Produit> produits = Collections.emptyList();
				produits = produitService.getProduits();
				return produits;
			}
		};

		ListView<Produit> listView = new ListView<Produit>("listView", loadableProduitsList) {
			@Override
			protected void populateItem(ListItem<Produit> item) {
				Produit produitRow = item.getModelObject();
//				CheckBox checkBox = new CheckBox("select", Model.of(Boolean.FALSE));
//				checkBox.add(new AjaxFormComponentUpdatingBehavior("onclick") {
//					@Override
//					protected void onUpdate(AjaxRequestTarget target) {
//						if (checkBox.getModelObject()) {
//							selectedProduits.add(produitRow);
//							System.out.println(produitRow);
//						} else {
//							selectedProduits.remove(produitRow);
//						}
//					}
//				});
//				item.add(checkBox);
				item.add(new Label("nomCommercial", produitRow.getNomCommercial()));
				item.add(new Label("datePeremption", produitService.getProduitStocks(produitRow.getCodeP()).get(0).getDatePeremption()));
				item.add(new Label("prixUnitaire", produitRow.getPrixUnitaire()));
				item.add(new Label("quantiteStk", produitService.getProduitStockTotal(produitRow.getCodeP())));
				item.add(new Label("categorie", produitRow.getCategorieP().getLibelleCtg()));

				AjaxLink<Void> selectButton = new AjaxLink<Void>("selectButton") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						selectedProduits.add(produitRow);
						System.out.println("Selected product: " + produitRow);
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
						System.out.println("Removed product: " + produitRow);
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
		add(listView);


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
