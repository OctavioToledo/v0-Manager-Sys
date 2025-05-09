package com.demoV1Project.domain.dto.UserDto;

import com.demoV1Project.domain.dto.BusinessDto.BusinessShortDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.util.enums.Role;
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
    private Role role;
    private List<BusinessShortDto> businessShortList = new ArrayList<>();
}
