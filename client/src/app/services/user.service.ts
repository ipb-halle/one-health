export interface UpdateUserRequest {
    email: string;
}
export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
}

export interface UpdateUserRequest {
    email: string;
}

export interface ChangePasswordRequest {
    currentPassword: string;
    newPassword: string;
}

export interface User {
    id: string;
    username: string;
    email?: string;
    role: 'VIEWER' | 'CURATOR' | 'ADMIN';
    enabled: boolean;
}



