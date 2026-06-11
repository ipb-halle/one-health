package de.ipb_halle.server.postgre.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import de.ipb_halle.server.data.dtos.NaturalProductDTO;
import de.ipb_halle.server.postgre.models.PSQLNaturalProduct;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PSQLMapper {
    PSQLMapper MAPPER = Mappers.getMapper(PSQLMapper.class);

    default NaturalProductDTO map(PSQLNaturalProduct entity) {
        if (entity == null)
            return null;

        var dto = new NaturalProductDTO();

        dto.setId(entity.get__id());
        dto.setInChI(entity.getInchi());
        dto.setInChIKey(entity.getInchikey());
        dto.setSmiles(entity.getSmiles());
        dto.setMolecularFormula(entity.getMolformula());
        dto.setMolecularWeight(entity.getMolweight());
        dto.setCasRegistryNumber(entity.getCas());
        dto.setIupacName(entity.getIupac());
        dto.setName(entity.getName());

        return dto;
    }

}
