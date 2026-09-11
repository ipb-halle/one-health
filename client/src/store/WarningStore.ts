import { types } from "mobx-state-tree";


export const WarningStore = types
    .model({
        showNeighborhoodExplorerWarning: false,
        showCoOccurrencesSummaryWarning: false,
    })
    .actions((self) => ({
        ChangeShowNeighborhoodExplorerWarning(value: boolean) : void {
            self.showNeighborhoodExplorerWarning = value;
        },
        ChangeShowCoOccurrencesSummaryWarning(value: boolean) : void {
            self.showCoOccurrencesSummaryWarning = value;
        }
    }))