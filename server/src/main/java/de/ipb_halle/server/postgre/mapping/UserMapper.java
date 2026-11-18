package de.ipb_halle.server.postgre.mapping;

import de.ipb_halle.model.User;
import de.ipb_halle.model.UserRole;
import de.ipb_halle.server.postgre.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import java.time.ZoneId;
import java.time.OffsetDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Berlin");

    default User toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        User userDto = new User();

        userDto.setId(userEntity.getId());
        userDto.setEnabled(userEntity.getEnabled());
        userDto.setDisplayName(userEntity.getDisplayName());
        userDto.setLastLogin(userEntity.getLastLogin() == null
                ? null
                : userEntity.getLastLogin()
                        .atZone(ZONE_ID)
                        .toOffsetDateTime());

        if (userEntity.getRole() != null) {
            userDto.setRole(UserRole.valueOf(userEntity.getRole().name()));
        }

        return userDto;
    }

    default User map(UserEntity userEntity) {
        return toDto(userEntity);
    }
}