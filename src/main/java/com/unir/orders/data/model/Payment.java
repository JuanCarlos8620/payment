package com.unir.orders.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "payments")  // Nombre de la tabla en la base de datos
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String status; // Estatus del pago, por ejemplo, "COMPLETED", "PENDING"

    // Lista de IDs de productos asociados al pago
    @ElementCollection
    @Column(name = "products")
    private List<Long> products;  // Almacena solo los IDs de los productos

}