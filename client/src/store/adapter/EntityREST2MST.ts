import { Instance } from 'mobx-state-tree';
import { IEntityDTO } from '@/features/search/general-search/models/entity-dto';
import { Entity } from '../Entity';
import { Property } from '../Property';
import { Reference } from '../Reference';

export function EntityREST2MST(dtos: IEntityDTO[]): Instance<typeof Entity>[];
export function EntityREST2MST(dto: IEntityDTO): Instance<typeof Entity>;
export function EntityREST2MST(
    input: IEntityDTO | IEntityDTO[],
): Instance<typeof Entity> | Instance<typeof Entity>[] {
    if (Array.isArray(input)) {
        return input.map((dto) => EntityREST2MST(dto));
    }

    const dto = input;

    return Entity.create({
        id: dto.id,
        type: dto.type,
        name: dto.name,
        color: dto.color,
        labels: dto.labels,
        properties: dto.properties.map((property) =>
            Property.create({
                name: property.name,
                value: property.value ?? null,
                position: property.position,
                dataType: property.dataType as any,
            }),
        ),
        references: Array.isArray(dto.references)
            ? dto.references.map((reference: any) =>
                  Reference.create({
                      source: reference.source,
                      externalId: reference.externalId,
                      sourceUrl: reference.sourceUrl,
                      entityId: reference.entityId,
                  }),
              )
            : [],
        synonyms: dto.synonyms,
    });
}
