package org.pharmac.views.Dashboard;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.services.ProduitService;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("gestionnaire/dashboard")
public class DashboardPage extends BasePage {

	@SpringBean
	private ProduitService produitService;

//	int stockTotal = produitService.getProduitStockTotal(produitRow.getCodeP());
////				item.add(new Label("stockTotal", stockTotal));
//	String stockStatus = "";
//				if (stockTotal <= 25) {
//		stockStatus = "Bas";
//	} else if (stockTotal < 65) {
//		stockStatus = "Moyen";
//	} else {
//		stockStatus = "Bon";
//	}
//	add(new Label("stockStatus", stockStatus));
}
