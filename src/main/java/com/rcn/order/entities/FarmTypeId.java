package com.rcn.order.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class FarmTypeId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String farmId;
    private Date startOrderDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FarmTypeId farmTypeId = (FarmTypeId) o;
        return farmId.equals(farmTypeId.farmId) &&
        		startOrderDateTime.equals(farmTypeId.startOrderDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(farmId, startOrderDateTime);
    }
}
