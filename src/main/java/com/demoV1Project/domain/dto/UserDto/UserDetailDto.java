package com.demoV1Project.domain.dto.UserDto;

import com.demoV1Project.domain.dto.BusinessDto.BusinessShortDto;
import com.demoV1Project.domain.model.Business;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String password;
    private String email;
    private String role;
    private List<BusinessShortDto> businessShortList = new ArrayList<>();
}
