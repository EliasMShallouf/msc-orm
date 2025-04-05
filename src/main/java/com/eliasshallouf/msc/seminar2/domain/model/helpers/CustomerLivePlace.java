package com.eliasshallouf.msc.seminar2.domain.model.helpers;

import jakarta.persistence.Column;

public class CustomerLivePlace {
    @Column String country;
    @Column String city;
    @Column Long count;
}
