import js from '@eslint/js';
import tseslint from '@typescript-eslint/eslint-plugin';
import tsParser from '@typescript-eslint/parser';
import react from 'eslint-plugin-react';
import reactHooks from 'eslint-plugin-react-hooks';
import eslintConfigPrettier from 'eslint-config-prettier';

export default [
    // Config for eslint.config.js and vite.config.js (no 'project' option)
    {
        files: ['eslint.config.js', 'vite.config.js'],

        languageOptions: {
            ecmaVersion: 2021,
            sourceType: 'module',
            parser: tsParser,
            parserOptions: {
                project: undefined, // Loads no TypeScript project
            },
        },
	        plugins: {
	            '@typescript-eslint': tseslint,
	            react,
	            'react-hooks': reactHooks,
	        },
        settings: {
            react: {
                version: 'detect',
            },
        },
        //EsLint-Rules are added here
	        rules: {
	            ...js.configs.recommended.rules, //recommended ESLint-Rules (Standard JS)
	            ...tseslint.configs.recommended.rules, //recommended Rules from TsESLint
	            ...react.configs.recommended.rules, // recommended Rules from ReactESLint
	            ...reactHooks.configs.recommended.rules, // recommended Rules especially for React hooks
	            '@typescript-eslint/no-unused-vars': [
	                // warns if variables are unused
	                'warn',
	                { argsIgnorePattern: '^_' },
	            ],
	            ...eslintConfigPrettier.rules,
	        },
	    },

    // General config for all other files
    {
        files: ['**/*.{js,ts,jsx,tsx}'],
        languageOptions: {
            ecmaVersion: 2021,
            sourceType: 'module',
            parser: tsParser,
            parserOptions: {
                project: './tsconfig.json',
                ecmaFeatures: {
                    jsx: true,
                },
            },
        },
	        plugins: {
	            '@typescript-eslint': tseslint,
	            react,
	            'react-hooks': reactHooks,
	        },
        settings: {
            react: {
                version: 'detect',
            },
        },
	        rules: {
	            ...js.configs.recommended.rules,
	            ...tseslint.configs.recommended.rules,
	            ...react.configs.recommended.rules,
	            ...reactHooks.configs.recommended.rules,
	            '@typescript-eslint/no-unused-vars': [
	                'warn',
	                { argsIgnorePattern: '^_' },
	            ],
	            ...eslintConfigPrettier.rules,
	        },
	    },
];
