import React, { useRef, useState } from 'react';
import { Toast } from 'primereact/toast';
import {
    FileUpload,
    FileUploadHeaderTemplateOptions,
    FileUploadSelectEvent,
    FileUploadUploadEvent,
    ItemTemplateOptions,
} from 'primereact/fileupload';
import { ProgressBar } from 'primereact/progressbar';
import { Button } from 'primereact/button';
import { Tooltip } from 'primereact/tooltip';
import { Tag } from 'primereact/tag';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

export default function TemplateDemo() {
    const toast = useRef<Toast>(null);
    const [totalSize, setTotalSize] = useState(0);
    const fileUploadRef = useRef<FileUpload>(null);

    const [dataset, setDataset] = useState<any[]>([]);
    const [header, setHeader] = useState<any[]>([]);
    const [body, setBody] = useState<any[]>([]);

    // put this functions in utils files
    const processChunkAsync = (chunk: any) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            let lines: any = [];

            reader.onload = () => {
                const chunkText = reader.result?.toString();
                if (!chunkText) return;
                const chunkLines = chunkText.split('\n');
                lines.push(...chunkLines);

                if (chunkLines.length === 1 && chunkLines[0] === '') {
                    // If the last line is empty, remove it
                    lines.pop();
                }

                resolve(lines);
            };

            reader.readAsText(chunk);
        });
    };

    const readNextChunk = (file: any, offset: number, chunkSize: number) => {
        const chunk = file.slice(offset, offset + chunkSize);
        return chunk;
    };

    const onTemplateSelect = async (e: any) => {
        var chunk = readNextChunk(e.files[0], 0, 1024 * 1024);
        var lines: any = await processChunkAsync(chunk);
        var dataset = lines.slice(0, 5).map((x: any) => {
            return x.split(',');
        });
        var header = dataset[0];

        var body: any = [];
        dataset.slice(1, 5).forEach((element: any) => {
            let newElement: any = {};
            element.forEach((value: any, index: number) => {
                newElement[header[index]] = value;
            });
            body.push(newElement);
        });
        setDataset(dataset);
        setHeader(header);
        setBody(body);
    };

    const onTemplateUpload = (e: FileUploadUploadEvent) => {
        let _totalSize = 0;

        e.files.forEach((file) => {
            _totalSize += file.size || 0;
        });

        setTotalSize(_totalSize);
        toast.current?.show({
            severity: 'info',
            summary: 'Success',
            detail: 'File Uploaded',
        });
    };

    const onTemplateRemove = (file: File, callback: Function) => {
        setTotalSize(totalSize - file.size);
        callback();
    };

    const onTemplateClear = () => {
        setTotalSize(0);
    };

    const headerTemplate = (options: FileUploadHeaderTemplateOptions) => {
        const { className, chooseButton, uploadButton, cancelButton } = options;
        const value = totalSize / 10000;
        const formatedValue =
            fileUploadRef && fileUploadRef.current
                ? fileUploadRef.current.formatSize(totalSize)
                : '0 B';

        return (
            <div
                className={className}
                style={{
                    backgroundColor: 'transparent',
                    display: 'flex',
                    alignItems: 'center',
                }}>
                {chooseButton}
                {uploadButton}
                {cancelButton}
                <div className="flex align-items-center gap-3 ml-auto">
                    <span>{formatedValue} / 1 MB</span>
                    <ProgressBar
                        value={value}
                        showValue={false}
                        style={{
                            width: '10rem',
                            height: '12px',
                        }}></ProgressBar>
                </div>
            </div>
        );
    };

    const itemTemplate = (inFile: object, props: ItemTemplateOptions) => {
        if (dataset.length > 0) {
            return (
                <DataTable value={body} size="small" columnResizeMode="expand">
                    {header.map((x: any) => (
                        <Column
                            field={x}
                            header={x}
                            style={{ width: '50px' }}></Column>
                    ))}
                </DataTable>
            );
        } else {
            return <></>;
        }
    };

    const emptyTemplate = () => {
        return (
            <div className="flex align-items-center flex-column">
                <span
                    style={{
                        fontSize: '1.2em',
                        color: 'var(--text-color-secondary)',
                    }}
                    className="my-5">
                    Drag and Drop File Here
                </span>
            </div>
        );
    };

    const chooseOptions = {
        icon: 'pi pi-fw pi-file',
        iconOnly: true,
        className: 'custom-choose-btn p-button-rounded p-button-outlined',
    };
    const uploadOptions = {
        icon: 'pi pi-fw pi-cloud-upload',
        iconOnly: true,
        className:
            'custom-upload-btn p-button-success p-button-rounded p-button-outlined',
    };
    const cancelOptions = {
        icon: 'pi pi-fw pi-times',
        iconOnly: true,
        className:
            'custom-cancel-btn p-button-danger p-button-rounded p-button-outlined',
    };

    return (
        <div>
            <Toast ref={toast}></Toast>

            <Tooltip
                target=".custom-choose-btn"
                content="Choose"
                position="bottom"
            />
            <Tooltip
                target=".custom-upload-btn"
                content="Upload"
                position="bottom"
            />
            <Tooltip
                target=".custom-cancel-btn"
                content="Clear"
                position="bottom"
            />

            <FileUpload
                ref={fileUploadRef}
                name="demo[]"
                url="/api/upload"
                multiple
                accept=".csv"
                onUpload={onTemplateUpload}
                onSelect={onTemplateSelect}
                onError={onTemplateClear}
                onClear={onTemplateClear}
                headerTemplate={headerTemplate}
                itemTemplate={itemTemplate}
                emptyTemplate={emptyTemplate}
                chooseOptions={chooseOptions}
                uploadOptions={uploadOptions}
                cancelOptions={cancelOptions}
            />
        </div>
    );
}
