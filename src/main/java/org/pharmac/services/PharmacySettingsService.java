package org.pharmac.services;

import org.pharmac.models.PharmacySettings;
import org.pharmac.repository.PharmacySettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PharmacySettingsService {

	@Autowired
	private PharmacySettingsRepository pharmacySettingsRepository;

	public PharmacySettings saveOrUpdatePharmacy(PharmacySettings settings) {
		Optional<PharmacySettings> existingSettings = pharmacySettingsRepository.findById(settings.getId());
		if (existingSettings.isPresent()) {
			PharmacySettings updatedSettings = existingSettings.get();
			updatedSettings.setNomPharma(settings.getNomPharma());
			updatedSettings.setAdressePharma(settings.getAdressePharma());
			updatedSettings.setTelPharma(settings.getTelPharma());
			updatedSettings.setEmailPharma(settings.getEmailPharma());
			updatedSettings.setTheme(settings.getTheme());
			return pharmacySettingsRepository.save(updatedSettings);
		} else {
			return pharmacySettingsRepository.save(settings);
		}
	}

	public Optional<PharmacySettings> getPharmacySettings() {
		return pharmacySettingsRepository.findAll().stream().findFirst();
	}
}
