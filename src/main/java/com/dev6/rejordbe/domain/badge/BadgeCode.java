package com.dev6.rejordbe.domain.badge;

import lombok.AllArgsConstructor;

/**
 * BadgeCode
 */
@AllArgsConstructor
@lombok.Getter
public enum BadgeCode {
    // TODO 이미지 URL 변경 해줘야됨
    WATER_FAIRY("imageUrl1", "워터 요정"), // 워터 요정
    VEGETARIAN("imageUrl2", "나름 채식러"), // 나름 채식러
    ENERGY_SAVER("imageUrl3", "절전 요정"),   // 절전 요정
    MEAL_PLANNER("imageUrl4", "식사 계획러"),   // 식사 계획러
    DIGITAL_FAIRY("imageUrl5", "디지털 요정"),  // 디지털 요정
    FOREST_LOVER("imageUrl6", "포레스트 러버"),   // 포레스트 러버
    PRO_SHOPPER("imageUrl7", "프로 쇼핑러"),    // 프로 쇼핑러
    ENVIRONMENTAL_EXPERT("imageUrl8", "환경 전문가"),   // 환경 전문가
    PRO_ACTIVATE("imageUrl9", "프로 부지런러"), // 프로 부지런러
    DEFAULT("imageUrl10", "공개 예정");

    private final String imageUrl;
    private final String badgeName;
}