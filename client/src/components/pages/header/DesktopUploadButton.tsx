import React, { useState, useContext } from 'react';
import { dependencyFactory } from "@/app/di";
import { IDocumentService } from "@/core/services/document-service";
import { SERVICES } from "@/app/di/service-types";
import MessageServiceContext from "@/app/providers/messages/message-service.context";

export const DesktopUploadButton: React.FC = () => {
    const [isDraggingOver, setIsDraggingOver] = useState(false);
    const [isUploading, setIsUploading] = useState(false);
    const { messageService } = useContext(MessageServiceContext);

    const handleFiles = async (files: FileList | null) => {
        const file = files?.[0];
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
        }
    };

    const handleDragOver = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDraggingOver(true);
    };

    const handleDragLeave = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDraggingOver(false);
    };

    const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDraggingOver(false);
        handleFiles(e.dataTransfer.files);
    };

    return (
        <div 
            onDragOver={handleDragOver}
            onDragLeave={handleDragLeave}
            onDrop={handleDrop}
            style={{
                backgroundColor: isDraggingOver ? '#28a745' : 'transparent',
                color: isDraggingOver ? '#ffffff' : 'inherit',
                cursor: isUploading ? 'not-allowed' : 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem',
                padding: '0.5rem 1rem',
                userSelect: 'none',
            }}
        >
            <i className={isUploading ? 'pi pi-spin pi-spinner' : 'pi pi-upload'} />
            <span>{isUploading ? 'Uploading...' : isDraggingOver ? 'Drop file here' : 'Upload Document'}</span>
        </div>
    );
};

export default DesktopUploadButton;