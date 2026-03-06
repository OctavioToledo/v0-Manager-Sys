package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionDto;
import com.demoV1Project.domain.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    public SubscriptionDto toDto(Subscription entity) {
        if (entity == null) {
            return null;
        }

        return SubscriptionDto.builder()
                .id(entity.getId())
                .tier(entity.getTier())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .build();
    }
}
