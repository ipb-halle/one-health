import { action } from "mobx"
import { types } from "mobx-state-tree";

const MOBILE_MAX_WIDTH_PX = 768

export const ScreenDeviceStore = types
    .model({
        isMobile: types.optional(types.boolean, false),
    }).volatile(() => ({
        mql: undefined as MediaQueryList | undefined,
        stopListener: undefined as (() => void) | undefined,
    }))
    .actions((self) => ({
        changeMobile(newState: boolean): void {
            self.isMobile = newState;
        },
    }))
    .actions((self) => ({
        start() {
            if (typeof window === "undefined") return;

            self.mql = window.matchMedia(`(max-width: ${MOBILE_MAX_WIDTH_PX}px)`);

            const onChange = action(() => {

                self.changeMobile(!!self.mql?.matches);
            })

            onChange()
            self.mql.addEventListener("change", onChange)

            self.stopListener = () => self.mql?.removeEventListener("change", onChange)
        },
        stop() {
            self.stopListener?.()
            self.stopListener = undefined
            self.mql = undefined
        }
    }))
