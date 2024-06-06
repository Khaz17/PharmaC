package org.pharmac.views.Produits;

import lombok.SneakyThrows;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.*;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;
import org.pharmac.models.Categorie;
import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.services.CategorieService;
import org.pharmac.services.FournisseurService;
import org.pharmac.services.ProduitService;
import org.pharmac.services.StockService;
import org.pharmac.views.components.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDateTime;
import java.util.*;

@MountPath("gestionnaire/info-produit")
public class AddOrEditProduitPage extends BasePage {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private FournisseurService fournisseurService;
	@SpringBean
	private CategorieService categorieService;
	@SpringBean
	private StockService stockService;

	private static final Logger logger = LoggerFactory.getLogger(AddOrEditProduitPage.class);

	private Produit produit = new Produit();
	private boolean produitIsSet = false;
	private Stock stock = new Stock();

	private static final List<String> DOSAGE_UNITS = Arrays.asList("mg", "g", "ml", "l", "mcg", "IU");

	private static final List<String> VOIES_ADMINISTRATION = Arrays.asList(
			"Orale", "Intraveineuse", "Intramusculaire", "Sous-cutanée", "Intracardiaque",
			"Intradermique", "Intrathécale", "Intra-articulaire", "Intracérébrale", "Intravitréenne",
			"Intrapéritonéale", "Intrapleurale", "Intranasale", "Inhalation", "Topique",
			"Transdermique", "Ophtalmique", "Othique", "Rectale", "Vaginale",
			"Urétrale", "Buccale", "Sublinguale", "Gingivale", "Dentale",
			"Nasale", "Nasogastrique", "Nasojejunal", "Endobronchique", "Intravesicale",
			"Intraosseuse", "Intralesionale", "Peridurale", "Épidurale", "Intracrânienne",
			"Intra-amniotique", "Intra-utérine", "Intracavitaire", "Intracervicale",
			"Intralymphatique", "Intratumorale", "Intravenocisternale", "Intraosseuse",
			"Intraperitoneale", "Intrathoracique", "Intracystique", "Intraseptale",
			"Intrabiliary", "Intraductal", "Intramyocardique", "Intraovarienne",
			"Intraprostatique", "Intraspinale", "Intratympanique", "Intraventriculaire"
	);
	static {
		Collections.sort(VOIES_ADMINISTRATION);
	}

	private static final List<String> FORMES_GALENIQUES = Arrays.asList(
			"Comprimés", "Comprimés à libération immédiate", "Comprimés à libération prolongée",
			"Comprimés à libération retardée", "Comprimés effervescents", "Comprimés dispersibles",
			"Comprimés orodispersibles", "Comprimés sublinguaux", "Comprimés à croquer",
			"Gélules", "Capsules", "Pilules", "Dragées", "Granulés", "Poudres",
			"Solutions buvables", "Sirops", "Suspensions", "Emulsions", "Elixirs", "Gouttes buvables",
			"Solutions injectables", "Suspensions injectables", "Emulsions injectables",
			"Poudres pour injection (à reconstituer)", "Implants injectables",
			"Crèmes", "Pommades", "Gels", "Lotions", "Solutions pour application cutanée",
			"Emulsions pour application cutanée", "Poudres pour application cutanée",
			"Timbres transdermiques", "Pâtes", "Aérosols doseurs", "Nebulisateurs", "Poudres pour inhalation",
			"Solutions pour inhalation", "Suppositoires", "Ovules", "Crèmes rectales ou vaginales",
			"Gels rectaux ou vaginaux", "Solutions rectales ou vaginales", "Mousse rectale ou vaginale",
			"Collyres (gouttes ophtalmiques)", "Pommades ophtalmiques", "Gels ophtalmiques",
			"Solutions pour irrigation oculaire", "Gouttes auriculaires", "Pommades auriculaires", "Solutions pour irrigation auriculaire",
			"Gouttes nasales", "Sprays nasaux", "Pommades nasales", "Solutions pour irrigation nasale",
			"Solutions buccales", "Pommades buccales", "Pastilles", "Gels buccaux", "Films buccaux", "Gommes à mâcher",
			"Solutions pour perfusion intraveineuse", "Emulsions pour perfusion intraveineuse", "Poudres pour perfusion (à reconstituer)",
			"Liniments", "Poudres à usage externe", "Pâtes dentaires", "Solutions pour irrigation dentaire", "Colles dentaires",
			"Solutions pour irrigation vésicale", "Vaccins", "Sérums", "Immunoglobulines",
			"Solutions radio-pharmaceutiques", "Capsules radio-pharmaceutiques", "Nanoparticules", "Liposomes",
			"Microsphères", "Formes à libération contrôlée et ciblée", "Lyophilisat oral"
	);

	public AddOrEditProduitPage(PageParameters parameters) {

		StringValue idValue = parameters.get("idProduit");

		// vérifie si un id de produit est passé dans la page pour savoir s'il s'agit d'un ajout ou d'une modification
		if (!idValue.isNull()) {
			Long produitToEditId = idValue.toLong();
			produit = produitService.getProduit(produitToEditId).get();
			produitIsSet = true;
		} else {
			produit = new Produit();
		}
		stock.setProduit(produit);

		WebMarkupContainer stockWindow = new WebMarkupContainer("produitContainer");


		LoadableDetachableModel stocksLoadableList = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				List<Stock> stocks = Collections.emptyList();
				stocks = produitService.getProduitStocks(produit.getCodeP());
				return stocks;
			}
		};

		ListView<Stock> stockListView = new ListView<Stock>("stocks-list", stocksLoadableList) {
			@Override
			protected void populateItem(ListItem<Stock> item) {
				Stock stockRow = item.getModelObject();
				item.add(new Label("datePeremption", stockRow.getDatePeremption().toString()));
				item.add(new Label("quantiteStk", stockRow.getQuantiteStk()));
				item.add(new Label("dateAjout", stockRow.getDateAjout()));
			}
		};
		stockWindow.add(stockListView);

		stockWindow.setVisible(produitIsSet);

		add(new FeedbackPanel("feedback"));
		add(stockWindow);
		Form<Produit> produitForm = new Form<>("produit-form", new CompoundPropertyModel<>(produit));
		produitForm.setModelObject(produit);

		produitForm.add(new RequiredTextField<>("nomCommercial"));
		produitForm.add(new RequiredTextField<>("dci"));

		IAutoCompleteRenderer<String> iAutoCompleteRenderer = new AbstractAutoCompleteRenderer<String>() {
			@Override
			protected void renderChoice(String object, Response response, String criteria) {
				String highlighted = highlightMatch(object, criteria);
				response.write("<div class='auto-complete-dropdown-item' style='text-align:left; padding: 5px 10px; width:100%; list-style:none; background-color: #ffffff;'>");
				response.write(highlighted);
				response.write("</div>");
			}

			private String highlightMatch(String text, String query) {
				if (query == null || query.isEmpty()) {
					return text;
				}
				String lowerText = text.toLowerCase();
				String lowerQuery = query.toLowerCase();
				int startIndex = lowerText.indexOf(lowerQuery);
				if (startIndex == -1) {
					return text;
				}
				String highlighted = text.substring(0, startIndex)
						+ "<strong>" + text.substring(startIndex, startIndex + query.length()) + "</strong>"
						+ text.substring(startIndex + query.length());
				return highlighted;
			}

			@Override
			protected String getTextValue(String object) {
				return object;
			}
		};

		TextField formeGaleniqueTextField = new AutoCompleteTextField<String>("formeGalenique", iAutoCompleteRenderer) {
			private int currentIndex = -1;

			@SneakyThrows
			@Override
			protected Iterator<String> getChoices(String input) {
				if (input == null || input.trim().isEmpty()) {
					return Collections.emptyIterator();
				}

				List<String> completions = new ArrayList<>();
				for (String s : FORMES_GALENIQUES) {
					if (s.toLowerCase().contains(input.toLowerCase())) {
						completions.add(s);
					}
				}

				completions.sort((a, b) -> {
					int aIndex = a.toLowerCase().indexOf(input.toLowerCase());
					int bIndex = b.toLowerCase().indexOf(input.toLowerCase());
					if (aIndex != bIndex) {
						return Integer.compare(aIndex, bIndex);
					} else {
						return a.compareToIgnoreCase(b);
					}
				});

				currentIndex = -1; // Reset index whenever the input changes
				return completions.iterator();
			}

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("autocomplete", "off");
				tag.put("onkeydown", "handleKeyDown(event)");
			}
		};

		produitForm.add(formeGaleniqueTextField);




		RequiredTextField<String> dosageTextfield = new RequiredTextField<>("dosage");
		produitForm.add(dosageTextfield);
		produitForm.add(new NumberTextField<>("nbreComprimes"));

		DropDownChoice<String> voieDropdrown = new DropDownChoice<String>("voieAdministration", VOIES_ADMINISTRATION);
		voieDropdrown.setNullValid(true);
		produitForm.add(voieDropdrown);

		produitForm.add(new NumberTextField<>("prixUnitaire").setRequired(true));
		produitForm.add(new TextField<>("descriptionP"));

		List<Categorie> categories = categorieService.getCategories();
		produitForm.add(new DropDownChoice<Categorie>("categorieP",
				categories,
				new ChoiceRenderer<>("libelleCtg", "numCtg")).setRequired(true));
		produitForm.add(new Button("submit-produit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				String dosageInput = dosageTextfield.getModelObject();
				if (dosageInput != null) {
					dosageInput = dosageInput.trim();
					String[] parts = dosageInput.split("\\s+");
					if (parts.length == 2) {
						try {
							double value = Double.parseDouble(parts[0]);
							String unit = parts[1].toLowerCase();
							if (DOSAGE_UNITS.contains(unit)) {
								String normalizedDosage = value + " " + unit;
								dosageTextfield.setModelObject(normalizedDosage);
							} else {
								error("L'unité du dosage est invalide. Vous devez utiliser l'une de celles-ci : " + DOSAGE_UNITS);
							}
						} catch (NumberFormatException e) {
							error("La valeur du dosage est invalide. Veuillez entrer un chiffe valide");
						}
					} else {
						error("Le format du dosage est invalide. '500 mg' ou '1.5 g' par exemple sont des valeurs valides");
					}
				} else {
					error("Ce champ ne peut pas être vide");
				}

				String voieChoisie = voieDropdrown.getModelObject();
				if (voieChoisie == null || voieChoisie.isEmpty()) {
					error("Veuillez sélectionner une voie d'administration");
				}

				produit.setNomComplet(
						produit.getNomCommercial() + "-" +
								produit.getDosage().replace(" ", "") + "-" +
								shortenFormeGalenique(produit.getFormeGalenique().toUpperCase())
				);
				produitService.createOrUpdateProduit(produit);
				logger.info("Le produit {} a été créé", produit.getNomCommercial());
				produitIsSet = true;
				stockWindow.setVisible(produitIsSet);
			}
		});
		add(produitForm);


		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);

		Form<Stock> stockForm = new Form<>("stock-form", new CompoundPropertyModel<>(stock));
		stockForm.add(new DateTextField("datePeremption", "yyyy-MM-dd"));
		stockForm.add(new NumberTextField<>("quantiteStk").setRequired(true));
		container.add(stockForm);
		container.add(feedbackPanel);
		stockForm.add(new AjaxButton("submit-stock") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				produitForm.setEnabled(false);
				stock.setDateAjout(LocalDateTime.now());
				stockService.createOrUpdateStock(stock);
				target.add(container);
				stock = new Stock();
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				super.onError(target);
				target.add(feedbackPanel);
			}
		});
		List<Fournisseur> fournisseurs = fournisseurService.getFournisseurs();
		stockForm.add(new DropDownChoice<Fournisseur>("fournisseur",
				fournisseurs,
				new ChoiceRenderer<>("nomF", "idF")));

	}

	private String shortenFormeGalenique(String formeGalenique) {
		String[] words = formeGalenique.split("\\s+");
		StringBuilder abbreviation = new StringBuilder();
		for (String word : words) {
			if (word.length() >= 3) {
				abbreviation.append(word, 0, 3);
			} else {
				abbreviation.append(word);
			}
		}
		return abbreviation.toString().toUpperCase();
	}

}
