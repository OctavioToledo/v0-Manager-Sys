package com.demoV1Project.domain.dto.UserDto;

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
public class UserUpdateDto {
    private String name;
    private String phoneNumber;
    private String password;
    private Role role;
    private List<Business> businessList = new ArrayList<>();
}
