package com.unir.orders.service;

import com.unir.orders.data.PaymentJpaRepository;
import com.unir.orders.data.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.unir.orders.facade.ProductsFacade;
import com.unir.orders.facade.model.Product;
import com.unir.orders.controller.model.PaymentRequest;

@Service
@RequiredArgsConstructor  //mejor inyeccion por constructor mas por temas de testing
public class PaymentsServiceImpl implements PaymentsService {

  private final ProductsFacade productsFacade;  // Facade para obtener productos desde el servicio de productos

  private final PaymentJpaRepository repository;  // Repositorio de pagos

  @Override
  public Payment createPayment(PaymentRequest request) {
    // Obtener los productos de la solicitud de pago
    List<Product> products = request.getProducts().stream()
            .map(productId -> productsFacade.getProduct(productId.getId()))
            .filter(Objects::nonNull)
            .toList();

    // Verificar que los productos son válidos y visibles
    if (products.size() != request.getProducts().size() ||
            products.stream().anyMatch(product -> !product.getVisible())) {
      return null;  // Si algún producto no es válido, no se crea el pago
    } else {
      // Crear el objeto Payment con los productos validados
      Payment payment = Payment.builder()
              .products(products.stream().map(Product::getId).collect(Collectors.toList()))
              .build();
      repository.save(payment);  // Guardar el pago en la base de datos
      return payment;  // Retornar el pago creado
    }
  }

  @Override
  public Payment getPayment(Long id) {
    return repository.findById(id).orElse(null);  // Buscar el pago por ID
  }

  @Override
  public List<Payment> getPayments() {
    List<Payment> payments = repository.findAll();  // Obtener todos los pagos
    return payments.isEmpty() ? null : payments;  // Si no hay pagos, retornar null
  }

  @Override
  public boolean deletePayment(Long id) {
    // Verificar si el pago existe y eliminarlo
    if (repository.existsById(id)) {
      repository.deleteById(id);  // Eliminar el pago por ID
      return true;  // Retornar true si se eliminó correctamente
    }
    return false;  // Retornar false si no se encontró el pago
  }
}
