export const getPropertyValue = (element: any, key: string) => {
    return element?.properties?.find((e: any) => e.name === key)?.value || '';
};
