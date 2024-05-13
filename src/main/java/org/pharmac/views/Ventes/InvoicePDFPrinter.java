package org.pharmac.views.Ventes;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.DetailVente;
import org.pharmac.models.Vente;
import org.pharmac.services.DetailVenteService;

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

	public InvoicePDFPrinter(Vente vente) {
		this.vente = vente;
//		this.vente.setDetailVenteList(detailVenteService.getDetailsVenteByVente(vente));
	}

	public void export(OutputStream output) throws DocumentException, IOException {
//		Document document = new Document(PageSize.A4);
		Rectangle pageSize = new Rectangle(226, 366);
		Document document = new Document(pageSize, 10, 10, 20, 20);

		PdfWriter.getInstance(document, output);
		document.open();

//		document.add(new Paragraph("Reçu pharmacie"));

		// Ajouter le logo
		Image logo = Image.getInstance("C:/Users/ASUS TUF/Downloads/_696d8832-6904-48e2-82bf-fb1b86ae7af6.jpeg");
		logo.scaleToFit(50, 50);  // Redimensionner l'image si nécessaire
		document.add(logo);



		PdfPTable table = new PdfPTable(4);
		PdfPCell cell;

		FontFactory.register("C:/WINDOWS/FONTS/TREBUC.TTF", "Trebuchet MS");
		Font headFont = FontFactory.getFont("Trebuchet MS", 8, Font.BOLD);
		Font littleFont = FontFactory.getFont("Trebuchet MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 5, Font.NORMAL);
		cell = new PdfPCell(new Phrase("Reçu", headFont));
		cell.setColspan(4);
		table.setSpacingAfter(20);
		cell.setBackgroundColor(new Color(175, 175, 175));
		cell.setPadding(8f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);

		Font font = FontFactory.getFont("Trebuchet MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7, Font.NORMAL);
//		Font ff = FontFactory.getFont("Trebuchet MS")
		table.addCell(new PdfPCell(new Phrase("Numéro vente", font)));
		table.addCell(new PdfPCell(new Phrase("Date vente", font)));
		table.addCell(new PdfPCell(new Phrase("Heure vente", font)));
		table.addCell(new PdfPCell(new Phrase("Nom du client", font)));

		table.addCell(new Phrase(vente.getIdV().toString(), font));
		DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
		table.addCell(new Phrase(vente.getDateVente().format(df), littleFont));
		DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm:ss");
		table.addCell(new Phrase(vente.getDateVente().format(hf), font));
		String nameToDisplay = vente.getNomClient() != null ? vente.getNomClient().toString() : "N/A";
		table.addCell(new Phrase(nameToDisplay, font));

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
		table2.addCell(new PdfPCell(new Phrase("Total", font)));

//		List<DetailVente> detailVenteList = detailVenteService.getDetailsVenteByVente(vente.get());
//		vente.get().setDetailVenteList(detailVenteList);
		List<DetailVente> detailVenteList = detailVenteService.getDetailsVenteByVente(vente);
//		vente.setDetailVenteList(detailVenteList);
		for(DetailVente item : detailVenteList) {
			table2.addCell(new Phrase(item.getProduit().getNomCommercial(), littleFont));
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

		cell = new PdfPCell(new Phrase("Total", font));
		cell.setColspan(3);
		cell.setPadding(8f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase(String.valueOf(vente.getTotal()), font));
		cell.setPadding(8f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table2.addCell(cell);

		document.add(table);
		document.add(table2);
		document.close();
	}
}
