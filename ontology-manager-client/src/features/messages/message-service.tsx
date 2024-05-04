export interface MessageService {
    show(severity: "success" | "info" | "warn" | "error" | undefined, summary: string, detail: string) : void;
}

