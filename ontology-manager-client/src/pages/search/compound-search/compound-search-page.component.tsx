import { useState } from "react";
import { StructureFilterMatchMode } from "../../../features/filters/enums/structure-filter-match-mode";
import { CollectionPlaceholderComponent, PageTitle } from "../../../components";
import { Panel } from "primereact/panel";
import StructureEditor from "../../../features/filters/structure-editor.component";
import { Dropdown } from "primereact/dropdown";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";

export interface StructureSearchQuery {
    maxResults: number;
    matchMode?: string;

}

export const CompoundSearchPageComponent: React.FC = () => {

    const maxResultsOptions = [50, 100, 150, 200, 250].map(x => { return { label: x, value: x }; });
    const structureFilterMatchModes = Object.entries(StructureFilterMatchMode).map(([key, value]) => { return { label: value, value: key }; });
    const [query, setQuery] = useState<StructureSearchQuery>({ maxResults: 50 });


    return <div className="container">
        <PageTitle icon='fa fa-atom' title='Compound Search' help={true} />

        <div className="row" style={{ marginBottom: 20 }}>
            <div className="col-6">
                <Panel header="Search by structure">
                    <div style={{ height: '490px' }}>

                        <div style={{ height: "450px", paddingBottom: "10px" }}>

                            <StructureEditor></StructureEditor>
                        </div>
                        <div className="row">
                            <div className="col-7">

                                <Dropdown
                                    style={{ width: "350px" }}
                                    options={structureFilterMatchModes}
                                    value={query.matchMode}
                                    onChange={x => setQuery({ ...query, matchMode: x.value })}
                                >
                                </Dropdown>
                            </div>
                            <div className="col-3">

                                <Dropdown
                                    style={{ width: '85px' }}
                                    options={maxResultsOptions}
                                    value={query.maxResults}
                                    onChange={x => setQuery({ ...query, maxResults: x.value })}>
                                </Dropdown>
                            </div>
                            <div className="col-2" style={{ paddingRight: 5 }}>

                                <Button
                                    style={{ marginRight: 5 }}
                                    label="Search">

                                </Button>

                            </div>

                        </div>
                    </div>

                </Panel>
            </div>
            <div className="col-6">
                <Panel header="Search by attribute (exact match)">
                    <div style={{ height: '490px' }}>
                        <div style={{ height: '450px' }}>

                            <div className="form-group">
                                <label className="font-bold block mb-2">SMILES</label>
                                <InputText
                                    className="form-control"
                                    id="inchI" />
                            </div>
                            <div className="form-group">
                                <label className="font-bold block mb-2">InChI</label>
                                <InputText
                                    className="form-control"
                                    id="entityType.pluralName" />
                            </div>

                            <div className="form-group">
                                <label className="font-bold block mb-2">InChI Key</label>
                                <InputText
                                    className="form-control"
                                    id="entityType.pluralName" />
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-10">

                            </div>
                            <div className="col-2" style={{ paddingRight: 5 }}>

                                <Button
                                    style={{ marginRight: 5 }}
                                    label="Search">

                                </Button>

                            </div>

                        </div>

                    </div>

                </Panel>
            </div>

        </div>

        <Panel header="Results">
            <CollectionPlaceholderComponent icon='pi pi-list' message=''/>
        </Panel>
    </div>;
};


export default CompoundSearchPageComponent;