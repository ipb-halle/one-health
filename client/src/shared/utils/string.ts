export const truncateString = (string: string, length: number) => {
    if (string.length <= length) return string;
    return string.substring(0, length - 1) + '...';
};
