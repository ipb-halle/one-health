import { injectable } from 'inversify';

@injectable()
export class ITutorialStore {
    getShowNeighborhoodExplorerTutorial(): boolean {
        throw Error();
    }
    setShowNeighborhoodExplorerTutorial(value: boolean): void {
        throw Error();
    }

    getShowCoOccurrencesSummaryTutorial(): boolean {
        throw Error();
    }
    setShowCoOccurrencesSummaryTutorial(value: boolean): void {
        throw Error();
    }

    getShowCompoundSearchTutorial(): boolean {
        throw Error();
    }
    setShowCompoundSearchTutorial(value: boolean): void {
        throw Error();
    }

    getShowGeneralSearchTutorial(): boolean {
        throw Error();
    }
    setShowGeneralSearchTutorial(value: boolean): void {
        throw Error();
    }
}

@injectable()
export class TutorialStore extends ITutorialStore {
    showNeighborhoodExplorerTutorialVarName: string =
        'one-health-show-neighborhood-explorer-tutorial';
    showCoOccurrencesSummaryTutorialVarName: string =
        'one-health-show-cooccurrences-summary-tutorial';
    showCompoundSearchTutorialVarName: string =
        'one-health-show-compound-search-tutorial';
    showGeneralSearchTutotrialVarName: string =
        'one-health-show-general-search-tutorial';

    constructor() {
        super();
        let value = localStorage.getItem(
            this.showNeighborhoodExplorerTutorialVarName,
        );
        if (!value) {
            this.setShowNeighborhoodExplorerTutorial(true);
        }

        value = localStorage.getItem(
            this.showCoOccurrencesSummaryTutorialVarName,
        );
        if (!value) {
            this.setShowCoOccurrencesSummaryTutorial(true);
        }

        value = localStorage.getItem(this.showCompoundSearchTutorialVarName);
        if (!value) {
            this.setShowCompoundSearchTutorial(true);
        }

        value = localStorage.getItem(this.showGeneralSearchTutotrialVarName);
        if (!value) {
            this.setShowGeneralSearchTutorial(true);
        }
    }

    getShowGeneralSearchTutorial(): boolean {
        const value = localStorage.getItem(
            this.showGeneralSearchTutotrialVarName,
        );
        if (!value) {
            this.setShowGeneralSearchTutorial(true);
            return true;
        } else {
            return value === 'true';
        }
    }

    setShowGeneralSearchTutorial(value: boolean): void {
        localStorage.setItem(
            this.showGeneralSearchTutotrialVarName,
            value.valueOf().toString(),
        );
    }

    getShowCompoundSearchTutorial(): boolean {
        const value = localStorage.getItem(
            this.showCompoundSearchTutorialVarName,
        );
        if (!value) {
            this.setShowCompoundSearchTutorial(true);
            return true;
        } else {
            return value === 'true';
        }
    }

    setShowCompoundSearchTutorial(value: boolean): void {
        localStorage.setItem(
            this.showCompoundSearchTutorialVarName,
            value.valueOf().toString(),
        );
    }

    getShowNeighborhoodExplorerTutorial(): boolean {
        const value = localStorage.getItem(
            this.showNeighborhoodExplorerTutorialVarName,
        );
        if (!value) {
            this.setShowNeighborhoodExplorerTutorial(true);
            return true;
        } else {
            return value === 'true';
        }
    }

    setShowNeighborhoodExplorerTutorial(value: boolean): void {
        localStorage.setItem(
            this.showNeighborhoodExplorerTutorialVarName,
            value.valueOf().toString(),
        );
    }

    getShowCoOccurrencesSummaryTutorial(): boolean {
        const value = localStorage.getItem(
            this.showCoOccurrencesSummaryTutorialVarName,
        );
        if (value) {
            return value === 'true';
        } else {
            this.setShowCoOccurrencesSummaryTutorial(true);
            return true;
        }
    }

    setShowCoOccurrencesSummaryTutorial(value: boolean): void {
        localStorage.setItem(
            this.showCoOccurrencesSummaryTutorialVarName,
            value.valueOf().toString(),
        );
    }
}
