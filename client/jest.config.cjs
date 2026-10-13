
/** @type {import('jest').Config} */

module.exports = {

  testEnvironment: "jsdom",

  roots: ["<rootDir>/test"],

  moduleNameMapper: {
    '^@/core/api/http/api-config$':
      '<rootDir>/test/config/api-config.ts',
    '^@/(.*)$': '<rootDir>/src/$1',
  },

  transform: {
    '^.+\\.(ts|tsx)$': [
      'ts-jest',
      {
        tsconfig: '<rootDir>/tsconfig.test.json',
        useESM: false,
      },
    ],
  },
};
