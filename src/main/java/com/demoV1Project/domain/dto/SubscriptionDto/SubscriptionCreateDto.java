package com.demoV1Project.domain.dto.SubscriptionDto;

import com.demoV1Project.util.enums.SubscriptionTier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreateDto {
    private SubscriptionTier tier;
}
