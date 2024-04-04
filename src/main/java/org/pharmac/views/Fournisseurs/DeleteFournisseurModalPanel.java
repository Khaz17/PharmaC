package org.pharmac.views.Fournisseurs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Fournisseur;
import org.pharmac.services.FournisseurService;

import java.util.Optional;

public class DeleteFournisseurModalPanel extends Panel {
	@SpringBean
	private FournisseurService fournisseurService;

	private long fournisseurIdToDelete; // Stocke l'ID de l'élément à supprimer

	public DeleteFournisseurModalPanel(String id) {
		super(id);
		setOutputMarkupId(true); // Assurez-vous que l'ID du panel est rendu dans le HTML
	}

	public void show(AjaxRequestTarget target, long fournisseurId) {
		fournisseurIdToDelete = fournisseurId; // Stocke l'ID de l'élément à supprimer
		target.add(this); // Rafraîchit le panel pour afficher le modal
	}

	protected void onInitialize() {
		super.onInitialize();

//		add(new Label("question", "Vous êtes sur le point de supprimer le fournisseur #"+ fournisseurToDelete.getIdF()+"-"+fournisseurToDelete.getNomF()+" ?" ));
		add(new AjaxButton("confirm") {
			@Override
			public void onSubmit(AjaxRequestTarget target) {
				boolean deleted = deleteFournisseur(fournisseurIdToDelete);
				if (deleted) {
					info("Élément supprimé avec succès !");
				} else {
					error("La suppression a échoué.");
				}
			}
		});
	}

	private boolean deleteFournisseur(Long fournisseurToDeleteId) {
		Optional<Fournisseur> fournisseurToDelete = fournisseurService.getFournisseur(fournisseurToDeleteId);
		fournisseurToDelete.ifPresent(fournisseur -> fournisseurService.removeFournisseur(fournisseur.getIdF()));
		return true;
	}

//	public DeleteFournisseurModalPanel(String id, Fournisseur fournisseurToDelete) {
//		super(id);
////		Optional<Fournisseur> fournisseur = fournisseurService.getFournisseur(fournisseurToDeleteId);
//		if(fournisseurToDelete != null) {
//			add(new Label("question","Vous êtes sur le point de supprimer le fournisseur #"+ fournisseurToDelete.getIdF()+"-"+fournisseurToDelete.getNomF()+" ?" ));
//			add(new Link<Void>("confirm") {
//				@Override
//				public void onClick() {
//					fournisseurService.removeFournisseur(fournisseurToDelete.getIdF());
//					setResponsePage(FournisseursPage.class);
//				}
//			});
//		}
//	}


//	public abstract void onConfirm();
}
