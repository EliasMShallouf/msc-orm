package com.eliasshallouf.msc.seminar2.domain.model.helpers;

import jakarta.persistence.Column;

public class CustomerWithTotal {
    @Column String name;
    @Column Double total;

    public String getName() {
        return name;
    }

    public Double getTotal() {
        return total;
    }
}
