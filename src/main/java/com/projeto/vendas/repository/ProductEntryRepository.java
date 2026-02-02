package com.projeto.vendas.repository;

import com.projeto.vendas.model.ProductEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEntryRepository extends JpaRepository<ProductEntry, Long> {

}
