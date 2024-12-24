package ru.vladuss.mainservice.controllers.dataFetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vladuss.contracts.dtos.ProductDto;
import ru.vladuss.mainservice.entity.Product;
import ru.vladuss.mainservice.services.IProductService;

import java.util.List;
import java.util.UUID;

@DgsComponent
public class ProductDataFetcher {

    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductDataFetcher(IProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @DgsQuery
    public List<ProductDto> getAllProducts() {
        return productService.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    @DgsQuery
    public ProductDto getProduct(@InputArgument(name = "productUUID") String productUUID) {
        UUID uuid = UUID.fromString(productUUID);
        ProductDto product = productService.findByUUID(uuid);

        return modelMapper.map(product, ProductDto.class);
    }

    @DgsMutation
    public ProductDto createProduct(@InputArgument(name = "product") ProductDto productDto) {
        productService.createProduct(productDto);

        return productDto;
    }

    @DgsMutation
    public void deleteProduct(@InputArgument(name = "productUUID") String productUUID) {
        UUID uuid = UUID.fromString(productUUID);
        productService.deleteByUUID(uuid);
    }

    @DgsMutation
    public ProductDto editProduct(@InputArgument(name = "product") ProductDto productDto) {
        ProductDto product = modelMapper.map(productDto, ProductDto.class);
        productService.editProduct(product);
        return modelMapper.map(product, ProductDto.class);
    }
}
