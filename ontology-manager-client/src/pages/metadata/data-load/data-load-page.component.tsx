import { Panel } from 'primereact/panel';
import { Dropdown } from 'primereact/dropdown';
import { FileUpload } from 'primereact/fileupload';
import { PageTitle } from '../../../components';
import { useContext, useEffect, useState } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Tooltip } from 'primereact/tooltip';
import { useRef } from 'react';
import {  FileUploadHeaderTemplateOptions, FileUploadSelectEvent, FileUploadUploadEvent, ItemTemplateOptions,} from 'primereact/fileupload';
import { dependencyFactory } from '../../../features/shared/injection';
import { IDataSourceService, IEntityTypeService, ILinkTypeService, IMetadataService, SERVICES } from '../../../services';
import DataPreview from '../../../features/modules/metadata/data-load/data-preview.component';
import { MessageServiceContext } from '../../../features/shared/messages';
import { IMapping } from '../../../features/modules/metadata/data-load/mapping';
import { SelectableOption } from '../../../utils/selectable-option';
import { IDataSource } from '../../../features/modules/metadata/data-sources';
import { processChunkAsync, readNextChunk } from '../../../utils/files';
import { InputText } from 'primereact/inputtext';
import DatasetList from '../../../features/modules/metadata/data-sources/dataset-list.component';
import { Messages } from 'primereact/messages';
import { useMountEffect } from 'primereact/hooks';




const React = require('react');

const metadataService = dependencyFactory.get<IMetadataService>(SERVICES.IMetadataService);
const entityTypeService = dependencyFactory.get<IEntityTypeService>(SERVICES.IEntityTypeService);
const linkTypeService = dependencyFactory.get<ILinkTypeService>(SERVICES.ILinkTypeService);
const dataSourceService = dependencyFactory.get<IDataSourceService>(SERVICES.IDataSourceService);

const DataPreviewMemo = React.memo(DataPreview);


const DataLoadPageComponent: React.FC = () => {
    const {messageService} = useContext(MessageServiceContext);

    const [dataset, setDataset] = useState<any[]>([]);
    const [header, setHeader] = useState<any[]>([]);
    const [body, setBody] = useState<any[]>([]);
    const [options, setOptions] = useState<any[]>([]);
    const [mapping, setMapping] = useState<IMapping>({
        type: "",
        definitionId: "",
        mapping: {}
    });

    const msgs = useRef<Messages>(null);
    
    const [element, setElement] = useState<any>({});

    const [file, setFile] = useState<any>();
    const [name, setName] = useState<string>();

    const [elementId, setElementId] = useState<string>("");

    const [elementOptions, setElementOptions] = useState<SelectableOption[]>([]);

   

    const [dataSource, setDataSource] = useState<IDataSource>({
        name: "",
    });
    const [uploading, setUploading] = useState<boolean>(false);

    const init = async () => {
        let elementOptionsResult = await metadataService.getAllAsOptions(messageService!);
        setElementOptions(elementOptionsResult);
    }

    useEffect(() => {
        init()
    }, [])

    useEffect(() => {
        onSelectedElementChangeHandler();
    }, [elementId])

    useMountEffect(() => {
        if (msgs.current) {
            msgs.current.clear();
            msgs.current.show([
                {
                    severity: 'error',
                    sticky: true,
                    content: (
                        <p>
                            This feature is currently in beta and is not fully stable. While we encourage you to explore and provide feedback, please be aware that using this feature will lead to bugs, crashes, or unexpected behavior.
                        </p>
                    )
                }
            ]);
        }
    }); 

    useEffect(() => {
        if (uploading)
            onUploadHandler();
    }, [uploading])

    useEffect(() => {
        setOptions(header.map((x:any) => {return {label: x, value: x}}));
    }, [header])

    useEffect(() => {
    }, [mapping])

    const onSelectedElementChangeHandler = async () => {

        if (!elementId)
            return;
        
        let newType = elementOptions.find(x => x.value === elementId)?.data.type;

        


        if (newType === "entity"){
            let newElement = await entityTypeService.get(elementId, messageService!);
            setElement(newElement);
        }

        if (newType === "link"){
            let newElement = await linkTypeService.get(elementId, messageService!);
            setElement(newElement);
        }
    }


    const onTemplateSelect = async (e:any) => {
        setFile(e.files[0]);

        var chunk = readNextChunk(e.files[0], 0, 1024 * 1024);
        var text: any = await processChunkAsync(chunk);

        var lines = text.split('\n');
      
        if (lines.length === 1 && lines[0] === '') {
        // If the last line is empty, remove it
        lines.pop();
        }

        var dataset = lines.slice(0,5).map((x:any) => {return x.split(',')})
        var header = dataset[0];

        setDataset(dataset);
        
        var body:any = [];
        dataset.slice(1,5).forEach((element:any) => {
            let newElement :any = {}
            element.forEach((value:any, index:number) => {
                newElement[header[index]] = value;
            })
            body.push(newElement)
        });
        setHeader(header);
        setBody(body);
    };

    const onTemplateUpload = async (e: FileUploadUploadEvent) => {
        let _totalSize = 0;

        e.files.forEach((file) => {
            _totalSize += file.size || 0;
        });

        messageService!.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
    };

    const onUploadHandler = async () => 
    {
        const result = await dataSourceService.create(dataSource, messageService!);
        setDataSource(result);

        var offset = 0;
        var i = 0;
        while (offset < file.size){
            const chunk = readNextChunk(file, offset, 1024*1024);
            

            var text: any = await processChunkAsync(chunk);
            if (result.id)
                await dataSourceService.writeFile(result.id, text, messageService!);
            offset += chunk.size;
            i++;
        }

        setUploading(false);
    };

    const onTemplateRemove = (file: File, callback: Function) => {

    };

    const onTemplateClear = () => {
    };

    const headerTemplate = (options: FileUploadHeaderTemplateOptions) => {
        const { className, chooseButton, uploadButton, cancelButton } = options;


        return (
            <div className={className} style={{ backgroundColor: 'transparent', display: 'flex', alignItems: 'center' }}>
                {chooseButton}
                {uploadButton}
                {cancelButton}
                <div className="flex align-items-center gap-3 ml-auto">
                <InputText 
                        style={{width: "300px"}} 
                        placeholder='Enter your data source name'
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        />
                </div>
            </div>
        );
    };

    const itemTemplate = (inFile: object, props: ItemTemplateOptions) => {
        return <></>;
    };

    const emptyTemplate = () => {
        return <></>;
    };

    const chooseOptions = { icon: 'pi pi-fw pi-file', iconOnly: true, className: 'custom-choose-btn p-button-rounded p-button-outlined' };
    const uploadOptions = { icon: 'pi pi-fw pi-cloud-upload', iconOnly: true, className: 'custom-upload-btn p-button-success p-button-rounded p-button-outlined' };
    const cancelOptions = { icon: 'pi pi-fw pi-times', iconOnly: true, className: 'custom-cancel-btn p-button-danger p-button-rounded p-button-outlined' };


    return (
        <div className="container" style={{pointerEvents: 'none', cursor: 'not-allowed'}} >
            <PageTitle icon='pi pi-download' title='Data Load' help={true}/>
            <Messages ref={msgs} />

            <div className='mb-3'>

            <Tooltip target=".custom-choose-btn" content="Choose" position="bottom" />
            <Tooltip target=".custom-upload-btn" content="Upload" position="bottom" />
            <Tooltip target=".custom-cancel-btn" content="Clear" position="bottom" />

        
            <FileUpload name="demo[]" url="/api/upload" multiple accept=".csv"
                customUpload={true}
                uploadHandler={() => {setUploading(true)}}
                onUpload={onTemplateUpload} onSelect={onTemplateSelect} onError={onTemplateClear} onClear={onTemplateClear}
                headerTemplate={headerTemplate} itemTemplate={itemTemplate} emptyTemplate={emptyTemplate}
                chooseOptions={chooseOptions} uploadOptions={uploadOptions} cancelOptions={cancelOptions} />
            </div>

            <DataPreviewMemo header={header} body={body}></DataPreviewMemo>
            
            
            {/* {body.length > 0 && 
            <DataTable value={body} size='small' >
            {header.map((x:any) => <Column field={x} header={x} style={{width:"50px"}}></Column>)}
        </DataTable>
            } */}
    
            <div className="row pt-3">
                <div className="col-md-5">
                    <Panel header="Property Mapping" className='panel-no-padding' icons={() => {return  <Dropdown
                                value={elementId}
                                options={elementOptions} 
                                style={{ width: '240px' }}
                                filter
                                onChange={(e) => {setElementId(e.target.value)}}
                                
                            />}}>

                  

                        <div style={{ height: '560px', overflow: 'scroll' }}>
                            {element.properties !== undefined && element.properties.map((x:any) => (
                                <div
                                    className="row py-1"
                                    style={{
                                        borderBottom: '1px solid #dee2e6',
                                        margin: 0,
                                    }}
                                >
                                    <div className="col-sm-6 pt-2">
                                        <label>{x.name}</label>
                                    </div>
                                    <div className="col-sm-6">
                                        <Dropdown
                                            filter
                                            id = {`${x}-map`}
                                            value={mapping.mapping[x.name]}
                                            options={options}
                                            optionLabel='label'
                                            optionValue='value'
                                            style={{ width: '100%' }}
                                            placeholder="Select the source column"
                                            onChange={(e) => { mapping.mapping[x.name] = e.target.value; setMapping({...mapping});    }}
                                        ></Dropdown>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </Panel>
                </div>
                <div className='col-md-7' style={{ height: '560px'}}>
                    <DatasetList ></DatasetList>
                </div>
             
            </div>
        </div>
    );
};

export default DataLoadPageComponent;
