import { flow, types } from 'mobx-state-tree';
import { UserRole } from '@/generated/auth/model';
import { getCurrentUser } from '@/generated/auth/auth/auth';
import { HttpError } from '@/app/services/orval-fetch';

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
    }));
