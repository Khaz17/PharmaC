package org.pharmac.views.Categories;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Categorie;
import org.pharmac.services.CategorieService;
import org.pharmac.views.components.ConfirmDeletePage;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Collections;
import java.util.List;

@MountPath("categories")
public class CategoriesPage extends BasePage {

	private Categorie categorie = new Categorie();
	@SpringBean
	private CategorieService categorieService;

	public CategoriesPage() {
		LoadableDetachableModel loadableDetachableModel = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				List<Categorie> categories = Collections.emptyList();
				categories = categorieService.getCategories();
				return categories;
			}
		};

		ListView<Categorie> listView = new ListView<Categorie>("categories-list", loadableDetachableModel) {
			@Override
			protected void populateItem(ListItem<Categorie> item) {
				Categorie categorieRow = item.getModelObject();
				item.add(new Label("id", categorieRow.getNumCtg()));
				item.add(new Label("libelle", categorieRow.getLibelleCtg()));
				item.add(new Label("description", categorieRow.getDescriptionCtg()));
				item.add(new Link<Categorie>("edit-ctg", item.getModel()) {
					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("categorieToEditId", categorieRow.getNumCtg());
						setResponsePage(EditCategoriePage.class, parameters);
					}
				});
				item.add(new Link<Void>("delete") {
					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("elementType", categorieRow.getClass().getSimpleName());
						parameters.add("elementId", categorieRow.getNumCtg());
						setResponsePage(ConfirmDeletePage.class, parameters);
					}
				});
			}
		};
		add(listView);

		Form<Categorie> form = new Form<>("form", new CompoundPropertyModel<>(categorie)) {
			@Override
			protected void onSubmit() {
				categorieService.createOrUpdateCategorie(categorie);
			}
		};

		form.add(new RequiredTextField<>("libelleCtg"));
		form.add(new TextField<>("descriptionCtg"));

		add(form);
	}
}
