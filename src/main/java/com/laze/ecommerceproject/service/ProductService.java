package com.laze.ecommerceproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laze.ecommerceproject.controller.dto.ProductCreateRequest;
import com.laze.ecommerceproject.controller.dto.ProductResponse;
import com.laze.ecommerceproject.document.ProductDocument;
import com.laze.ecommerceproject.domain.Product;
import com.laze.ecommerceproject.domain.ProductViewLog;
import com.laze.ecommerceproject.domain.User;
import com.laze.ecommerceproject.esrepository.ProductSearchRepository;
import com.laze.ecommerceproject.repository.ProductRepository;
import com.laze.ecommerceproject.repository.ProductViewLogRepository;
import com.laze.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final UserRepository userRepository;
    private  final ProductViewLogRepository productViewLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void createProduct(ProductCreateRequest request) throws Exception {
        String attributesJson = convertMapToJson(request.getAttributes());

        Product product = new Product(
                request.getProductName(),
                request.getPrice(),
                request.getStock(),
                attributesJson
        );
        productRepository.save(product);

        ProductDocument productDocument = ProductDocument.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .brand(getBrandFromAttributes(attributesJson))
                .build();

        productSearchRepository.save(productDocument);
    }

    private String getBrandFromAttributes(String attributesJson) throws Exception {
        try {
            JsonNode rootNode = objectMapper.readTree(attributesJson);
            if (rootNode.has("brand")) {
                return rootNode.get("brand").asText();
            }
        } catch (Exception e) {
            // TODO: Logging 처리
            return null;
        }
        return null;
    }


    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found id: " + id));

        // Log 기록
        logProductView(product);
        return new ProductResponse(product);
    }

    private void logProductView(Product product) {
        // 현재 SecurityContext에서 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        // 인증 정보가 있고, 익명 사용자가 아닌 경우에만 사용자 정보를 조회
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String email = (String) authentication.getPrincipal();
            user = userRepository.findByEmail(email).orElse(null);
        }

        // ProductViewLog 엔티티를 생성하고 저장
        ProductViewLog log = new ProductViewLog(user, product);
        productViewLogRepository.save(log);
    }

    private String convertMapToJson(Map<String, Object> attributes) {
        try {
            return objectMapper.writeValueAsString(attributes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting attributes to JSON", e);
        }
    }

    public List<ProductResponse> getProductsWithFilters(String brand, Long minPrice, Long maxPrice) {
        return productRepository.findWithFilters(brand, minPrice, maxPrice).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String keyword) {
        // 1. Elasticsearch에서 productName으로 검색
        List<ProductDocument> documents = productSearchRepository.findByProductNameContaining(keyword);

        // 2. 검색 결과(ProductDocument)를 ProductResponse 리스트로 변환
        return documents.stream()
                .map(this::convertDocumentToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse convertDocumentToResponse(ProductDocument document) {
        return new ProductResponse(document.getId(), document.getProductName(), document.getPrice());
    }

    public List<ProductResponse> getRecommendProducts(Long productId) {
        int limit = 5;
        List<Product> recommendedProducts = productRepository.findRecommendations(productId, limit);

        return recommendedProducts.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
