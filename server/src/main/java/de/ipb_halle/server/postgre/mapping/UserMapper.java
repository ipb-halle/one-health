package de.ipb_halle.server.postgre.mapping;

import de.ipb_halle.model.User;
import de.ipb_halle.model.UserRole;
import de.ipb_halle.server.postgre.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    default User toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        User userDto = new User();
       
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setEmail(userEntity.getEmail());
        userDto.setEnabled(userEntity.getEnabled());

        if (userEntity.getOrcid() != null) {
            userDto.setOrcid(JsonNullable.of(userEntity.getOrcid()));
        } else {
            userDto.setOrcid(JsonNullable.undefined());
        }

        if(userEntity.getRole() != null) {
            userDto.setRole(UserRole.valueOf(userEntity.getRole().name()));
        }

        return userDto;
    }

    default User map(UserEntity userEntity) {
        return toDto(userEntity);
    }
}