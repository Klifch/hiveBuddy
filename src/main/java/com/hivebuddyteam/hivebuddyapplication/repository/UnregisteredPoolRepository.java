package com.hivebuddyteam.hivebuddyapplication.repository;

import com.hivebuddyteam.hivebuddyapplication.domain.UnregisterdPoolUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnregisteredPoolRepository extends JpaRepository<UnregisterdPoolUnit, String> {
//
    Boolean existsBySerialNumber(String serial);

    UnregisterdPoolUnit findBySerialNumber(String serial);
}
