import { Message } from "./message";

export interface MessageService {
    show( message: Message ) : void;
}

