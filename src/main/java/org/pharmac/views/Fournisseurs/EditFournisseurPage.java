package org.pharmac.views.Fournisseurs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Fournisseur;
import org.pharmac.services.FournisseurService;
import org.pharmac.views.components.BasePage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Optional;

@MountPath("admin/edit-fournisseur")
public class EditFournisseurPage extends BasePage {

	private Fournisseur fournisseur = new Fournisseur();

	@SpringBean
	private FournisseurService service;

	public EditFournisseurPage(PageParameters parameters) {
		Optional<Fournisseur> fournisseurToEdit = Optional.of(new Fournisseur());
		if (!parameters.isEmpty()) {
			StringValue fournisseurToEditId = parameters.get("fournisseurToEditId");
			Long fournisseurId = fournisseurToEditId.toLong();
			fournisseurToEdit = service.getFournisseur(fournisseurId);
			fournisseurToEdit.ifPresent(value -> fournisseur = value);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Form<Fournisseur> form = new Form<>("form", new CompoundPropertyModel<>(fournisseur)) {
			@Override
			protected void onSubmit() {
				service.createOrUpdateFournisseur(fournisseur);
			}

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
			}
		};

		form.add(new HiddenField<>("idF"));
		form.add(new RequiredTextField<>("nomF"));
		form.add(new RequiredTextField<>("telF"));
		form.add(new RequiredTextField<>("adresseF"));
		form.add(new RequiredTextField<>("emailF"));
		add(form);
		fournisseurToEdit.ifPresent(form::setModelObject);
		WebMarkupContainer container = new WebMarkupContainer("containerFournisseur");
		container.setOutputMarkupId(true);

		FournisseurModalPanel fournisseurModalPanel = new FournisseurModalPanel("fournisseurpanel") {
			@Override
			public void onSelected(AjaxRequestTarget target, Fournisseur frn) {
				fournisseur = frn;
				form.setModelObject(fournisseur);
				target.add(container);
			}
		};

		container.add(form);
		add(container);
		add(fournisseurModalPanel);
	}
}
