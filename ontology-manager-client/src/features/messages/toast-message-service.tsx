import { MessageService } from "./message-service";
import { RefObject } from "react";
import { Toast } from "primereact/toast";

export class ToastMessageService implements MessageService {
    toast : RefObject<Toast>;

    constructor(toast : RefObject<Toast>) {
        this.toast = toast;
    }

    show(severity: "success" | "info" | "warn" | "error" | undefined, summary: string, detail: string): void {
        this.toast.current?.show({severity: severity, summary: summary, detail: detail});
    }
}