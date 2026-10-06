import { MenuItem } from "primereact/menuitem"

export function createMenuItem(
    label: string, 
    icon: string, 
    action: () => void): MenuItem {
    return {
        label: label,
        icon: icon,
        command: action,
    }
}