
import React, { useContext, useEffect, useState } from 'react';
const OpenChemLib = require("openchemlib/full");

interface MolecularDrawComponentProps {
    element:any
}



const MolecularDrawComponent: React.FC<MolecularDrawComponentProps> = ({element}) => {
    
    const [hidden, setHidden] = useState<boolean>(false);
    
    useEffect(() => {
        const smiles = element.properties.find((x:any) => x.name === "SMILES")

        if (smiles){
            setHidden(false);
            const canvas = document.getElementById('molecular-draw') as HTMLCanvasElement;
            const molecule = OpenChemLib.Molecule.fromSmiles(smiles.value);
        
            OpenChemLib.StructureView.drawMolecule(canvas, molecule);
            // renderer.draw(molecule, 'molecular-draw');
        } else {
            setHidden(true);
        }
    }, [element])


    return (
        <canvas id="molecular-draw" hidden={hidden}></canvas>
    );
};

export default MolecularDrawComponent;
