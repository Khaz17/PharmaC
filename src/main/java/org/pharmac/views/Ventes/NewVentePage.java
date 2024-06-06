package org.pharmac.views.Ventes;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.DetailVente;
import org.pharmac.models.Produit;
import org.pharmac.models.Vente;
import org.pharmac.services.DetailVenteService;
import org.pharmac.services.VenteService;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

@MountPath("vendeur/new-vente")
public class NewVentePage extends BasePage {

	private Vente vente;

	private List<DetailVente> detailVenteList = new ArrayList<>();

	@SpringBean
	private VenteService venteService;

	@SpringBean
	private DetailVenteService detailVenteService;

	public NewVentePage() {
		WebMarkupContainer venteContainer = new WebMarkupContainer("venteContainer");
		venteContainer.setOutputMarkupId(true);
		add(venteContainer);

		SelectionPanel selectionPanel = new SelectionPanel("panel");
		selectionPanel.setOutputMarkupId(true);
		venteContainer.add(selectionPanel);

	}
}
