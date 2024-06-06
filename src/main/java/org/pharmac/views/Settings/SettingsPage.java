package org.pharmac.views.Settings;

import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.PharmacySettings;
import org.pharmac.services.PharmacySettingsService;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Optional;

@MountPath("admin/settings")
public class SettingsPage extends BasePage {

	@SpringBean
	private PharmacySettingsService pharmacyService;

	private PharmacySettings settings = new PharmacySettings();

	public SettingsPage() {

		Optional<PharmacySettings> pharmacySettings = pharmacyService.getPharmacySettings();

		pharmacySettings.ifPresent(value -> settings = value);

		add(new FeedbackPanel("feedback"));

		Form<PharmacySettings> form = new Form<>("form", new CompoundPropertyModel<>(settings)) {
			@Override
			protected void onSubmit() {
				pharmacyService.saveOrUpdatePharmacy(settings);
			}
		};

		form.add(new TextField<>("nomPharma"));
		form.add(new TextField<>("adressePharma"));
		form.add(new TextField<>("telPharma"));
		form.add(new EmailTextField("emailPharma"));
		form.add(new TextArea<>("descriptionPharma"));
//		form.add(new TextField<>("theme"));

//		List<String> choices = Arrays.asList("Clair", "Sombre");
//
//		IChoiceRenderer<String> renderer = new IChoiceRenderer<>() {
//			@Override
//			public Object getDisplayValue(String object) {
//				return object;
//			}
//
//			@Override
//			public String getIdValue(String object, int index) {
//				return object;
//			}
//
//			@Override
//			public String getObject(String id, IModel<? extends List<? extends String>> choices) {
//				for (String choice : choices.getObject()) {
//					if (choice.equals(id)) {
//						return choice;
//					}
//				}
//				return null;
//			}
//
//		};
//
//		DropDownChoice<String> dropDownChoice = new DropDownChoice<>("actif", choices, renderer);
//		form.add(dropDownChoice);

		PropertyModel<String> themeModel = new PropertyModel<>(settings, "theme");

		RadioGroup<String> radioGroup = new RadioGroup<>("themeGroup", themeModel);

		radioGroup.add(new Radio<>("themeClair", new Model<>("Clair")));
		radioGroup.add(new Radio<>("themeSombre", new Model<>("Sombre")));

		form.add(radioGroup);
		add(form);
	}
}
