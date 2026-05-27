import React, { useState } from 'react';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';
import { authService } from '../../services/auth.service';

interface Props {
    visible: boolean;
    onHide: () => void;
    onLoginSuccess: () => void;
}

const LoginDialog: React.FC<Props> = ({ visible, onHide, onLoginSuccess }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleLogin = async () => {
        setLoading(true);
        setError(null);

        try {
            const res = await authService.login({ username, password });
            authService.setSession(res);

            setUsername('');
            setPassword('');

            onLoginSuccess();
            onHide();
        } catch (e: any) {
            setError(
                e?.response?.data?.message || 'Login failed. Please try again.',
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <Dialog
            header="Login"
            visible={visible}
            onHide={onHide}
            style={{ width: '400px' }}
            modal
        >
            <div className="p-fluid">
                <div className="field">
                    <label>Username</label>
                    <InputText
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>

                <div className="field">
                    <label>Password</label>
                    <Password
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        toggleMask
                        feedback={false}
                    />
                </div>

                {error && (
                    <small style={{ color: 'red' }}>{error}</small>
                )}

                <Button
                    label="Login"
                    icon="pi pi-sign-in"
                    onClick={handleLogin}
                    loading={loading}
                    className="mt-3"
                />
            </div>
        </Dialog>
    );
};

export default LoginDialog;