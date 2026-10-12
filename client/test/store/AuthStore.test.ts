import { AuthStore } from "@/store/AuthStore";
import { UserRole } from '@/generated/auth/model';


let originalFetch: typeof globalThis.fetch | undefined;

beforeEach(() => {
    originalFetch = globalThis.fetch;
});

afterEach(() => {
    if (originalFetch) {
        globalThis.fetch = originalFetch;
    } else {
        delete (globalThis as { fetch?: typeof fetch }).fetch;
    }
});


describe("AuthStore", () => {
    // Test the initial state of the AuthStore
    describe("initial state", () => {
        it("user is not authenticated", () => {
            const authStore = AuthStore.create();

            expect(authStore.user).toBeNull();
            expect(authStore.isAuthenticated).toBe(false);
        });
    });

    // Test the loadCurrentUser action
    it('loads the authenticated user successfully', async () => {
        const user = {
            id: 1,
            displayName: 'John Doe',
            email: 'john@example.com',
            orcid: '0000-0001-2345-6789',
            role: UserRole.VIEWER,
            enabled: true,
        };

        globalThis.fetch = jest.fn().mockResolvedValue({
            ok: true,
            status: 200,
            headers: new Headers({
                'content-type': 'application/json',
            }),
            json: async () => user,
        });

        const authStore = AuthStore.create();

        await authStore.loadCurrentUser();

        expect(authStore.user?.displayName).toBe('John Doe');
        expect(authStore.user?.role).toBe(UserRole.VIEWER);
        expect(authStore.isAuthenticated).toBe(true);
    });

    // Test the loadCurrentUser action when the user is not authenticated (HTTP 401)
    it('handles HTTP 401 without logging an error', async () => {
        globalThis.fetch = jest.fn().mockResolvedValue({
            ok: false,
            status: 401,
            headers: new Headers(),
            text: async () => '',
        });

        const consoleSpy = jest.spyOn(console, 'error')
            .mockImplementation(() => { });

        try {
            const authStore = AuthStore.create();

            await authStore.loadCurrentUser();

            expect(authStore.user).toBeNull();
            expect(consoleSpy).not.toHaveBeenCalled();
        } finally {
            consoleSpy.mockRestore();
        }
    });

});
