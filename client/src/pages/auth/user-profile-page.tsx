import { useEffect, useState } from "react";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { Tag } from "primereact/tag";
import { Divider } from "primereact/divider";
import { Message } from "primereact/message";
import "./user-profile-page.scss";

// Import Orval generated API clients and types
import { getCurrentUser } from "@/generated/auth/auth/auth";
import { updateCurrentUser, deleteCurrentUser } from "@/generated/auth/users/users";
import type { ErrorResponse, UpdateUserRequest, User } from "@/generated/auth/model";
import { HttpError } from "@/core/api/http/http-error";

const isErrorResponse = (data: unknown): data is ErrorResponse =>
    typeof data === "object" && data !== null && typeof (data as ErrorResponse).message === "string";

// The `httpRequest` mutator resolves with the raw response body (not Orval's declared {data,status,headers} wrapper).
const fetchCurrentUser = (options?: Parameters<typeof getCurrentUser>[0]): Promise<User> =>
    getCurrentUser(options) as unknown as Promise<User>;

const saveCurrentUser = (body: UpdateUserRequest): Promise<User> =>
    updateCurrentUser(body) as unknown as Promise<User>;

const describeError = (err: unknown, fallback: string): string => {
    if (err instanceof HttpError && isErrorResponse(err.data)) {
        return err.data.message;
    }
    return err instanceof Error ? err.message : fallback;
};

const UserProfilePage: React.FC = () => {
    const [profile, setProfile] = useState<User | null>(null);
    const [displayName, setDisplayName] = useState("");
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [error, setError] = useState("");
    const [notice, setNotice] = useState("");

    useEffect(() => {
        const controller = new AbortController();

        async function loadProfile() {
            setLoading(true);
            setError("");

            try {
                const user = await fetchCurrentUser({ signal: controller.signal });

                setProfile(user);
                setDisplayName(user.displayName ?? "");
            } catch (err) {
                if (controller.signal.aborted) return;
                if (err instanceof HttpError && err.status === 401) {
                    setError("You are not signed in.");
                } else if (err instanceof HttpError && err.status === 404) {
                    setError("Profile not found.");
                } else {
                    setError(describeError(err, "An unexpected error occurred."));
                }
            } finally {
                if (!controller.signal.aborted) setLoading(false);
            }
        }

        void loadProfile();
        return () => controller.abort();
    }, []);

    async function saveProfile() {
        if (!profile) return;

        const trimmedName = displayName.trim();
        if (!trimmedName) {
            setError("Display name cannot be empty.");
            return;
        }

        setSaving(true);
        setError("");
        setNotice("");

        try {
            const updatedUser = await saveCurrentUser({ displayName: trimmedName });

            setProfile(updatedUser);
            setDisplayName(updatedUser.displayName);
            setNotice("Your profile has been updated.");
        } catch (err) {
            if (err instanceof HttpError && err.status === 401) {
                setError("Your session has expired. Please sign in again.");
            } else {
                setError(describeError(err, "Could not save profile."));
            }
        } finally {
            setSaving(false);
        }
    }

    async function deleteAccount() {
        const confirmed = window.confirm(
            "Are you sure you want to delete your account? " +
            "This action may be irreversible. Review the data-retention " +
            "information before proceeding."
        );

        if (!confirmed) return;

        setDeleting(true);
        setError("");
        setNotice("");

        try {
            await deleteCurrentUser();

            window.location.assign("/");
        } catch (err) {
            if (err instanceof HttpError && err.status === 403) {
                setError("You are not permitted to delete this account.");
            } else if (err instanceof HttpError && err.status === 409) {
                setError("Account cannot be deleted in its current state.");
            } else {
                setError(describeError(err, "Could not delete account."));
            }
        } finally {
            setDeleting(false);
        }
    }

    if (loading) {
        return (
            <main className="profile-page">
                <p role="status">Loading your profile…</p>
            </main>
        );
    }

    if (!profile) {
        return (
            <main className="profile-page">
                <h1>My profile</h1>
                <Message severity="error" text={error || "Profile unavailable."} />
            </main>
        );
    }

    return (
        <main className="profile-page">
            <header className="profile-page__heading">
                <div className="profile-page__avatar" aria-hidden="true">
                    <i className="pi pi-user" />
                </div>
                <div>
                    <h1>My profile</h1>
                    <p>Manage your One Health account</p>
                </div>
            </header>

            {error && (
                <Message
                    className="profile-page__message"
                    severity="error"
                    text={error}
                />
            )}
            {notice && (
                <Message
                    className="profile-page__message"
                    severity="success"
                    text={notice}
                />
            )}

            <Card className="profile-page__card">
                <h2>Account information</h2>

                <div className="profile-page__details">
                    <div className="profile-page__detail">
                        <span className="profile-page__label">Display name</span>
                        <strong>{profile.displayName || "Not set"}</strong>
                    </div>

                    <div className="profile-page__detail">
                        <span className="profile-page__label">Role</span>
                        <div>
                            <Tag value={profile.role} />
                        </div>
                    </div>

                    <div className="profile-page__detail">
                        <span className="profile-page__label">Credits</span>
                        <strong>
                            {profile.credits == null
                                ? "Not available"
                                : profile.credits.toLocaleString()}
                        </strong>
                    </div>

                    <div className="profile-page__detail">
                        <span className="profile-page__label">Account status</span>
                        <div>
                            <Tag
                                value={profile.enabled ? "Enabled" : "Disabled"}
                                severity={profile.enabled ? "success" : "danger"}
                            />
                        </div>
                    </div>

                    <div className="profile-page__detail">
                        <span className="profile-page__label">Last login</span>
                        <strong>
                            {profile.lastLogin
                                ? new Date(profile.lastLogin).toLocaleString()
                                : "Not available"}
                        </strong>
                    </div>
                </div>

                <Divider />

                <section className="profile-page__edit">
                    <h2>Edit display name</h2>
                    <label htmlFor="profile-display-name">Display name</label>
                    <InputText
                        id="profile-display-name"
                        value={displayName}
                        onChange={(event) => setDisplayName(event.target.value)}
                        maxLength={150}
                        autoComplete="name"
                        className="profile-page__input"
                    />
                    <div className="profile-page__actions">
                        <Button
                            label="Save changes"
                            icon="pi pi-check"
                            onClick={() => void saveProfile()}
                            loading={saving}
                            disabled={
                                saving ||
                                !displayName.trim() ||
                                displayName.trim() === profile.displayName
                            }
                        />
                    </div>
                </section>

                <Divider />

                <section className="profile-page__danger">
                    <div>
                        <h2>Delete account</h2>
                        <p>
                            Account deletion may affect saved data and related
                            records. Review the consequences before continuing.
                        </p>
                    </div>
                    <Button
                        label="Delete my account"
                        icon="pi pi-trash"
                        severity="danger"
                        outlined
                        onClick={() => void deleteAccount()}
                        loading={deleting}
                        disabled={deleting}
                    />
                </section>
            </Card>
        </main>
    );
};

export default UserProfilePage;
