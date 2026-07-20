

interface PropertyList {
    properties: {
        name: string,
        value: string
    }[]
}

export const getPropertyValue = (element: PropertyList, key: string) => {
    return element?.properties?.find((e: { name: string }) => e.name === key)?.value || '';
};
