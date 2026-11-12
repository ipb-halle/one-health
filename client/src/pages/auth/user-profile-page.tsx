import { useEffect, useState } from 'react';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Tag } from 'primereact/tag';
import { Divider } from 'primereact/divider';
import { Message } from 'primereact/message';
import './user-profile-page.scss';

import { getCurrentUser } from '@/generated/auth/auth/auth';

import {
    updateCurrentUser,
    deleteCurrentUser,
} from '@/generated/auth/users/users';

type UserRole = 'VIEWER' | 'CURATOR' | 'ADMIN';

interface UserProfile {
    id: string;
    displayName: string;
    role: UserRole;
    enabled: boolean;
    lastLogin?: string | null;
    credits?: number | null;
}

const API_BASE = '/api';

const UserProfilePage: React.FC = () => {
    const [profile, setProfile] = useState<UserProfile | null>(null);
    const [displayName, setDisplayName] = useState('');
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [error, setError] = useState('');
    const [notice, setNotice] = useState('');

    useEffect(() => {
        const controller = new AbortController();

        async function loadProfile() {
            setLoading(true);
            setError('');

            try {
                const user = await getCurrentUser();
                const userProfile = user as unknown as UserProfile;

                if (!userProfile || typeof userProfile.id === 'undefined') {
                    throw new Error('Invalid user profile response.');
                }

                setProfile({
                    ...userProfile,
                    id: String(userProfile.id),
                });
                setDisplayName(userProfile.displayName ?? '');
            } catch (err) {
                if (controller.signal.aborted) return;
                setError(
                    err instanceof Error
                        ? err.message
                        : 'An unexpected error occurred.',
                );
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
            setError('Display name cannot be empty.');
            return;
        }

        setSaving(true);
        setError('');
        setNotice('');

        try {
            const response = await updateCurrentUser({
                displayName: trimmedName,
            });

            const updatedProfile = response as unknown as UserProfile;

            if (!updatedProfile || typeof updatedProfile.id === 'undefined') {
                throw new Error('Invalid updated profile response.');
            }

            const updated = {
                ...updatedProfile,
                id: String(updatedProfile.id),
            };

            setProfile(updated);
            setDisplayName(updated.displayName ?? '');
            setNotice('Your profile has been updated.');
        } catch (err) {
            setError(
                err instanceof Error ? err.message : 'Could not save profile.',
            );
        } finally {
            setSaving(false);
        }
    }

    async function deleteAccount() {
        const confirmed = window.confirm(
            'Are you sure you want to delete your account? ' +
                'This action may be irreversible. Review the data-retention ' +
                'information before proceeding.',
        );

        if (!confirmed) return;

        setDeleting(true);
        setError('');
        setNotice('');

        try {
            const response = await fetch(`${API_BASE}/users/me`, {
                method: 'DELETE',
                credentials: 'include',
                headers: {
                    Accept: 'application/json',
                    // Add the project's CSRF header/token here if required.
                },
            });

            if (!response.ok) {
                throw new Error(
                    `Account deletion failed (${response.status}).`,
                );
            }

            // Prefer the application's existing logout/session-cleanup flow.
            window.location.assign('/');
        } catch (err) {
            setError(
                err instanceof Error
                    ? err.message
                    : 'Could not delete account.',
            );
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
                <Message
                    severity="error"
                    text={error || 'Profile unavailable.'}
                />
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
                        <span className="profile-page__label">
                            Display name
                        </span>
                        <strong>{profile.displayName || 'Not set'}</strong>
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
                                ? 'Not available'
                                : profile.credits.toLocaleString(undefined, {
                                      minimumFractionDigits: 2,
                                      maximumFractionDigits: 2,
                                  })}
                        </strong>
                    </div>

                    <div className="profile-page__detail">
                        <span className="profile-page__label">
                            Account status
                        </span>
                        <div>
                            <Tag
                                value={profile.enabled ? 'Enabled' : 'Disabled'}
                                severity={
                                    profile.enabled ? 'success' : 'danger'
                                }
                            />
                        </div>
                    </div>

                    <div className="profile-page__detail">
                        <span className="profile-page__label">Last login</span>
                        <strong>
                            {profile.lastLogin
                                ? new Date(profile.lastLogin).toLocaleString()
                                : 'Not available'}
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
                        maxLength={200}
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
