package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.domain.UnregisterdPoolUnit;
import com.hivebuddyteam.hivebuddyapplication.repository.UnregisteredPoolRepository;
import org.springframework.stereotype.Service;

@Service
public class UnregisteredPoolServiceImpl implements UnregisteredPoolService{

    private final UnregisteredPoolRepository unregisteredPoolRepository;

    public UnregisteredPoolServiceImpl(UnregisteredPoolRepository unregisteredPoolRepository) {
        this.unregisteredPoolRepository = unregisteredPoolRepository;
    }

    @Override
    public Boolean checkIfExists(String serial) {
        return unregisteredPoolRepository.existsBySerialNumber(serial);
    }

    @Override
    public UnregisterdPoolUnit getBySerial(String serial) {
        return unregisteredPoolRepository.findBySerialNumber(serial);
    }

    @Override
    public Boolean checkIfSecurityCodeValid(String securityCode, String serial) {
        UnregisterdPoolUnit unregisterdPoolUnit = getBySerial(serial);

        return unregisterdPoolUnit != null && unregisterdPoolUnit.getSecurityCode().equals(securityCode);
    }

    @Override
    public void removeUnit(UnregisterdPoolUnit unit) {
        unregisteredPoolRepository.delete(unit);
    }
}
