package org.pharmac.views.Stocks;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.services.FournisseurService;
import org.pharmac.services.ProduitService;
import org.pharmac.services.StockService;
import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.ProduitsModalPanel;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDateTime;
import java.util.List;

@MountPath("gestionnaire/new-stock")
public class NewStocksPage extends BasePage {

	@SpringBean
	private StockService stockService;

	@SpringBean
	private FournisseurService fournisseurService;

	private Produit produit = new Produit();

	@SpringBean
	private ProduitService produitService;

	private Stock stock = new Stock();

	public NewStocksPage(PageParameters parameters) {

		StringValue idValue = parameters.get("idProduit");

		WebMarkupContainer container = new WebMarkupContainer("stocksContainer");
		container.setOutputMarkupId(true);

		FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);

		Form<Stock> form = new Form<>("form", new CompoundPropertyModel<>(stock));

		IModel<String> model = new Model<>("");
		TextField<String> produitName = new TextField<>("produit-name", model);
		produitName.setEnabled(false);
		produitName.setOutputMarkupId(true);

		// vérifie si un id de produit est passé dans la page pour savoir s'il s'agit d'un ajout ou d'une modification
		if (!idValue.isNull()) {
			Long produitToEditId = idValue.toLong();
			produit = produitService.getProduit(produitToEditId).get();
			stock.setProduit(produit);
			model.setObject(produit.getNomCommercial());
		}

		form.add(produitName);
		form.add(new DateTextField("datePeremption", "yyyy-MM-dd"));
		form.add(new NumberTextField<>("quantiteStk").setRequired(true));

		List<Fournisseur> fournisseurs = fournisseurService.getFournisseurs();
		form.add(new DropDownChoice<Fournisseur>("fournisseur",
				fournisseurs,
				new ChoiceRenderer<>("nomF", "idF")));
		form.add(new AjaxButton("submit-stock") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				stock.setDateAjout(LocalDateTime.now());
				stockService.createOrUpdateStock(stock);
				stock = new Stock();
				target.add(container);
				setResponsePage(NewStocksPage.class);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				super.onError(target);
				target.add(feedbackPanel);
			}
		});

		container.add(form);
		container.add(feedbackPanel);

		ProduitsModalPanel produitsModalPanel = new ProduitsModalPanel("produitspanel") {
			@Override
			public void onSelected(AjaxRequestTarget target, Produit prd) {
				produit = prd;
				stock.setProduit(produit);
				model.setObject(produit.getNomCommercial());
				target.add(container);
				target.add(produitName);
			}
		};

		add(container);
		add(produitsModalPanel);

	}
}
