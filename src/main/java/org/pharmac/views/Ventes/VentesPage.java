package org.pharmac.views.Ventes;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.pharmac.models.Vente;
import org.pharmac.services.DetailVenteService;
import org.pharmac.services.PharmacySettingsService;
import org.pharmac.services.VenteService;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

@MountPath("vendeur/ventes")
public class VentesPage extends BasePage {

	@SpringBean
	private VenteService venteService;

	@SpringBean
	private DetailVenteService detailVenteService;

	@SpringBean
	private PharmacySettingsService pharmacySettingsService;

	public VentesPage() {
		super();

		add(new Link<>("new-vente") {
			@Override
			public void onClick() {
				setResponsePage(NewVentePage.class);
			}
		});

		add(new Label("nbreVentes", venteService.getVentes().size()));

		LoadableDetachableModel loadableVentesList = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				List<Vente> ventes = Collections.emptyList();
				ventes = venteService.getVentes();
				return ventes;
			}
		};

		ListView<Vente> listView = new ListView<Vente>("listview", loadableVentesList) {
			@Override
			protected void populateItem(ListItem<Vente> item) {
				Vente venteRow = item.getModelObject();
				item.add(new Label("idVente", venteRow.getIdV()));
				item.add(new Label("dateVente", venteRow.getDateVente()));
				String userToDisplay = venteRow.getUtilisateur() != null ? venteRow.getUtilisateur().getNomU() + " " + venteRow.getUtilisateur().getPrenomU() : "N/A";
				item.add(new Label("realiseePar", userToDisplay));
				item.add(new Label("total", venteRow.getTotal()));
				item.add(new Link<>("print") {
					@Override
					public void onClick() {
						IResourceStream resourceStream = new AbstractResourceStreamWriter() {
							@Override
							public void write(OutputStream output) throws IOException {
								try {
									InvoicePDFPrinter invoicePrinter = new InvoicePDFPrinter(venteRow, pharmacySettingsService, detailVenteService);
									invoicePrinter.export(output);
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}
						};

						ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(resourceStream);
						handler.setFileName("recu"+venteRow.getIdV()+venteRow.getDateVente()+".pdf");
						handler.setCacheDuration(Duration.NONE);
						handler.setContentDisposition(ContentDisposition.ATTACHMENT);
						getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
					}
				});
				item.add(new Link<>("view-vente") {
					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("idVente", venteRow.getIdV());
						setResponsePage(InvoicePage.class, parameters);
					}
				});
			}
		};
		add(listView);

	}
}
