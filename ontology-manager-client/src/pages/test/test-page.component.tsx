import { Button } from "primereact/button";
import { useEffect, useRef } from "react";

// @ts-ignore: TS7016: Could not find a declaration file for module 'smiles-drawer'.
import SmilesDrawer from "smiles-drawer";
const React = require('react');

const TestPageComponent: React.FC = () => {

    const canvasRef = useRef(null);

    const draw = () => {

            const canvas = document.getElementById('molecular-draw') as HTMLCanvasElement;

            let drawer = new SmilesDrawer.Drawer({
                width: 400,
                height: 400
              });
            

            console.log("haaaaaaaaaaa");
            SmilesDrawer.parse('CC(=O)Oc1ccccc1C(=O)O', function (tree:any) {
                console.log("hereeeeeeeee");
                drawer.draw(tree, 'test-canvas', "light", false);
            }, (error:any) => {
                console.error(error);
              });

        
            // OpenChemLib.StructureView.drawMolecule(canvas, molecule);
            // renderer.draw(molecule, 'molecular-draw');
    
    };



    return <>

    <canvas id="test-canvas" width={"400px"} height={"400px"} ref={canvasRef}></canvas>
    This is the test page
    <Button onClick={draw}></Button>
    <SmileDrawerContainer></SmileDrawerContainer>
    
    </>;
};


const SmileDrawerContainer = () => {
    const canvasRef = useRef<HTMLCanvasElement>(null);
  
    let drawer = new SmilesDrawer.Drawer({
      width: 400,
      height: 400
    });
  
    SmilesDrawer.parse('CC(=O)Oc1ccccc1C(=O)O', function (tree:any) {
      drawer.draw(tree, canvasRef.current, "light", false);
    });
  
    return (
      <canvas id="canvas" ref={canvasRef} width={"400px"} height={"400px"} />
    );
  };

export default TestPageComponent;


