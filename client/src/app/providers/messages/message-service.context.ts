import React from 'react';
import { MessageService } from '@/core/api/messages/interfaces/message-service';


interface MessageServiceContextType {
    messageService?: MessageService;
}

const MessageServiceContext = React.createContext<MessageServiceContextType>(
    {},
);

export default MessageServiceContext;
