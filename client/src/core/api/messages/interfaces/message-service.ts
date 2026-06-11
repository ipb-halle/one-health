import { Message } from '../models/message';

export interface MessageService {
    show(message: Message): void;
}
