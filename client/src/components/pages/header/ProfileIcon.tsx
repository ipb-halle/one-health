import { RootStoreContext } from "@/app/providers/store-provider";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";

export const ProfileIcon: React.FC = () => {
    const authStore = useContext(RootStoreContext).authStore;
    const navigate = useNavigate();

    const handleClick = () => {
        if (authStore.isAuthenticated) {
            navigate("/profile");
        }
        else {
            void authStore.loginOrLogout(window);
        }
    };

    return (
        <button
            type="button"
            className="profile-shortcut"
            onClick={handleClick}
            aria-label={
                authStore.isAuthenticated
                    ? "My profile"
                    : "Sign in with ORCID"
            }
            title={
                authStore.isAuthenticated
                    ? "My profile"
                    : "Sign in with ORCID"
            }
        >
            <i className="pi pi-user" aria-hidden="true" />
            {authStore.isAuthenticated && (
                <span>Hi {authStore.initials}</span>
            )}
        </button>
    );
};
