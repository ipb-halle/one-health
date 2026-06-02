import React, { useState } from "react";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Password } from "primereact/password";
import { Button } from "primereact/button";
import { Message } from "primereact/message";

import { register } from "@/generated/auth/auth/auth";

interface Props {
    visible: boolean;
    onHide: () => void;
    onSuccess: () => void;
}

const RegisterDialog: React.FC<Props> = ({
    visible,
    onHide,
    onSuccess,
}) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleRegister = async () => {
        setError(null);

        if (!username.trim()) {
            setError('Username is required');
            return;
        }

        if (!email.trim()) {
            setError('Email is required');
            return;
        }

        if (password.length < 8) {
            setError('Password must contain at least 8 characters');
            return;
        }

        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }


        setLoading(true);

        try {
            await register({
                username,
                email,
                password,
            });

            setUsername('');
            setEmail('');
            setPassword('');
            setConfirmPassword('');

            onSuccess();
        } catch (e: any) {
            setError(
                e?.response?.data?.message ??
                'Registration failed. Please try again.',
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <Dialog
            header="Create Account"
            visible={visible}
            onHide={onHide}
            style={{ width: '450px' }}
            modal
        >
            <div className="p-fluid">
                <div className="field mb-3">
                    <label htmlFor="username">Username</label>
                    <InputText
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>

                <div className="field mb-3">
                    <label htmlFor="email">Email</label>
                    <InputText
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>

                <div className="field mb-3">
                    <label htmlFor="password">Password</label>
                    <Password
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        toggleMask
                    />
                </div>

                <div className="field mb-3">
                    <label htmlFor="confirmPassword">Confirm Password</label>
                    <Password
                        id="confirmPassword"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        toggleMask
                        feedback={false}
                    />
                </div>
                {error && (
                    <div className="mb-3">
                        <Message severity="error" text={error} />

                    </div>
                )}
                <Button
                    label="Create Account"
                    icon="pi pi-user-plus"
                    loading={loading}
                    onClick={handleRegister}
                />
            </div>
        </Dialog>
    );
};

export default RegisterDialog;

