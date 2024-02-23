package com.adevaldo.dscomerce.services;

import com.adevaldo.dscomerce.dto.ProductDTO;
import com.adevaldo.dscomerce.entities.Product;
import com.adevaldo.dscomerce.repositories.ProductRepository;
import com.adevaldo.dscomerce.services.exceptions.DatabaseException;
import com.adevaldo.dscomerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product result = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found"));
        return new ProductDTO(result);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
       Page<Product> result = repository.findAll(pageable);
       return result.map(ProductDTO::new);
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO){
        var product = new Product();
        copyDtoToEntity(productDTO,product);
        product = repository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO){
        try {
            var product = repository.getReferenceById(id);
            copyDtoToEntity(productDTO, product);
            product = repository.save(product);
            return new ProductDTO(product);
        }catch (EntityNotFoundException ex){
            throw new ResourceNotFoundException("Resource Not Found");
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException(("Resource Not Found"));
        }
        try {
            repository.deleteById(id);
        }catch (DataIntegrityViolationException ex){
            throw new DatabaseException("Fail Integrity Referencial");
        }

    }

    public void copyDtoToEntity(ProductDTO productDTO, Product product){
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImgUrl(productDTO.getImgUrl());
    }



}
