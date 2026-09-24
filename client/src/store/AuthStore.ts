import { flow, SnapshotIn, types } from 'mobx-state-tree';
import { UserRole } from '@/generated/auth/model';
import { getCurrentUser } from '@/generated/auth/auth/auth';


const AuthenticatedUser = types.model('AuthenticatedUser', {
    id: types.number,
    displayName: types.string,
    email: types.maybeNull(types.string),
    orcid: types.maybeNull(types.string),
    role: types.enumeration<UserRole>(
        'UserRole',
        Object.values(UserRole),
    ),
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
        /*
                setUser(user: SnapshotIn<typeof AuthenticatedUser>) {
                    self.user = AuthenticatedUser.create(user);
                },*/

        clearUser() {
            self.user = null;
        },

        loadCurrentUser: flow(function* loadCurrentUser() {
            try {
                const user = yield getCurrentUser();
                self.user = AuthenticatedUser.create(user);
            } catch (error) {
                console.error('Failed to load current user:', error);
                self.user = null;
            }
        }),
    }));