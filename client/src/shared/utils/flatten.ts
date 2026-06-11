function getPathKeyFromObject(obj: object, path: string, key: string): string {
    return Array.isArray(obj) ? `${path}[${key}]` : `${path}.${key}`;
}

const _flatten = (objectBit: any, path = ''): any => {
    return [].concat(
        ...Object.keys(objectBit).map((key) =>
            objectBit[key] &&
            typeof objectBit[key] === 'object' &&
            !(objectBit[key] instanceof Date)
                ? _flatten(
                      objectBit[key],
                      getPathKeyFromObject(objectBit, path, key),
                  )
                : {
                      [getPathKeyFromObject(objectBit, path, key)]:
                          objectBit[key] == null
                              ? ''
                              : objectBit[key] instanceof Date
                                ? (objectBit[key] as Date).toUTCString()
                                : encodeURIComponent(objectBit[key]),
                  },
        ),
    );
};

export const flatten = (object: any) => {
    const o = Object.assign({}, ..._flatten(object));
    return Object.assign(
        {},
        ...(function _sliceFirst(obj) {
            return [
                ...Object.keys(o).map((key) => ({ [key.slice(1)]: o[key] })),
            ];
        })(o),
    );
};

export const constructHttpParams = (obj: any): any => {
    if (!obj) return null;
    const flattenBody = flatten(obj);
    return flattenBody;
};
