import { RootStoreContext } from "@/app/providers/store-provider";
import { useContext } from "react";

export const ProfileIcon: React.FC = () => {
    const authStore = useContext(RootStoreContext).authStore;
    const initialsComponent = <span className="shortcut-icon">Hi {authStore.initials}</span>;
    const anonymousUserComponent = <i className="pi pi-user shortcut-icon" />;
  
    return authStore.isAuthenticated ? initialsComponent : anonymousUserComponent;
};
