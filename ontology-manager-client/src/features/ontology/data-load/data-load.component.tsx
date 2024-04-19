import { Panel } from 'primereact/panel';
import { Dropdown } from 'primereact/dropdown';
import { FileUpload } from 'primereact/fileupload';
import { PageTitle } from '../../layout';
import { useEffect, useState } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Tooltip } from 'primereact/tooltip';
import { Toast } from 'primereact/toast';
import { useRef } from 'react';
import {  FileUploadHeaderTemplateOptions, FileUploadSelectEvent, FileUploadUploadEvent, ItemTemplateOptions,} from 'primereact/fileupload';

import { IMapping } from './mapping';
import { dependencyFactory } from '../../injection/inversify.config';
import { IMetadataService } from '../../services/metadata-service';
import { IDataSourceService, IEntityTypeService, ILinkTypeService, SERVICE_TYPES } from '../../services';
import { SelectableOption } from '../../utils/selectable-option';
import { InputText } from 'primereact/inputtext';
import { IDataSource } from '../data-sources';
import { processChunkAsync, readNextChunk } from '../../utils/files';
import { table } from 'console';
import DataPreview from './data-preview.component';
import DatasetList from '../data-sources/dataset-list.component';


const React = require('react');

const metadataService = dependencyFactory.get<IMetadataService>(SERVICE_TYPES.IMetadataService);
const entityTypeService = dependencyFactory.get<IEntityTypeService>(SERVICE_TYPES.IEntityTypeService);
const linkTypeService = dependencyFactory.get<ILinkTypeService>(SERVICE_TYPES.ILinkTypeService);
const dataSourceService = dependencyFactory.get<IDataSourceService>(SERVICE_TYPES.IDataSourceService);

const DataPreviewMemo = React.memo(DataPreview);


const DataLoader: React.FC = () => {
    const toast = useRef<Toast>(null);

    const [dataset, setDataset] = useState<any[]>([]);
    const [header, setHeader] = useState<any[]>([]);
    const [body, setBody] = useState<any[]>([]);
    const [options, setOptions] = useState<any[]>([]);
    const [mapping, setMapping] = useState<IMapping>({
        type: "",
        definitionId: "",
        mapping: {}
    });

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
        let elementOptionsResult = await metadataService.getAllAsOptions(toast);
        setElementOptions(elementOptionsResult);
    }

    useEffect(() => {
        console.log("init");
        init()
    }, [])

    useEffect(() => {
        console.log("onSelectedElementHandler");
        onSelectedElementChangeHandler();
    }, [elementId])


    useEffect(() => {
        console.log("onUploadHandler");
        if (uploading)
            onUploadHandler();
    }, [uploading])

    useEffect(() => {
        setOptions(header.map((x:any) => {return {label: x, value: x}}));
        console.log(header);
    }, [header])

    useEffect(() => {
        console.log(mapping);
    }, [mapping])

    const onSelectedElementChangeHandler = async () => {

        if (!elementId)
            return;
        
        let newType = elementOptions.find(x => x.value === elementId)?.data.type;

        console.log(newType);
        


        if (newType === "entity"){
            console.log("requesting entity");
            let newElement = await entityTypeService.get(elementId, toast);
            setElement(newElement);
        }

        if (newType === "link"){
            console.log("requesting link");
            let newElement = await linkTypeService.get(elementId, toast);
            setElement(newElement);
        }
    }


    const onTemplateSelect = async (e:any) => {
        console.log("onTemplateSelect");
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

        toast.current?.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
    };

    const onUploadHandler = async () => 
    {
        console.log("here");
        console.log("siuuuuu")
        const result = await dataSourceService.create(dataSource, toast);
        setDataSource(result);

        var offset = 0;
        var i = 0;
        console.log(file)
        while (offset < file.size){
            const chunk = readNextChunk(file, offset, 1024*1024);
            

            var text: any = await processChunkAsync(chunk);
            if (result.id)
                await dataSourceService.writeFile(result.id, text, toast);
            console.log(chunk);
            console.log(`reading chunk ${i}`);
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
        <div className="container">
            <PageTitle icon='pi pi-download' title='Data Load'/>

            <div className='mb-3'>
            <Toast ref={toast}></Toast>

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
                                onChange={(e) => {console.log(e.target.value);setElementId(e.target.value)}}
                                
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

export default DataLoader;
