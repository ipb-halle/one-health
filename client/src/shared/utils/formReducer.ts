export function formReducer<T>(state: T, event: T): T {
    return {
        ...state,
        ...event,
    };
}
