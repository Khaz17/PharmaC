package org.pharmac.views.Ventes;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.DetailVente;
import org.pharmac.models.Produit;
import org.pharmac.models.Utilisateur;
import org.pharmac.models.Vente;
import org.pharmac.services.UtilisateurService;
import org.pharmac.services.VenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConfirmationPanel extends Panel {

	private Vente vente = new Vente();

	@SpringBean
	private VenteService venteService;

	@SpringBean
	private UtilisateurService utilisateurService;

	private static final Logger logger = LoggerFactory.getLogger(ConfirmationPanel.class);

	String username = SecurityContextHolder.getContext().getAuthentication().getName();
	Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByUsername(username).get();

	public ConfirmationPanel(String id, List<Produit> selectedProduits) {
		super(id);

		List<DetailVente> detailVenteList = new ArrayList<>();

		for(Produit produit : selectedProduits) {
			DetailVente detailVente = new DetailVente();
			detailVente.setProduit(produit);
			detailVente.setPrixUnitaire(produit.getPrixUnitaire());
			detailVente.setQuantiteVendue(1);
			detailVenteList.add(detailVente);
		}

		WebMarkupContainer detailContainer = new WebMarkupContainer("detailContainer");
		detailContainer.setOutputMarkupId(true);
		add(detailContainer);

		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		Form form = new Form<>("form", new CompoundPropertyModel<>(this));
		form.setOutputMarkupId(true);

		//remise, somme rendue, nom client
		RequiredTextField nomClient = new RequiredTextField("vente.nomClient");
		nomClient.setLabel(Model.of("Nom du client")).setOutputMarkupId(true);
		nomClient.add(new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				vente.setNomClient((String) getComponent().getDefaultModelObject());
			}
		});
		form.add(nomClient);

		ListView detailListView = new ListView<DetailVente>("detailVenteList", detailVenteList) {
			@Override
			protected void populateItem(ListItem<DetailVente> item) {

				TextField produitName = new TextField("produit-name", new Model(item.getModelObject().getProduit().getNomCommercial()));
				produitName.setEnabled(false);
				item.add(produitName);

				NumberTextField prixTxt = new NumberTextField("prixUnitaire", new PropertyModel(item.getModelObject(), "prixUnitaire"));
				prixTxt.setLabel(Model.of("Prix unitaire"));
				prixTxt.setModelObject(item.getModelObject().getPrixUnitaire());
				prixTxt.setRequired(true);
				prixTxt.setEnabled(false);
				item.add(prixTxt);

				NumberTextField quantiteTxt = new NumberTextField("quantiteVendue", new PropertyModel(item.getModelObject(), "quantiteVendue"));
				quantiteTxt.setLabel(Model.of("Quantité vendue"));
				quantiteTxt.setRequired(true);
				quantiteTxt.setMinimum(1);
				quantiteTxt.add(new OnChangeAjaxBehavior() {
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						item.getModelObject().setQuantiteVendue((Integer) getComponent().getDefaultModelObject());
					}
				});
				item.add(quantiteTxt);

				item.add(new AjaxLink<Void>("removeLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						detailVenteList.remove(item.getIndex());
						target.add(detailContainer);
					}
				});
			}
		};
		form.add(detailListView);
		AjaxLink submitLink = new AjaxLink<>("submit-link") {

			@Override
			public void onClick(AjaxRequestTarget target) {
//				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//				if (principal instanceof UserDetails) {
//					String username = ((UserDetails) principal).getUsername();
//					Utilisateur utilisateur = utilisateurService.getUtilisateurByUsername(username).get();
//
//				} else {
//					System.out.println("Le principal n'est pas une instance d'UserDetails");
//				}
				vente.setUtilisateur(utilisateurConnecte);
				vente.setDetailVenteList(detailVenteList);
				vente.setDateVente(LocalDateTime.now());
				vente.setTotal(detailVenteList.stream().mapToDouble(detail -> detail.getPrixUnitaire() * detail.getQuantiteVendue()).sum());
//				System.out.println(vente);
				venteService.saveVente(vente);
				Vente newVente = venteService.getNewVente();
				logger.info("La vente {} a été réalisée par l'utilisateur {}", newVente.getIdV(), newVente.getUtilisateur().getNomU());

				PageParameters parameters = new PageParameters();
				parameters.add("idVente", newVente.getIdV());
				setResponsePage(InvoicePage.class, parameters);
			}
		};
		form.add(submitLink);

		form.add(new AjaxLink<>("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SelectionPanel selectionPanel = new SelectionPanel("panel");
				selectionPanel.setOutputMarkupId(true);
				ConfirmationPanel.this.replaceWith(selectionPanel);
				target.add(selectionPanel);
			}
		});
		detailContainer.add(form);

	}

}

