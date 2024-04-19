import { injectable } from "inversify";

export interface MyInterface<T> {
    hello() : string;
}

export interface MyStringInterface extends MyInterface<string> {}

export class StringClass implements MyStringInterface {
    hello(): string {
        return "string";
    }
}

export class IntegerClass implements MyInterface<number> {
    hello(): string {
        return "integer";
    }
}

@injectable()
export class Person {
    hello() : string { return "I'am a person"; };
}

@injectable()
export class Doctor extends Person {
    hello(): string { return "I'm a doctor"
    }
}