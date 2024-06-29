
import React, { useContext, useEffect, useState } from 'react';
const OpenChemLib = require("openchemlib/full");
const SmilesDrawerLib = require('smiles-drawer');



interface MolecularDrawComponentProps {
    element:any
}



const MolecularDrawComponent: React.FC<MolecularDrawComponentProps> = ({element}) => {
    
    const [hidden, setHidden] = useState<boolean>(false);

  
    
    useEffect(() => {
        const smiles = element.properties.find((x:any) => x.name === "SMILES")

        let smilesDrawer = new SmilesDrawerLib.SvgDrawer({compactDrawing: false});

        if (smiles){

            setHidden(false);


            console.log(SmilesDrawerLib);
            const canvas = document.getElementById('molecular-draw') as HTMLCanvasElement;

            console.log("haaaaaaaaaaa");
            console.log(smiles);
            SmilesDrawerLib.parse(smiles.value, (tree:any) => {
                // Draw to the canvas
                console.log("here drawing");
                console.log(tree);
        
                const canvas = document.getElementById('molecular-draw') as HTMLCanvasElement;

                console.log(canvas);
                smilesDrawer.draw(tree, "svg-canvas", "light", false);
                // Alternatively, draw to SVG:
                // svgDrawer.draw(tree, 'output-svg', 'dark', false);
              }, (error:any) => {
                console.error(error);
              });

            const molecule = OpenChemLib.Molecule.fromSmiles(smiles.value);
        
            // OpenChemLib.StructureView.drawMolecule(canvas, molecule);
            // renderer.draw(molecule, 'molecular-draw');
        } else {
            setHidden(true);
        }
    }, [element])


    return (
        <>
        <svg id="svg-canvas"></svg>
        </>
    );
};

export default MolecularDrawComponent;
