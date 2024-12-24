package ru.vladuss.mainservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vladuss.contracts.controllers.ProductApi;
import ru.vladuss.contracts.dtos.ProductDto;
import ru.vladuss.contracts.dtos.ProductRequest;
import ru.vladuss.contracts.dtos.ProductResponse;
import ru.vladuss.mainservice.services.IProductService;
import ru.vladuss.mainservice.utils.NotFoundUuidException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController implements ProductApi {
    private final IProductService<ProductDto, UUID> productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(IProductService<ProductDto, UUID> productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);

        EntityModel<ProductResponse> productEntityModel = EntityModel.of(modelMapper.map(productDto, ProductResponse.class));
        productEntityModel.add(linkTo(methodOn(ProductController.class).getProduct(productDto.getUuid())).withSelfRel());

        return ResponseEntity.ok().body(productEntityModel.getContent());
    }

    @Override
    public ResponseEntity<String> addProduct(@PathVariable UUID productUuid, @RequestParam int quantity) {
        productService.addProduct(productUuid, quantity);
        return ResponseEntity.ok("Количество товара увеличено на " + quantity);
    }

    @Override
    public ResponseEntity<ProductRequest> getProduct(@PathVariable UUID productUUID) {
        ProductDto productDto = productService.findByUUID(productUUID);

//        if (productDto == null) {
//            throw new NotFoundUuidException("Product not found for UUID: " + productUUID);
//        }

        ProductRequest productRequest = new ProductRequest(productDto);
        productRequest.add(linkTo(methodOn(ProductController.class).getProduct(productUUID)).withSelfRel());
        productRequest.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("get-all"));

        return ResponseEntity.ok(productRequest);
    }

    @Override
    public ResponseEntity<CollectionModel<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.findAll().stream()
                .map(productDto -> modelMapper.map(productDto, ProductResponse.class))
                .collect(Collectors.toList());

        CollectionModel<ProductResponse> productModels = CollectionModel.of(products);
        productModels.add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

        return ResponseEntity.ok(productModels);
    }

    @Override
    public ResponseEntity<EntityModel<ProductResponse>> editProduct(@RequestBody ProductDto productDto) {
        productService.editProduct(productDto);

        ProductResponse productResponse = modelMapper.map(productDto, ProductResponse.class);
        EntityModel<ProductResponse> productResource = EntityModel.of(productResponse);
        productResource.add(linkTo(methodOn(ProductController.class).getProduct(productDto.getUuid())).withSelfRel());
        productResource.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all-products"));

        return ResponseEntity.ok(productResource);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productUUID) {
        productService.deleteByUUID(productUUID);
        return ResponseEntity.ok().build();
    }
}
