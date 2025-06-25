package com.laze.ecommerceproject.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products") // 이 클래스는 "products"라는 이름의 인덱스와 매핑됨
public class ProductDocument {

    @Id
    private Long id;

    // text 타입: 전문(Full-text) 검색에 적합하도록 '분석(Analysis)' 과정을 거침
    // ex: "나이키 에어포스" -> "나이키", "에어", "포스" 로 쪼개서 저장
    @Field(type = FieldType.Text)
    private String productName;

    // keyword 타입: 정확히 일치하는 값을 필터링하거나 집계할 때 사용됨. 분석되지 않음
    // 예: "Nike"는 그대로 "Nike"로 저장됨
    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Long)
    private Long price;
}
