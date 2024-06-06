package org.pharmac.views.Utilisateurs;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@MountPath("admin/edit-utilisateur")
public class EditUtilisateurPage extends BasePage {

	@SpringBean
	private UtilisateurService utilisateurService;

	private Utilisateur utilisateurToEdit;

	public EditUtilisateurPage(PageParameters parameters) {
		Long userId = parameters.get("userId").toLong();
		Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(userId);
		if (utilisateur.isPresent()) {
			utilisateurToEdit = utilisateur.get();

			add(new Label("id", utilisateurToEdit.getId()));

			Form form = new Form<>("form", new CompoundPropertyModel<>(utilisateurToEdit)) {
				@Override
				protected void onSubmit() {
					utilisateurService.updateUtilisateur(utilisateurToEdit);
					setModelObject(new Utilisateur());
				}
			};

			form.add(new TextField<>("username"));
			form.add(new TextField<>("nomU"));
			form.add(new TextField<>("prenomU"));
			form.add(new EmailTextField("emailU"));
			form.add(new TextField<>("telU"));
			form.add(new TextField<>("adresseU"));

			List<Boolean> choices = Arrays.asList(true, false);

			IChoiceRenderer<Boolean> renderer = new IChoiceRenderer<>() {
				@Override
				public Object getDisplayValue(Boolean object) {
					return object ? "Actif" : "Désactivé";
				}

				@Override
				public String getIdValue(Boolean object, int index) {
					return object.toString();
				}

				@Override
				public Boolean getObject(String id, IModel<? extends List<? extends Boolean>> choices) {
					return Boolean.valueOf(id);
				}
			};

			DropDownChoice<Boolean> dropDownChoice = new DropDownChoice<>("actif", choices, renderer);
			form.add(dropDownChoice);

			add(form);
		}
	}
}
