import { defineConfig } from 'orval';

export default defineConfig({
    authApi: {
        input: {
            target: '../server/src/main/resources/openapi/auth-api.yaml',
        },
        output: {
            mode: 'tags-split',
            target: 'src/generated/auth',
            schemas: 'src/generated/auth/model',

            client: 'react-query',
            httpClient: 'axios',

            clean: true,

            override: {
                mutator: {
                    path: './src/app/services/orval-axios.ts',
                    name: 'customAxiosInstance',
                },
            },
        },
    },
});