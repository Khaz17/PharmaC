package org.pharmac.views.Ventes;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.time.Duration;
import org.pharmac.models.DetailVente;
import org.pharmac.models.Vente;
import org.pharmac.services.DetailVenteService;
import org.pharmac.services.VenteService;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@MountPath("invoice")
public class InvoicePage extends BasePage {

	@SpringBean
	private VenteService venteService;

	@SpringBean
	private DetailVenteService detailVenteService;

	public InvoicePage(PageParameters parameters) {
		StringValue idVente = parameters.get("idVente");
		Optional<Vente> vente = venteService.getVente(idVente.toLong());
		if (vente.isPresent()) {
			add(new Label("idVente", vente.get().getIdV()));

			DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
			add(new Label("dateVente", vente.get().getDateVente().format(df)));

			DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm:ss");
			add(new Label("heureVente", vente.get().getDateVente().format(hf)));

			String nameToDisplay = vente.get().getNomClient() != null ? vente.get().getNomClient() : "N/A";
			add(new Label("nomClient", nameToDisplay));
//			add(new Label("remise", vente.get().getRemise()));
			add(new Label("total", vente.get().getTotal()));
//			add(new Label("sommeRendue", vente.get().getSommeRendue()));


			List<DetailVente> detailVenteList = detailVenteService.getDetailsVenteByVente(vente.get());
			vente.get().setDetailVenteList(detailVenteList);
			ListView<DetailVente> listView = new ListView<>("detailListView", detailVenteList) {
				@Override
				protected void populateItem(ListItem<DetailVente> item) {
					DetailVente detailVenteRow = item.getModelObject();
					item.add(new Label("codeP", detailVenteRow.getProduit().getCodeP()));
					item.add(new Label("nomProduit", detailVenteRow.getProduit().getNomCommercial()));
					item.add(new Label("quantiteVendue", detailVenteRow.getQuantiteVendue()));
					item.add(new Label("prixUnitaire", detailVenteRow.getPrixUnitaire()));
					item.add(new Label("detailTotal", detailVenteRow.getQuantiteVendue() * detailVenteRow.getPrixUnitaire()));
				}
			};
			add(listView);

			add(new Link<Void>("print") {
				@Override
				public void onClick() {
					IResourceStream resourceStream = new AbstractResourceStreamWriter() {
						@Override
						public void write(OutputStream output) throws IOException {
							try {

								InvoicePDFPrinter invoicePrinter = new InvoicePDFPrinter(vente.get());
								invoicePrinter.export(output);
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
					};

					ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(resourceStream);
					handler.setFileName("recu"+vente.get().getIdV()+vente.get().getDateVente()+".pdf");
					handler.setCacheDuration(Duration.NONE);
					handler.setContentDisposition(ContentDisposition.ATTACHMENT);
					getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
				}
			});
//			add(new Button("print") {
//				@Override
//				public void onSubmit() {
//
//				}
//			});
			add(new Link<>("list-ventes") {
				@Override
				public void onClick() {
//				setResponsePage();
				}
			});
		}


	}
}
