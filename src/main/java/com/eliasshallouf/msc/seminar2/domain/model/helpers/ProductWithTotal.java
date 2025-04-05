package com.eliasshallouf.msc.seminar2.domain.model.helpers;

import jakarta.persistence.Column;

public class ProductWithTotal {
    @Column String name;
    @Column Double total;
}
