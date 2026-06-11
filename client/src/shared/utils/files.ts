export const processChunkAsync = (chunk: any) => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();

        reader.onload = () => {
            const chunkText = reader.result?.toString();
            if (!chunkText) return;

            resolve(chunkText);
        };

        reader.readAsText(chunk);
    });
};

export const readNextChunk = (file: any, offset: number, chunkSize: number) => {
    const chunk = file.slice(offset, offset + chunkSize);
    return chunk;
};
