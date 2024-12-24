package ru.vladuss.mainservice.services.Impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vladuss.contracts.dtos.ProductDto;
import ru.vladuss.mainservice.entity.Product;
import ru.vladuss.mainservice.repositories.IProductRepository;
import ru.vladuss.mainservice.services.IProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService<ProductDto, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final IProductRepository productRepository;
    private final SenderService senderService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductService(IProductRepository productRepository, SenderService senderService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.senderService = senderService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        if (product.getUuid() != null && productRepository.existsById(product.getUuid())) {
            Optional<Product> existingProduct = productRepository.findById(product.getUuid());

            if (existingProduct.isPresent()) {
                Product existing = existingProduct.get();
                if (existing.getStockQuantity() == 0) {
                    existing.setInStock(true);
                }
                existing.setStockQuantity(existing.getStockQuantity() + product.getStockQuantity());
                productRepository.saveAndFlush(existing);
                logger.info("Товар {} обновлён, добавлено количество на складе.", existing.getUuid());
            }
        } else {
            productRepository.saveAndFlush(product);
            logger.info("Новый товар {} добавлен.", product.getUuid());
        }
    }

    @Override
    public void addProduct(UUID productUuid, int quantity) {
        Optional<Product> existingProduct = productRepository.findById(productUuid);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setStockQuantity(product.getStockQuantity() + quantity);

            if (!product.getInStock() && product.getStockQuantity() > 0) {
                product.setInStock(true);
            }

            productRepository.saveAndFlush(product);
            logger.info("Количество товара {}, {} увеличено на {}. Текущее количество: {}",
                    product.getUuid(), product.getName(), quantity, product.getStockQuantity());
        } else {
            logger.warn("Товар с UUID {} не найден, добавление невозможно.", productUuid);
        }
    }

    @Override
    public void deleteByUUID(UUID uuid) {
        Optional<Product> existingProduct = productRepository.findById(uuid);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            if (product.getStockQuantity() > 1) {
                product.setStockQuantity(product.getStockQuantity() - 1);
            } else if (product.getStockQuantity() == 1) {
                product.setStockQuantity(0);
                product.setInStock(false);
            }
            productRepository.saveAndFlush(product);
            logger.info("Количество товара {}, {} уменьшено. Текущее количество: {}", product.getUuid(), product.getName(), product.getStockQuantity());
        } else {
            logger.warn("Товар с UUID {} не найден, удаление невозможно.", uuid);
        }
    }

    @Override
    public ProductDto findByUUID(UUID uuid) {
        Optional<Product> existingProduct = productRepository.findById(uuid);
        if (existingProduct.isPresent()) {
            logger.info("Товар {} найден.", uuid);
            return convertToDto(existingProduct.get());
        } else {
            logger.warn("Товар с UUID {} не найден.", uuid);
            return null;
        }
    }

    @Override
    public List<ProductDto> findAll() {
        logger.info("Получение всех товаров.");
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public void editProduct(ProductDto productDto) {
        if (productDto.getUuid() == null) {
            logger.warn("Не передан UUID для обновления товара.");
            return;
        }

        Optional<Product> existingProduct = productRepository.findById(productDto.getUuid());
        if (!existingProduct.isPresent()) {
            logger.warn("Товар с UUID {} не найден, невозможно обновить.", productDto.getUuid());
            return;
        }

        Product existing = existingProduct.get();
        Product updatedProduct = convertToEntity(productDto);
        updateProductDetails(existing, updatedProduct);
        updateProductStock(existing, updatedProduct);

        productRepository.saveAndFlush(existing);
        logger.info("Товар {} обновлён.", existing.getUuid());
    }

    private void updateProductDetails(Product existing, Product updatingProduct) {
        existing.setName(updatingProduct.getName());
        existing.setDescription(updatingProduct.getDescription());
        existing.setPrice(updatingProduct.getPrice());
    }

    private void updateProductStock(Product existing, Product updatingProduct) {
        if (!existing.getStockQuantity().equals(updatingProduct.getStockQuantity())) {
            if (existing.getStockQuantity() == 0 && updatingProduct.getStockQuantity() > 0) {
                existing.setInStock(true);
            } else if (updatingProduct.getStockQuantity() == 0) {
                existing.setInStock(false);
            }
            existing.setStockQuantity(updatingProduct.getStockQuantity());
        }
    }

    private ProductDto convertToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    private Product convertToEntity(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }
}
