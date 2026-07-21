import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import eslint from 'vite-plugin-eslint';
import path from 'path'; 

export default defineConfig(() => {
    return {
        server: {
            proxy: {
                '/api': 'http://localhost:8080', // change this setting
            },
        },

        build: {
            outDir: 'build',
        },
        plugins: [
            react({
                babel: {
                    plugins: [
                        ['@babel/plugin-proposal-decorators', { legacy: true }],
                    ],
                },
            }),
            eslint({
                // only in dev mode
                emitWarning: true,
                emitError: false,
                failOnError: false,
            }),
        ],
        resolve: {
            alias: {
                '@': path.resolve(__dirname, './src'),
            },
        },

    };
});
