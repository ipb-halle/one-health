import { flow, types } from 'mobx-state-tree';
import { UserRole } from '@/generated/auth/model';
import { getCurrentUser, logout } from '@/generated/auth/auth/auth';
import { HttpError } from '@/core/api/http/http-error';

type BrowserWindow = Pick<Window, 'location'>;

const AuthenticatedUser = types.model('AuthenticatedUser', {
    id: types.number,
    displayName: types.string,
    email: types.maybeNull(types.string),
    orcid: types.maybeNull(types.string),
    role: types.enumeration<UserRole>('UserRole', Object.values(UserRole)),
    enabled: types.boolean,
});

export const AuthStore = types
    .model('AuthStore', {
        user: types.maybeNull(AuthenticatedUser),
    })
    .views((self) => ({
        get isAuthenticated() {
            return self.user !== null;
        },
        get initials(): string {
            if (!self.user?.displayName) {
                return 'U';
            }
            const parts = self.user?.displayName.trim().split(/\s+/);
            if (parts.length === 1) {
                return parts[0].charAt(0).toUpperCase();
            }
            return (
                parts[0].charAt(0) +
                parts[parts.length - 1].charAt(0)
            ).toUpperCase();
        }
    }))
    .views((self) => ({
        get label(): string {
            return self.isAuthenticated
                ? `Hi ${self.user?.displayName ?? 'User'} . Log out`
                : 'Sign in with ORCID'
        },
        get buttonTitle(): string {
            return self.isAuthenticated
                ? `Log out ${self.user?.displayName ?? ''}`
                : 'Sign in with ORCID'
        }
    }))
    .actions((self) => ({
        clearUser() {
            self.user = null;
        },
        loadCurrentUser: flow(function* loadCurrentUser() {
            try {
                const user = yield getCurrentUser();
                self.user = AuthenticatedUser.create(user);
            } catch (error) {
                self.user = null;

                if (error instanceof HttpError && error.status == 401) {
                    return;
                }
                console.error('Failed to load current user:', error);
            }
        }),
        loginOrLogout: flow(function* loginOrLogout(browserWindow: BrowserWindow) {
            if (self.isAuthenticated) {
                try {
                    yield logout();
                    self.user = null;
                    browserWindow.location.reload();
                } catch (error) {
                    console.error('Logout failed: ', error);
                }
            } else {
                browserWindow.location.href = '/api/oauth2/authorization/orcid';
            }
        }),
    }));
