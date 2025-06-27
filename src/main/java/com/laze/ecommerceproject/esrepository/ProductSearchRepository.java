package com.laze.ecommerceproject.esrepository;

import com.laze.ecommerceproject.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    // productName 필드에서 주어진 검색어를 포함하는 문서를 찾는다.
    // Elasticsearch의 match 쿼리로 변환되어, 텍스트 분석을 통한 검색을 수행한다.
    List<ProductDocument> findByProductNameContaining(String productName);
}
