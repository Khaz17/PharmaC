package org.pharmac.views.components;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Produit;
import org.pharmac.services.ProduitService;

public abstract class ProduitsModalPanel extends Panel {

	@SpringBean
	private ProduitService produitService;

	public ProduitsModalPanel(String id) {
		super(id);

		ListView<Produit> listView = new ListView<Produit>("produits-list", produitService.getProduits()) {
			@Override
			protected void populateItem(ListItem<Produit> item) {
				Produit produitRow = item.getModelObject();
				item.add(new Label("id", produitRow.getCodeP()));
				item.add(new Label("nomCommercial", produitRow.getNomCommercial()));
				item.add(new Label("dci", produitRow.getDci()));
				item.add(new Label("prixUnitaire", produitRow.getPrixUnitaire()));
				item.add(new Label("voieAdministration", produitRow.getVoieAdministration()));
				item.add(new Label("stockActuel", produitService.getProduitStockTotal(produitRow.getCodeP())));
				item.add(new AjaxLink<Void>("select-produit") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						onSelected(target, produitRow);
					}
				});
				item.setOutputMarkupPlaceholderTag(true);
			}
		};

		add(listView);
	}

	public abstract void onSelected(AjaxRequestTarget target, Produit produit);

}
