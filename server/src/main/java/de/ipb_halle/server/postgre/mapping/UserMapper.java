package de.ipb_halle.server.postgre.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;

import de.ipb_halle.model.User;
import de.ipb_halle.server.postgre.models.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    default User toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());

        user.setRole(userEntity.getRole() != null
                ? User.RoleEnum.fromValue(userEntity.getRole().name())
                : null);
        user.setEnabled(userEntity.getEnabled());

        if (userEntity.getOrcid() != null) {
            user.setOrcid(JsonNullable.of(userEntity.getOrcid()));
        } else {
            user.setOrcid(JsonNullable.undefined());
        }

        return user;
    }

    default User map(UserEntity userEntity) {
        return toDto(userEntity);
    }
}