import React, { useRef, useState, useContext } from 'react';
import { MenuItem } from 'primereact/menuitem';
import { dependencyFactory } from "@/app/di";
import { IDocumentService } from "@/core/services/document-service";
import { SERVICES } from "@/app/di/service-types";
import MessageServiceContext from "@/app/providers/messages/message-service.context";

export const useDesktopUpload = () => {
    const fileInputRef = useRef<HTMLInputElement>(null);
    const [isUploading, setIsUploading] = useState(false);
    const { messageService } = useContext(MessageServiceContext);

    const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file || !messageService) return;

        setIsUploading(true);
        try {
            const documentService = dependencyFactory.get<IDocumentService>(
                SERVICES.IDocumentService,
            );
            const response = await documentService.uploadDocument(file, messageService);

            messageService.show({
                severity: 'success',
                summary: 'Success',
                detail: response.message || 'Document uploaded successfully',
            });
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Failed to upload document';
            messageService.show({
                severity: 'error',
                summary: 'Error',
                detail: errorMessage,
            });
        } finally {
            setIsUploading(false);
            if (fileInputRef.current) {
                fileInputRef.current.value = '';
            }
        }
    };

    // Return the menu item configuration AND the hidden input element renderer
    const uploadItem: MenuItem = {
        label: isUploading ? 'Uploading...' : 'Upload Document',
        icon: 'pi pi-upload',
        disabled: isUploading,
        command: () => {
            fileInputRef.current?.click();
        },
    };

    const FileInputRenderer = () => (
        <input
            ref={fileInputRef}
            type="file"
            style={{ display: 'none' }}
            onChange={handleFileSelect}
            accept=".pdf,.csv,.txt"
        />
    );

    return { uploadItem, FileInputRenderer };
};