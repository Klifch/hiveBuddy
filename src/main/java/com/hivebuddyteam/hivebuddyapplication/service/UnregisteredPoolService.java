package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.domain.UnregisterdPoolUnit;

public interface UnregisteredPoolService {

    public Boolean checkIfExists(String serial);

    public UnregisterdPoolUnit getBySerial(String serial);

    public Boolean checkIfSecurityCodeValid(String securityCode, String serial);

    public void removeUnit(UnregisterdPoolUnit unit);
}
