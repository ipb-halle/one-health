function FormulaRenderer(data: {formula:string}) {

    const formula = splitFormula(data.formula).map((e) => {
        if (e.match("[0-9]+")) {
            return <sub>{e}</sub>
        } else {
            return <span>{e}</span>
        }
    });
        

    return <div>{formula}</div>
}
export default (FormulaRenderer)

function splitFormula(formula: string): string[] {
    let elements: string[] = [];
    let startIndex = 0;

    if (formula.length == 0) {
        return elements;
    }
    let wasDigit = false;
    let startPos = 0;

    for(let i = 0; i < formula.length; i++) {
        let isDigit = (0x2f < formula.charCodeAt(i)) && (formula.charCodeAt(i) < 0x3a);
        if ((isDigit != wasDigit) && (startPos != i)) {
            elements.push(formula.substring(startPos, i));
            wasDigit = isDigit;
            startPos = i;
        }
    }
    elements.push(formula.substring(startPos));
    return elements;
}