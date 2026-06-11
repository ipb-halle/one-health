import { Dispatch, SetStateAction, useContext, useState } from 'react';
import { StructureFilterMatchMode } from '../../../filters/enums/structure-filter-match-mode';
import { Panel } from 'primereact/panel';
import { Dropdown } from 'primereact/dropdown';
import { Button } from 'primereact/button';
import { dependencyFactory } from '../../../../app/di';
import { ICompoundService, SERVICES } from '../../../..';
import MessageServiceContext from '../../../../app/providers/messages/message-service.context';
import {
    FileUpload,
    FileUploadHandlerEvent,
    FileUploadSelectEvent,
} from 'primereact/fileupload';
import { InputNumber } from 'primereact/inputnumber';
import { Slider } from 'primereact/slider';
import { Instance } from 'mobx-state-tree';
import { Entity } from '../../../../store/Entity';
export interface CompoundSearchQuery {
    value?: string;
    matchMode?: string;
    threshold?: any;
}

export interface ExactSearchQuery {
    value?: string;
    matchMode?: string;
    inchi?: string;
    inchikey?: string;
    smiles?: string;
}

interface CompoundSearchModalProps {
    editor?: any;
    elements: Instance<typeof Entity>[];
    selectedElements: Instance<typeof Entity>[];
}

export const CompoundSearchModal: React.FC<CompoundSearchModalProps> = ({
    editor,
}) => {
    const maxResultsOptions = [50, 100, 150, 200, 250, 500, 1000].map((x) => {
        return { label: x, value: x };
    });
    const structureFilterMatchModes = Object.entries(
        StructureFilterMatchMode,
    ).map(([key, value]) => {
        return { label: value, value: key };
    });

    const compoundService = dependencyFactory.get<ICompoundService>(
        SERVICES.ICompoundService,
    );
    const { messageService } = useContext(MessageServiceContext);

    const [first, setFirst] = useState<number>(0);
    const [rows, setRows] = useState<number>(50);
    const [total, setTotal] = useState<number>(0);

    const [loading, setLoading] = useState<boolean>(false);

    const [items, setItems] = useState<any[]>([]);

    const [structureQuery, setStructureQuery] = useState<CompoundSearchQuery>({
        threshold: 80,
        matchMode: 'SUBSTRUCTURE',
    });
    const [selectedCompounds, setSelectedCompounds] = useState<any[]>([]);

    const onMolFileUpload = async (e: FileUploadSelectEvent) => {
        if (e.files.length <= 0) return;

        const molFile = await e.files[0].text();

        editor.setMolFile(molFile);

        messageService!.show({
            severity: 'success',
            summary: 'Success',
            detail: 'File Uploaded',
        });
    };

    const chooseOptions = { icon: 'pi pi-fw pi-upload' };

    const onStructureSearch = async () => {
        const smiles = editor.getSmiles();

        if (!smiles) {
            // verify the smiles is valid
            messageService?.show({
                severity: 'error',
                summary: 'ERROR: Invalid input',
                detail: 'The provide structure is either empty or invalid',
            });
            return;
        }

        setSelectedCompounds([]);

        setFirst(0);
        const newStructureQuery = { ...structureQuery, value: smiles };
        setStructureQuery(newStructureQuery);

        setLoading(true);
        if (structureQuery.matchMode === 'SUBSTRUCTURE') {
            await runSubstructureSearch(newStructureQuery);
        } else if (structureQuery.matchMode === 'TAN_SIMILARITY') {
            await runSimilaritySearch(newStructureQuery);
        } else if (structureQuery.matchMode === 'EXACT_SMILES') {
            await runSMILESSearch(newStructureQuery.value);
        }
        setLoading(false);
    };

    const runSubstructureSearch = async (structureQuery: any) => {
        const page = await compoundService.getBySubstructure(
            structureQuery.value!,
            rows,
            first,
            messageService!,
        );
        setTotal(page.total);
        setItems(page.items);
    };

    const runSimilaritySearch = async (structureQuery: any) => {
        const page = await compoundService.getBySimilarity(
            structureQuery.value!,
            structureQuery.threshold,
            rows,
            messageService!,
        );
        setTotal(page.total);
        setItems(page.items);
    };

    const runSMILESSearch = async (value: string) => {
        const element = await compoundService.getBySMILES(
            value,
            messageService!,
        );
        if (element) {
            setTotal(1);
            setItems([element]);
        } else {
            setTotal(0);
            setItems([]);
        }
    };

    return (
        <div>
            <div className="row">
                <Panel
                    header="Search by structure"
                    id="compound-search-structure-panel">
                    <div style={{ height: '570px' }}>
                        <div
                            style={{
                                marginBottom: 10,
                                height: '70px',
                                display: 'flex',
                                gap: '15px',
                            }}>
                            <div style={{ width: '16%' }}>
                                <label>Max results:</label>
                                <Dropdown
                                    style={{ width: '100%' }}
                                    options={maxResultsOptions}
                                    value={rows}
                                    onChange={(x) =>
                                        setRows(x.value)
                                    }></Dropdown>
                            </div>

                            <div style={{ width: '55%' }}>
                                <label>Search mode:</label>
                                <Dropdown
                                    style={{ width: '100%' }}
                                    options={structureFilterMatchModes}
                                    placeholder="Select structure match mode..."
                                    value={structureQuery.matchMode}
                                    onChange={(x) =>
                                        setStructureQuery({
                                            ...structureQuery,
                                            matchMode: x.value,
                                        })
                                    }></Dropdown>
                            </div>

                            {structureQuery.matchMode === 'TAN_SIMILARITY' && (
                                <div style={{ width: '25%' }}>
                                    <label>Similarity threshold:</label>
                                    <div>
                                        <InputNumber
                                            inputStyle={{ width: '100%' }}
                                            value={structureQuery.threshold}
                                            onChange={(e) =>
                                                setStructureQuery({
                                                    ...structureQuery,
                                                    threshold: e.value,
                                                })
                                            }
                                            min={0}
                                            max={100}
                                            suffix="%"
                                        />
                                    </div>
                                    <Slider
                                        style={{ width: '100%' }}
                                        value={structureQuery.threshold}
                                        onChange={(e: any) =>
                                            setStructureQuery({
                                                ...structureQuery,
                                                threshold: e.value,
                                            })
                                        }
                                        min={0}
                                        max={100}
                                    />
                                </div>
                            )}
                        </div>
                        <div
                            style={{
                                height: '450px',
                                paddingBottom: '10px',
                            }}>
                            <div
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    border: '1px solid #DEE2E6',
                                }}
                                id="structureSearchEditor"
                            />
                        </div>
                        <div className="row" style={{ paddingRight: '5px' }}>
                            <div className="col-3">
                                <FileUpload
                                    mode="basic"
                                    name="demo[]"
                                    url="/api/upload"
                                    auto
                                    customUpload={true}
                                    accept=".mol"
                                    maxFileSize={1000000}
                                    uploadHandler={(
                                        e: FileUploadHandlerEvent,
                                    ) => {
                                        e.options.clear();
                                    }}
                                    onSelect={onMolFileUpload}
                                    chooseLabel="Load file"
                                    chooseOptions={chooseOptions}
                                />
                                {/* <SplitButton label="Load" icon="pi pi-upload" model={uploadOptions} /> */}
                            </div>

                            <div className="col-7"></div>

                            <div className="d-flex col-2 justify-content-end">
                                <Button
                                    label="Search" // TODO: wenn geklickt dann modal schließen
                                    onClick={onStructureSearch}></Button>
                            </div>
                        </div>
                    </div>
                </Panel>
            </div>
        </div>
    );
};

export default CompoundSearchModal;
