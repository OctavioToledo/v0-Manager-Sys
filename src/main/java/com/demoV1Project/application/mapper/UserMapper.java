package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.AppointmentDto.AppointmentUpdateDto;
import com.demoV1Project.domain.dto.UserDto.UserCreateDto;
import com.demoV1Project.domain.dto.UserDto.UserDetailDto;
import com.demoV1Project.domain.dto.UserDto.UserDto;
import com.demoV1Project.domain.dto.UserDto.UserUpdateDto;
import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
    public UserDetailDto toDetailDto(User user) {
        return modelMapper.map(user, UserDetailDto.class);
    }

    public User toEntity(UserCreateDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public void updateEntity(UserUpdateDto userUpdateDto, User user) {
        modelMapper.map(userUpdateDto, user);
    }


    public List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
