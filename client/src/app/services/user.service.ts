export interface UpdateUserRequest {
    displayName: string;
}
export interface RegisterRequest {
    username: string;
    password: string;
}


export interface ChangePasswordRequest {
    currentPassword: string;
    newPassword: string;
}

export interface User {
    id: string;
    username: string;
    role: 'VIEWER' | 'CURATOR' | 'ADMIN';
    enabled: boolean;
}



