package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import java.io.Serializable;
import java.util.Objects;

public class FishID implements Serializable {
    private String fishID;
    private String user;

    // Equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FishID that = (FishID) o;
        return Objects.equals(fishID, that.fishID) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fishID, user);
    }
}
