package org.pharmac.views.Stocks;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.services.FournisseurService;
import org.pharmac.services.StockService;
import org.pharmac.views.Produits.DetailsProduitPage;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@MountPath("edit-stock")
public class EditStocksPage extends BasePage {

	@SpringBean
	private StockService stockService;

	private Stock stock = new Stock();

	@SpringBean
	private FournisseurService fournisseurService;

	public EditStocksPage(PageParameters parameters) {
		StringValue idValue = parameters.get("idParam");
		Stock stockToEdit = stockService.getStockToEdit(idValue.toLong());
		if(stockToEdit != null){
			stock = stockToEdit;
		} else {
			stock = new Stock();
		}
		addComponents(stock);
	}

	private void addComponents(Stock stockToEdit){

		if (stockToEdit != null) {
			Produit produit = stockToEdit.getProduit();
			add(new Label("produitName", produit.getNomCommercial()));
			add(new Label("idStock", stockToEdit.getIdStk()));
			add(new Label("date", stockToEdit.getDateAjout()));
		}

//		stock.setDatePeremption(Local);
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		Form form = new Form<>("form", new CompoundPropertyModel<>(this));
		form.setOutputMarkupId(true);
		add(form);

		form.add(new HiddenField<>("stock.idStk"));
		form.add(new NumberTextField<>("stock.quantiteStk").setRequired(true));
//		Date localDate = stockToEdit.getDatePeremption();
//		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
//		Date date = Date.from(instant);
		DateTextField dateTextField = new DateTextField("stock.datePeremption", "yyyy-MM-dd");
		form.add(dateTextField);

		List<Fournisseur> fournisseurs = fournisseurService.getFournisseurs();
		form.add(new DropDownChoice<Fournisseur>("stock.fournisseur",
				fournisseurs,
				new ChoiceRenderer<>("nomF", "idF")));
		AjaxButton ajaxButton = new AjaxButton("ajaxButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				System.out.println(stock);
				stockService.createOrUpdateStock(stock);
				PageParameters parameters = new PageParameters();
				parameters.add("idProduit", stock.getProduit().getCodeP());
				setResponsePage(DetailsProduitPage.class, parameters);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				super.onError(target);
				target.add(feedbackPanel);
			}
		};
		form.add(ajaxButton);
	}


}
