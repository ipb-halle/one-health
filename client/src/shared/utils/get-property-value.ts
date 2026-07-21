import { Entity } from "@/store/Entity";
import { Instance } from "mobx-state-tree";


interface PropertyList {
    properties: {
        name: string,
        value: string
    }[]
}

export const getPropertyValue = (element: PropertyList | Instance<typeof Entity>, key: string):string | undefined => {
    return element?.properties?.find((e: { name: string }) => e.name === key)?.value || undefined;
};
