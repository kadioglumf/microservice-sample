package com.kadioglumf.authservice.mapper;

import com.kadioglumf.authservice.core.dto.UserDto;
import com.kadioglumf.authservice.models.CredentialsModel;
import com.kadioglumf.authservice.models.UserAddressModel;
import com.kadioglumf.authservice.payload.request.AddressRequest;
import com.kadioglumf.authservice.payload.request.UpdateUserDetailsRequest;
import com.kadioglumf.authservice.payload.response.GetUserDetailsResponse;
import com.kadioglumf.authservice.payload.response.UserDetailsResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserServiceMapper {

  List<UserDto> toUserDto(Set<CredentialsModel> users);

  UserDto toUserDto(CredentialsModel user);

  Set<GetUserDetailsResponse> convertToResponse(Set<CredentialsModel> users);

  @Mapping(target = "userId", source = "id")
  GetUserDetailsResponse toGetUserDetailsResponse(CredentialsModel user);

  UserAddressModel toUserAddressModel(AddressRequest request);

  @Mapping(source = "model.user.address", target = "address")
  @Mapping(source = "model.user.birthDay", target = "birthDay")
  UserDetailsResponse toUserDetailsResponse(CredentialsModel model);

  @Mapping(target = "model.user.address", source = "request.address")
  @Mapping(target = "model.user.birthDay", source = "request.birthDay")
  void updateUserDetails(@MappingTarget CredentialsModel model, UpdateUserDetailsRequest request);
}
