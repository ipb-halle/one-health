import React, { useContext, useEffect, useState } from 'react';
import OpenChemLib from 'openchemlib/full';

import SmilesDrawerLib from 'smiles-drawer';

interface MolecularDrawComponentProps {
    smiles: string;
    xkey: number;
}

const MolecularDrawComponent: React.FC<MolecularDrawComponentProps> = ({
    smiles,
    xkey,
}) => {
    const [hidden, setHidden] = useState<boolean>(false);

    useEffect(() => {
        const svg = document.getElementById('svg-canvas-' + xkey.toString());

        if (smiles && svg) {
            setHidden(false);
            let smilesDrawer = new SmilesDrawerLib.SvgDrawer({
                compactDrawing: false,
            });

            SmilesDrawerLib.parse(
                smiles,
                (tree: any) => {
                    smilesDrawer.draw(tree, svg, 'light', false);
                },
                (error: any) => {
                    console.error(error);
                },
            );
        } else {
            setHidden(true);
        }
    }, [smiles]);

    return (
        <div hidden={hidden}>
            <svg id={'svg-canvas-' + xkey.toString()}></svg>
        </div>
    );
};

export default MolecularDrawComponent;
