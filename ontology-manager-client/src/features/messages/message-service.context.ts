import React from "react";
import { MessageService } from "./message-service";

interface MessageServiceContextType {
    messageService?: MessageService
}

const MessageServiceContext = React.createContext<MessageServiceContextType>({});

export default MessageServiceContext;