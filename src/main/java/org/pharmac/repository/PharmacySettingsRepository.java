package org.pharmac.repository;

import org.pharmac.models.PharmacySettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacySettingsRepository extends JpaRepository<PharmacySettings, Long> {
}