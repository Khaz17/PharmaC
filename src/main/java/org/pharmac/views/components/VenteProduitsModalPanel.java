package org.pharmac.views.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.pharmac.models.Produit;

import java.util.function.BiConsumer;

public class VenteProduitsModalPanel extends ProduitsModalPanel {
	private BiConsumer<AjaxRequestTarget, Produit> onSelectedAction;

	public VenteProduitsModalPanel(String id) {
		super(id);
	}

	public void setOnSelectedAction(BiConsumer<AjaxRequestTarget, Produit> onSelectedAction) {
		this.onSelectedAction = onSelectedAction;
	}

	@Override
	public void onSelected(AjaxRequestTarget target, Produit produit) {
		if (onSelectedAction != null) {
			onSelectedAction.accept(target, produit);
		}
	}
}
