package com.demoV1Project.dto;


import com.demoV1Project.model.Business;
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
public class UserDto {

        private Long id;
        private String name;
        private String phoneNumber;
        private String password;
        private String email;
        private String role;
        private List<Business> businessList = new ArrayList<>();


}
