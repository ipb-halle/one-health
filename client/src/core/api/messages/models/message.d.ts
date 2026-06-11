export interface Message {
    severity: 'success' | 'info' | 'warn' | 'error' | undefined;
    summary: string;
    detail: string;
}
