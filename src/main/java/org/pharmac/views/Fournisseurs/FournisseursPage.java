package org.pharmac.views.Fournisseurs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Fournisseur;
import org.pharmac.services.FournisseurService;
import org.pharmac.views.EditFournisseurPage.EditFournisseurPage;
import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.ConfirmDeletePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Collections;
import java.util.List;

@MountPath("fournisseurs")
public class FournisseursPage extends BasePage {

	private Fournisseur fournisseur = new Fournisseur();
	@SpringBean
	private FournisseurService fournisseurService;

	public FournisseursPage() {
		LoadableDetachableModel loadableDetachableModel = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				List<Fournisseur> fournisseurs = Collections.emptyList();
				fournisseurs = fournisseurService.getFournisseurs();
				return fournisseurs;
			}
		};
//		final List<Fournisseur> fournisseurList = service.getFournisseurs();

		Form<Fournisseur> form = new Form<>("form", new CompoundPropertyModel<>(fournisseur)){
			@Override
			protected void onSubmit() {
				fournisseurService.createOrUpdateFournisseur(fournisseur);
				setModelObject(new Fournisseur());
			}
		};

//		ModalWindow modalWindow = new ModalWindow("deleteModal");
//		add(modalWindow);
//		DeleteFournisseurModal deleteFournisseurModal = new DeleteFournisseurModal("deleteModal");
//		add(deleteFournisseurModal);

//		WebMarkupContainer container = new WebMarkupContainer("containerModal");
//		container.setOutputMarkupId(true);
//		add(container);
//
//		DeleteFournisseurModalPanel deleteFournisseurModal = new DeleteFournisseurModalPanel("deleteModal");
//		add(deleteFournisseurModal);

		ListView<Fournisseur> listView = new ListView<Fournisseur>("fournisseurs-list", loadableDetachableModel) {
			@Override
			protected void populateItem(ListItem<Fournisseur> item) {
				Fournisseur fournisseurRow = item.getModelObject();
				item.add(new Label("id", fournisseurRow.getIdF()));
				item.add(new Label("nom", fournisseurRow.getNomF()));
				item.add(new Label("telephone", fournisseurRow.getTelF()));
				item.add(new Label("email", fournisseurRow.getEmailF()));
				item.add(new Label("adresse", fournisseurRow.getAdresseF()));
				item.add(new Link<>("edit-frn", item.getModel()) {
					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("fournisseurToEditId", fournisseurRow.getIdF());
						setResponsePage(EditFournisseurPage.class, parameters);
					}
				});
				item.add(new AjaxLink<Void>("delete") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						PageParameters parameters = new PageParameters();
						parameters.add("elementType", fournisseurRow.getClass().getSimpleName());
						parameters.add("elementId", fournisseurRow.getIdF());
						setResponsePage(ConfirmDeletePage.class, parameters);
//						fournisseurService.removeFournisseur(fournisseurRow.getIdF());
//						setResponsePage(FournisseursPage.class);
					}

//					@Override
//					protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
//						AjaxCallListener ajaxCallListener = new AjaxCallListener();
//						ajaxCallListener.onPrecondition("return confirm('Êtes-vous sûr ?" + "');");
//						attributes.getAjaxCallListeners().add(ajaxCallListener);
//						super.updateAjaxAttributes(attributes);
//					}
					//					private void showDeleteConfirmation(Long idF) {
//						Optional<Fournisseur> fournisseur = fournisseurService.getFournisseur(idF);
//						if (fournisseur.isPresent()) {
//
//						}
//
//					}
				});
//
			}
		};

		form.add(new RequiredTextField<>("nomF"));
		form.add(new RequiredTextField<>("telF"));
		form.add(new RequiredTextField<>("emailF"));
		form.add(new RequiredTextField<>("adresseF"));

		add(form);
		add(listView);
	}
}
