import { types } from "mobx-state-tree";


export const TutorialStore = types
    .model({
        showTutorial: false,
    })
    .actions((self) => ({
        ChangeShowTutorial(value: boolean) : void {
            self.showTutorial = value;
        }
    }))