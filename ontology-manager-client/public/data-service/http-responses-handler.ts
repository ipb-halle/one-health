import { HttpErrorResponse } from "@angular/common/http";
import { Injectable, Injector } from "@angular/core";

import { Message as PrimeNgMessage, ConfirmationService, MessageService } from "primeng/api";

import { Message } from "../../classes";
import { HttpStatusCodes } from "./http-status-codes";
import { AppOnlineStateService } from "@main/pwa/app-online-state.service";
import { AxiosResponse } from "axios";


export interface IHttpResponsesHandler {
    handleSuccess?(): void;
    handleError?(errorResponse: HttpErrorResponse): void;
}

export interface IHttpResponseHandlerSettings {
    showSuccessMessage?: boolean; // Default false
    successMessage?: string;
    showErrorMessage?: boolean; // Default true
    errorMessage?: string;
    errorStatusesMessages?: {[key: number]: string};
}


export class BaseHttpResponsesHandler implements IHttpResponsesHandler {
    constructor(protected injector: Injector, protected settings?: IHttpResponseHandlerSettings) { }


    handleSuccess(): void {
        if (this.settings && this.settings.showSuccessMessage) {
            this.showMessage(Message.Success(this.settings.successMessage ? this.settings.successMessage : "Operation succeed."));
        }
    }

    handleError(errorResponse: HttpErrorResponse): void {
        if (this.settings && this.settings.showErrorMessage == false) return;

        if (this.settings && this.settings.errorStatusesMessages && this.settings.errorStatusesMessages[errorResponse.status] != null) {
            this.showMessage(Message.Error(this.settings.errorStatusesMessages[errorResponse.status]));
            return;
        }

        this.handleErrorHttpStatusCode(errorResponse);
    }

    protected handleErrorHttpStatusCode(errorResponse: HttpErrorResponse): void {
        // App is in Offline mode
        const appOnlineService = this.injector.get(AppOnlineStateService, null);
        if (appOnlineService && !appOnlineService.isAppOnline) {
            // In offline mode no response error messages are shown to user, we don't bother him
            return;
        }

        switch (errorResponse.status) {
            case HttpStatusCodes.Status400BadRequest:
                this.handleStatus400BadRequest(errorResponse);
                break;
            case HttpStatusCodes.Status403Forbidden:
                this.handleStatus403Forbidden(errorResponse);
                break;
            case HttpStatusCodes.Status401Unauthorized:
                break;
            default:
                this.handleOtherError(errorResponse);
        }
    }

    protected handleOtherError(errorResponse: HttpErrorResponse): void {
        this.showMessage(Message.Error(errorResponse.error
            ? errorResponse.error :
            "An error has occurred at the website and your support team will need to fix the problem. " +
            "A preliminary report has been sent to the support team.Please do follow - up on the preliminary " +
            "report using the Report a Problem page"));
    }

    protected showMessage(message: PrimeNgMessage) {
        const messageService = this.injector.get(MessageService, null);
        if (messageService) {
            messageService.add(message);
        }
    }

    protected showMessages(messages: PrimeNgMessage[]) {
        const messageService = this.injector.get(MessageService, null);
        if (messageService) {
            messageService.addAll(messages);
        }
    }

    protected showDialog(message: string): Promise<any> {
        return new Promise((resolve, reject) => {
            const confirmationService = this.injector.get(ConfirmationService, null);
            if (confirmationService) {
                confirmationService.confirm({
                    message: message,
                    accept: () => {
                        resolve(true);
                    },
                    reject: () => {
                        reject();
                    }
                });
            }
        });
    }

    private handleStatus400BadRequest(errorResponse: AxiosResponse): boolean {
        let errorMessage = errorResponse.statusText
            ?? "Request not understood. There may be a network issue. Please attempt to submit your request again. If this problem persists, please use the Report a Problem page found on the menu to alert your support team.";

        if (typeof errorResponse.data === "object") {
            // OData errors
            if (errorResponse.error.error && errorResponse.error.error.message) {
                errorMessage = errorResponse.error.error.message;
            }

            // Validation errors
            if (errorResponse.error) {
                errorMessage = Object
                    .keys(errorResponse.error)
                    .map(key =>
                        `${key}: ${Array.isArray(errorResponse.error[key]) ? (<string[]>errorResponse.error[key]).join(", ") : errorResponse.error[key]}`)
                    .join("\n");
            }
        } else {
            errorMessage = errorResponse.error;
        }

        this.showMessage(Message.Error(errorMessage));
        return false;
    }

    private handleStatus403Forbidden(errorResponse: HttpErrorResponse): void {
        this.showMessage(Message.Error(
            "You don’t have authorisation for this feature. Please refresh your page. If this problem persists, " +
            "please use the Report a Problem page found on the menu to alert your support team."
        ));
    }
}

export class OnReadByIdResponsesHandler extends BaseHttpResponsesHandler {
    constructor(private entityTitle: string, injector: Injector, settings?: IHttpResponseHandlerSettings) {
        super(injector, settings);
    }

    protected handleErrorHttpStatusCode(errorResponse: HttpErrorResponse): void {
        if (errorResponse.status == HttpStatusCodes.Status404NotFound) {
            this.showMessage(Message.Error(
                `${this.entityTitle} not found. Perhaps another user has deleted the ${this.entityTitle}. ` +
                "Please consider refreshing the page."
            ));
            return;
        }

        super.handleErrorHttpStatusCode(errorResponse);
    }
}

export class OnCreateResponseHandler extends BaseHttpResponsesHandler {
    constructor(entityTitle: string, injector: Injector, settings?: IHttpResponseHandlerSettings) {
        super(injector, settings);

        if (!this.settings) this.settings = <IHttpResponseHandlerSettings> {};
        if (this.settings.showSuccessMessage == null) this.settings.showSuccessMessage = true;
        if (this.settings.successMessage == null) this.settings.successMessage = `${entityTitle} created.`;
    }
}

export class OnUpdateResponseHandler extends BaseHttpResponsesHandler {
    constructor(private entityTitle: string, injector: Injector, settings?: IHttpResponseHandlerSettings) {
        super(injector, settings);

        if (!this.settings) this.settings = <IHttpResponseHandlerSettings> {};
        if (this.settings.showSuccessMessage == null) this.settings.showSuccessMessage = true;
        if (this.settings.successMessage == null) this.settings.successMessage = `${entityTitle} updated.`;
    }


    protected handleErrorHttpStatusCode(errorResponse: HttpErrorResponse): void {
        if (errorResponse.status == HttpStatusCodes.Status404NotFound) {
            this.showMessage(Message.Error(
                `${this.entityTitle} not found. Perhaps another user has deleted the ${this.entityTitle}. ` +
                "Please consider taking a note of your changes and then refreshing the page."
            ));
            return;
        }

        super.handleErrorHttpStatusCode(errorResponse);
    }
}

export class OnDeleteResponseHandler extends BaseHttpResponsesHandler {
    constructor(private entityTitle: string, injector: Injector, settings?: IHttpResponseHandlerSettings) {
        super(injector, settings);

        if (!this.settings) this.settings = <IHttpResponseHandlerSettings> {};
        if (this.settings.showSuccessMessage == null) this.settings.showSuccessMessage = true;
        if (this.settings.successMessage == null) this.settings.successMessage = `${entityTitle} deleted.`;
    }


    protected handleErrorHttpStatusCode(errorResponse: HttpErrorResponse): void {
        if (errorResponse.status == HttpStatusCodes.Status404NotFound) {
            this.showMessage(Message.Error(
                `${this.entityTitle} not found. Perhaps another user has deleted the ${this.entityTitle}. ` +
                "Please consider taking a note of your changes and then refreshing the page."
            ));
            return;
        }

        super.handleErrorHttpStatusCode(errorResponse);
    }
}

@Injectable()
export class HttpResponsesHandlersFactory {
    constructor(private injector: Injector) {
    }

    getDefault(settings?: IHttpResponseHandlerSettings): IHttpResponsesHandler {
        return new BaseHttpResponsesHandler(this.injector, settings);
    }

    getForReadById(entityTitle: string, settings?: IHttpResponseHandlerSettings): IHttpResponsesHandler {
        return new OnReadByIdResponsesHandler(entityTitle, this.injector, settings);
    }

    getForReadByFilter(settings?: IHttpResponseHandlerSettings): IHttpResponsesHandler {
        return new BaseHttpResponsesHandler(this.injector, settings);
    }

    getForReadAll(settings?: IHttpResponseHandlerSettings): IHttpResponsesHandler {
        return new BaseHttpResponsesHandler(this.injector, settings);
    }

    getForCreate(entityTitle: string, settings?: IHttpResponseHandlerSettings): IHttpResponsesHandler {
        return new OnCreateResponseHandler(entityTitle, this.injector, settings);
    }

    getForUpdate(entityTitle: string, settings?: IHttpResponseHandlerSettings): IHttpResponsesHandler {
        return new OnUpdateResponseHandler(entityTitle, this.injector, settings);
    }

    getForDelete(entityTitle: string, settings?: IHttpResponseHandlerSettings): IHttpResponsesHandler {
        return new OnDeleteResponseHandler(entityTitle, this.injector, settings);
    }
}