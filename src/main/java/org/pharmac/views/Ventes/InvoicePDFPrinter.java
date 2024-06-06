package org.pharmac.views.Ventes;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.DetailVente;
import org.pharmac.models.PharmacySettings;
import org.pharmac.models.Vente;
import org.pharmac.services.DetailVenteService;
import org.pharmac.services.PharmacySettingsService;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class InvoicePDFPrinter {
	private Vente vente;

	@SpringBean
	private DetailVenteService detailVenteService;

	@SpringBean
	private PharmacySettingsService pharmacySettingsService;

	public InvoicePDFPrinter(Vente vente, PharmacySettingsService pharmacySettingsService, DetailVenteService detailVenteService) {
		this.vente = vente;
		this.pharmacySettingsService = pharmacySettingsService;
		this.detailVenteService = detailVenteService;
	}

	public void export(OutputStream output) throws DocumentException, IOException {
		List<DetailVente> detailVenteList = detailVenteService.getDetailsVenteByVente(vente);
		vente.setDetailVenteList(detailVenteList);
//		Document document = new Document(PageSize.A4);
		Rectangle pageSize = new Rectangle(226, 366);
		Document document = new Document(pageSize, 10, 10, 20, 20);

		PdfWriter.getInstance(document, output);
		document.open();

//		document.add(new Paragraph("Reçu pharmacie"));

//		// Ajouter le logo
//		Image logo = Image.getInstance("C:/Users/ASUS TUF/Downloads/_696d8832-6904-48e2-82bf-fb1b86ae7af6.jpeg");
//		logo.scaleToFit(50, 50);  // Redimensionner l'image si nécessaire
//		document.add(logo);

		// Créez une police pour le titre
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, Color.BLACK);
		// Créez une police pour les sous-titres
		Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 4, Color.BLACK);

		PharmacySettings pharmacySettings = pharmacySettingsService.getPharmacySettings().get();

		// Ajoutez le nom de la pharmacie en tant que titre
		Paragraph pharmacyName = new Paragraph("Pharmacie " + pharmacySettings.getNomPharma(), titleFont);
		pharmacyName.setAlignment(Element.ALIGN_CENTER);
		document.add(pharmacyName);

		// Ajoutez l'adresse de la pharmacie en tant que sous-titre
		Paragraph pharmacyAddress = new Paragraph(pharmacySettings.getAdressePharma(), subtitleFont);
		pharmacyAddress.setAlignment(Element.ALIGN_CENTER);
		document.add(pharmacyAddress);

		// Ajoutez le numéro de téléphone de la pharmacie en tant que sous-titre
		Paragraph pharmacyPhone = new Paragraph("Téléphone : " + pharmacySettings.getTelPharma(), subtitleFont);
		pharmacyPhone.setAlignment(Element.ALIGN_CENTER);
		document.add(pharmacyPhone);
		document.add(Chunk.NEWLINE);

		PdfPTable table = new PdfPTable(4);
		PdfPCell cell;

		FontFactory.register("C:/WINDOWS/FONTS/TREBUC.TTF", "Trebuchet MS");
		Font headFont = FontFactory.getFont("Trebuchet MS", 8, Font.BOLD);
		Font littleFont = FontFactory.getFont("Trebuchet MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 5, Font.NORMAL);
		cell = new PdfPCell(new Phrase("Reçu nº " + vente.getIdV().toString(), headFont));
		cell.setColspan(4);
		table.setSpacingAfter(20);
		cell.setBackgroundColor(new Color(175, 175, 175));
		cell.setPadding(8f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);

		Font font = FontFactory.getFont("Trebuchet MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7, Font.NORMAL);
		Font priceFont = FontFactory.getFont("Trebuchet MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9, Font.NORMAL);
		//		Font ff = FontFactory.getFont("Trebuchet MS")
		table.addCell(new PdfPCell(new Phrase("Nom du client", font)));
		table.addCell(new PdfPCell(new Phrase("Date vente", font)));
		table.addCell(new PdfPCell(new Phrase("Heure vente", font)));
		table.addCell(new PdfPCell(new Phrase("Réalisée par", font)));

		String clientToDisplay = vente.getNomClient() != null ? vente.getNomClient().toString() : "N/A";
		table.addCell(new Phrase(clientToDisplay, font));
		DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
		table.addCell(new Phrase(vente.getDateVente().format(df), littleFont));
		DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm:ss");
		table.addCell(new Phrase(vente.getDateVente().format(hf), font));
		String userToDisplay = vente.getUtilisateur() != null ? vente.getUtilisateur().getNomU() + " " + vente.getUtilisateur().getPrenomU() : "N/A";
		table.addCell(new Phrase(userToDisplay, littleFont));


		PdfPTable table2 = new PdfPTable(4);
		cell = new PdfPCell(new Phrase("Produits", headFont));
		cell.setColspan(4);
		table2.setSpacingAfter(20);
		cell.setBackgroundColor(new Color(175, 175, 175));
		cell.setPadding(8f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table2.addCell(cell);
		table2.addCell(new PdfPCell(new Phrase("Nom", font)));
		table2.addCell(new PdfPCell(new Phrase("Prix", font)));
		table2.addCell(new PdfPCell(new Phrase("Qté", font)));
		PdfPCell ttl = new PdfPCell(new Phrase("Total (FCFA)", font));
		ttl.setNoWrap(true);
		table2.addCell(ttl);

		for(DetailVente item : vente.getDetailVenteList()) {
			table2.addCell(new Phrase(item.getProduit().getNomComplet(), littleFont));
			cell = new PdfPCell(new Phrase(String.valueOf(item.getPrixUnitaire()), font));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantiteVendue()), font));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(new Phrase(String.valueOf(item.getPrixUnitaire() * item.getQuantiteVendue()), font));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table2.addCell(cell);
		}

		cell = new PdfPCell(new Phrase("Montant Total", font));
		cell.setColspan(3);
		cell.setPadding(8f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase(String.valueOf(vente.getTotal()), priceFont));
		cell.setNoWrap(true);
		cell.setPadding(8f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table2.addCell(cell);

		document.add(table);
		document.add(table2);
		document.close();
	}
}
