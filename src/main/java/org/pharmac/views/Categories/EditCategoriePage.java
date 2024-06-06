package org.pharmac.views.Categories;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Categorie;
import org.pharmac.services.CategorieService;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Optional;

@MountPath("admin/edit-categorie")
public class EditCategoriePage extends BasePage {

	private Categorie categorie = new Categorie();

	@SpringBean
	private CategorieService service;

	public EditCategoriePage(PageParameters parameters) {
		Optional<Categorie> categorieToEdit = Optional.of(new Categorie());
		if (!parameters.isEmpty()) {
			StringValue categorieToEditIdValue = parameters.get("categorieToEditId");
			Long categorieToEditId = categorieToEditIdValue.toLong();
			categorieToEdit = service.getCategorie(categorieToEditId);
			categorieToEdit.ifPresent(value -> categorie = value);
		}

		Form<Categorie> form = new Form<>("form", new CompoundPropertyModel<>(categorie)) {
			@Override
			protected void onSubmit() {
				service.createOrUpdateCategorie(categorie);
			}
		};
		form.add(new HiddenField<>("numCtg"));
		form.add(new RequiredTextField<>("libelleCtg"));
		form.add(new TextField<>("descriptionCtg"));
		add(form);

		categorieToEdit.ifPresent(form::setModelObject);
	}
}
