package com.servicebuddy.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ad")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Lob
    @Column(name = "img", columnDefinition="LONGBLOB")
    private byte[] img;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

}