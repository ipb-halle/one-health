import { types } from "mobx-state-tree";


export const TutorialStore = types
    .model({
        showCompoundSearchTutorial: false,
        showGeneralSearchTutorial: false,
        showNeighborhoodExplorerTutorial: false,
        showCoOccurrencesSummaryTutorial: false,
    })
    .actions((self) => ({
        ChangeShowCompoundSearchTutorial(value: boolean) : void {
            self.showCompoundSearchTutorial = value;
        },
        ChangeShowGeneralSearchTutorial(value: boolean) : void {
            self.showGeneralSearchTutorial = value;
        },
        ChangeShowNeighborhoodExplorerTutorial(value: boolean) : void {
            self.showNeighborhoodExplorerTutorial = value;
        },
        ChangeShowCoOccurrencesSummaryTutorial(value: boolean) : void {
            self.showCoOccurrencesSummaryTutorial = value;
        }
    }))