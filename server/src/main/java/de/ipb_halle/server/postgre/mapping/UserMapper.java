package de.ipb_halle.server.postgre.mapping;

import de.ipb_halle.model.User;
import de.ipb_halle.model.UserRole;
import de.ipb_halle.server.postgre.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    default User toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
 
        User userDto = new User();

        userDto.setId(userEntity.getId());
        userDto.setEnabled(userEntity.getEnabled());
        userDto.setDisplayName(userEntity.getDisplayName());

        if(userEntity.getRole() != null) {
            userDto.setRole(UserRole.valueOf(userEntity.getRole().name()));
        }

        return userDto;
    }

    default User map(UserEntity userEntity) {
        return toDto(userEntity);
    }
}