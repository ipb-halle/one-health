
import { injectable } from "inversify";

@injectable()
export class ITutorialStore {

    getShowNeighborhoodExplorerTutorial() : boolean { throw Error() };
    setShowNeighborhoodExplorerTutorial(value: boolean) : void { throw Error() };

    getShowCoOccurrencesSummaryTutorial() : boolean { throw Error() };
    setShowCoOccurrencesSummaryTutorial(value: boolean) : void { throw Error() };

};

@injectable()
export class TutorialStore extends ITutorialStore {

    showNeighborhoodExplorerTutorialVarName : string = 'one-health-show-neighborhood-explorer-tutorial';
    showCoOccurrencesSummaryTutorialVarName : string = 'one-health-show-cooccurrences-summary-tutorial';

    constructor(){
        super();
        let value = localStorage.getItem(this.showNeighborhoodExplorerTutorialVarName);
        if (!value){
            this.setShowNeighborhoodExplorerTutorial(true);
        }

        value = localStorage.getItem(this.showCoOccurrencesSummaryTutorialVarName);
        if (!value){
            this.setShowCoOccurrencesSummaryTutorial(true);
        }
    }

    getShowNeighborhoodExplorerTutorial(): boolean {
        const value = localStorage.getItem(this.showNeighborhoodExplorerTutorialVarName);
        if (!value){
            this.setShowNeighborhoodExplorerTutorial(true);
            return true;
        } else {
            return value === "true";
        }
    }

    setShowNeighborhoodExplorerTutorial(value: boolean): void {
        localStorage.setItem(this.showNeighborhoodExplorerTutorialVarName, value.valueOf().toString());
    }

    getShowCoOccurrencesSummaryTutorial(): boolean {
        const value = localStorage.getItem(this.showCoOccurrencesSummaryTutorialVarName);
        if (value) {
            return value === "true";
        } else {
            this.setShowCoOccurrencesSummaryTutorial(true);
            return true;
        }
    }

    setShowCoOccurrencesSummaryTutorial(value: boolean): void {
        localStorage.setItem(this.showCoOccurrencesSummaryTutorialVarName, value.valueOf().toString());
    }

}