import { AuthStore } from "@/store/AuthStore";

describe("AuthStore", () => {
    describe("initial state", () => {
        it("user is not authenticated", () => {
            const authStore = AuthStore.create();

            expect(authStore.user).toBeNull();
            expect(authStore.isAuthenticated).toBe(false);
        });
    });
});
